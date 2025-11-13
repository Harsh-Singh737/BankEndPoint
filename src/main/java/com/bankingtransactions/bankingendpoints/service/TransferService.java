package com.bankingtransactions.bankingendpoints.service;

import com.bankingtransactions.bankingendpoints.model.Account;
import com.bankingtransactions.bankingendpoints.model.Customer;
import com.bankingtransactions.bankingendpoints.model.Transfer;
import com.bankingtransactions.bankingendpoints.repository.AccountRepository;
import com.bankingtransactions.bankingendpoints.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmailService emailService;

    public Optional<Transfer> getTransferById(Long id) {
        return transferRepository.findById(id);
    }

    public Transfer createTransfer(Transfer transfer) {

        // Check if accounts exist
        Account fromAcc = accountRepository.findById(transfer.getFromAccountId())
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account toAcc = accountRepository.findById(transfer.getToAccountId())
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        // Save transfer (trigger handles actual logic)
        Transfer saved = transferRepository.save(transfer);

        // Notify both customers
        sendEmailNotifications(fromAcc, toAcc, transfer);

        return saved;
    }

    private void sendEmailNotifications(Account fromAcc, Account toAcc, Transfer transfer) {

        Customer sender = fromAcc.getCustomer();
        Customer receiver = toAcc.getCustomer();

        String amount = String.format("%.2f", transfer.getAmount());

        // ===========================
        // EMAIL FOR SENDER (DEBIT)
        // ===========================
        String debitEmail = """
                Dear %s %s,

                ₹%s has been debited from your account %s.

                Transfer Details:
                • To Account: %s
                • Amount: ₹%s
                • Avlb Balance: %s
                • Remarks: %s

                Thank you for banking with Harsh Bank.
                """.formatted(
                sender.getFirstName(), sender.getLastName(),
                amount,
                fromAcc.getAccountNumber(),
                toAcc.getAccountNumber(),
                amount,
                fromAcc.getBalance().subtract((new BigDecimal(amount))),
                transfer.getRemarks()
        );

        emailService.sendEmail(sender.getEmail(), "Amount Debited", debitEmail);

        // ===========================
        // EMAIL FOR RECEIVER (CREDIT)
        // ===========================
        String creditEmail = """
                Dear %s %s,

                ₹%s has been credited to your account %s.

                Transfer Details:
                • From Account: %s
                • Amount: ₹%s
                • Avlb Balance: ₹%s
                • Remarks: %s

                Thank you for banking with Harsh Bank.
                """.formatted(
                receiver.getFirstName(), receiver.getLastName(),
                amount,
                toAcc.getAccountNumber(),
                fromAcc.getAccountNumber(),
                amount,
                fromAcc.getBalance().add(new BigDecimal(amount)),
                transfer.getRemarks()
        );

        emailService.sendEmail(receiver.getEmail(), "Amount Credited", creditEmail);

        System.out.println("📧 Email notifications sent!");
    }

    public Optional<Transfer> updateTransfer(Long id, Transfer updatedTransfer) {
        return transferRepository.findById(id).map(existing -> {
            existing.setFromAccountId(updatedTransfer.getFromAccountId());
            existing.setToAccountId(updatedTransfer.getToAccountId());
            existing.setAmount(updatedTransfer.getAmount());
            existing.setRemarks(updatedTransfer.getRemarks());
            return transferRepository.save(existing);
        });
    }

    public boolean deleteTransfer(Long id) {
        if (transferRepository.existsById(id)) {
            transferRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

