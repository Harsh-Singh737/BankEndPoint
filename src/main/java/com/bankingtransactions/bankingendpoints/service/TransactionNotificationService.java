package com.bankingtransactions.bankingendpoints.service;

import com.bankingtransactions.bankingendpoints.model.Customer;
import com.bankingtransactions.bankingendpoints.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionNotificationService {

    @Autowired
    private EmailService emailService;

    public void notifyCustomer(Transaction txn) {
        Customer customer = txn.getAccount().getCustomer();

        String amount = String.format("%.2f", txn.getAmount());

        boolean isCredit = txn.getTransactionType().equalsIgnoreCase("CREDIT");

        String subject = isCredit ?
                "Amount Credited to Your Account" :
                "Amount Debited from Your Account";

        String message = """
                Dear %s %s,

                A %s transaction has been recorded on your account.

                Transaction Details:
                • Type: %s
                • Amount: ₹%s
                • Date: %s
                • Account Number: %s
                • Description: %s

                Thank you for banking with Harsh Bank.
                """.formatted(
                customer.getFirstName(),
                customer.getLastName(),
                txn.getTransactionType(),
                txn.getTransactionType(),
                amount,
                txn.getTransactionDate(),
                txn.getAccount().getAccountNumber(),
                txn.getDescription()
        );

        emailService.sendEmail(customer.getEmail(), subject, message);

        System.out.println("📧 Transaction email sent to " + customer.getEmail());
    }
}

