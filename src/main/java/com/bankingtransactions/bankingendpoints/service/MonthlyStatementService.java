package com.bankingtransactions.bankingendpoints.service;

import com.bankingtransactions.bankingendpoints.model.Customer;
import com.bankingtransactions.bankingendpoints.model.Transaction;
import com.bankingtransactions.bankingendpoints.repository.CustomerRepository;
import com.bankingtransactions.bankingendpoints.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MonthlyStatementService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 🕒 Runs on 1st of every month at 8 AM
     * Cron format: second minute hour day month weekday
     */
    @Scheduled(cron = "0 0 8 1 * *")
    public void sendMonthlyStatements() {
        List<Customer> customers = customerRepository.findAll();

        for (Customer customer : customers) {
            sendStatementForCustomer(customer);
        }
    }

    private void sendStatementForCustomer(Customer customer) {
        // 🔹 Define previous month’s date range
        LocalDateTime start = LocalDate.now()
                .minusMonths(1)
                .withDayOfMonth(1)
                .atStartOfDay();

        LocalDateTime end = LocalDate.now()
                .withDayOfMonth(1)
                .atStartOfDay()
                .minusSeconds(1);

        // 🔹 Fetch transactions for that customer and date range
        List<Transaction> transactions = transactionRepository
                .findByAccountCustomerCustomerIdAndTransactionDateBetween(
                        customer.getCustomerId(),
                        start,
                        end
                );

        if (transactions.isEmpty()) {
            System.out.println("⚠️ No transactions for customer: " + customer.getEmail());
            return;
        }

        // 🔹 Build summary text
        String summary = buildTransactionSummary(transactions, start, end, customer);

        // 🔹 Send the email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(customer.getEmail());
        message.setSubject("Your Monthly Statement - " + start.getMonth() + " " + start.getYear());
        message.setText(summary);

        mailSender.send(message);
        System.out.println("📧 Sent monthly statement to " + customer.getEmail());
    }

    private String buildTransactionSummary(List<Transaction> transactions, LocalDateTime start, LocalDateTime end, Customer customer) {
        StringBuilder sb = new StringBuilder();
        sb.append("Dear ").append(customer.getFirstName() != null ? customer.getFirstName() : "Customer").append(",\n\n")
                .append("Here is your transaction summary for ")
                .append(start.getMonth()).append(" ").append(start.getYear()).append(":\n\n");

        for (Transaction txn : transactions) {
            sb.append("• Date: ").append(txn.getTransactionDate())
                    .append(", Type: ").append(txn.getTransactionType())
                    .append(", Amount: ₹").append(txn.getAmount())
                    .append(", Account ID: ").append(txn.getAccount().getAccountId())
                    .append("\n");
        }

        sb.append("\nTotal Transactions: ").append(transactions.size())
                .append("\n\nThank you for banking with Harsh Bank!\n\n")
                .append("Best regards,\nHarsh Bank Team");

        return sb.toString();
    }
}

