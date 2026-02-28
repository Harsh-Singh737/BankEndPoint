package com.bankingtransactions.bankingendpoints.service;

import com.bankingtransactions.bankingendpoints.model.Customer;
import com.bankingtransactions.bankingendpoints.model.Transaction;
import com.bankingtransactions.bankingendpoints.repository.CustomerRepository;
import com.bankingtransactions.bankingendpoints.repository.TransactionRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MonthlyStatementService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private JavaMailSender mailSender;


    @Scheduled(cron = "0 0 8 1 * *") // Run on 1st day of each month at 8 AM
    public void sendMonthlyStatementsToSelectedCustomers() {

        // 🧾 Add only the customers you want to send the statement to
        List<String> selectedEmails = List.of(
                "harshsinghlo737@gmail.com",
                "harshsinghlo737@mpgi.edu.in"
        );

        for (String email : selectedEmails) {
            Customer customer = customerRepository.findByEmail(email);
            if (customer == null) {
                System.out.println("No customer found with email: " + email);
                continue;
            }
            sendStatementForCustomer(customer);
        }
    }

    private void sendStatementForCustomer(Customer customer) {
        // 🔹 Define the date range for the *previous month*
        LocalDateTime start = LocalDate.now()
                .minusMonths(1)
                .withDayOfMonth(1)
                .atStartOfDay();

        LocalDateTime end = LocalDate.now()
                .withDayOfMonth(1)
                .atStartOfDay()
                .minusSeconds(1);

        // 🔹 Fetch transactions for the given customer & date range
        List<Transaction> transactions = transactionRepository
                .findByAccountCustomerCustomerIdAndTransactionDateBetween(
                        customer.getCustomerId(),
                        start,
                        end
                );

        if (transactions.isEmpty()) {
            System.out.println("No transactions found for: " + customer.getEmail());
            return;
        }

        // 🔹 Build HTML content
        String htmlContent = buildHtmlTransactionTable(transactions, customer, start, end);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(customer.getEmail());
            helper.setSubject("Your Monthly Statement - " +
                    start.getMonth() + " " + start.getYear());
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            System.out.println("Sent monthly statement to: " + customer.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildHtmlTransactionTable(List<Transaction> transactions,
                                             Customer customer,
                                             LocalDateTime start,
                                             LocalDateTime end) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a");

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h2 style='color:#2E86C1;'>Harsh Bank - Monthly Transaction Statement</h2>");
        sb.append("<p>Dear <b>")
                .append(customer.getFirstName() != null ? customer.getFirstName() : "Customer")
                .append("</b>,</p>");
        sb.append("<p>Here is your transaction summary for ")
                .append(start.getMonth()).append(" ").append(start.getYear())
                .append(":</p>");

        sb.append("<table style='border-collapse: collapse; width:100%;'>")
                .append("<tr style='background-color:#f2f2f2;'>")
                .append("<th style='border:1px solid #ddd; padding:8px;'>Date</th>")
                .append("<th style='border:1px solid #ddd; padding:8px;'>Type</th>")
                .append("<th style='border:1px solid #ddd; padding:8px;'>Amount (₹)</th>")
                .append("<th style='border:1px solid #ddd; padding:8px;'>Account ID</th>")
                .append("</tr>");

        for (Transaction txn : transactions) {
            sb.append("<tr>")
                    .append("<td style='border:1px solid #ddd; padding:8px;'>")
                    .append(txn.getTransactionDate().format(formatter))
                    .append("</td>")
                    .append("<td style='border:1px solid #ddd; padding:8px;'>")
                    .append(txn.getTransactionType())
                    .append("</td>")
                    .append("<td style='border:1px solid #ddd; padding:8px;'>₹")
                    .append(txn.getAmount())
                    .append("</td>")
                    .append("<td style='border:1px solid #ddd; padding:8px;'>")
                    .append(txn.getAccount().getAccountNumber())
                    .append("</td>")
                    .append("</tr>");
        }

        sb.append("</table>");
        sb.append("<p style='margin-top:15px;'>Thank you for banking with <b>Harsh Bank</b>!</p>");
        sb.append("<p>Best Regards,<br><b>Harsh Bank Team</b></p>");
        sb.append("</body></html>");

        return sb.toString();
    }
}





































//    private void sendFullStatementForCustomer(Customer customer) {
//
//        List<Account> accounts = customer.getAccounts();
//
//        if (accounts == null || accounts.isEmpty()) {
//            System.out.println("Customer has no accounts: " + customer.getEmail());
//            return;
//        }
//
//        LocalDateTime earliestStart = null;
//        LocalDateTime end = LocalDateTime.now();
//
//        StringBuilder emailContent = new StringBuilder();
//        emailContent.append("<html><body>");
//        emailContent.append("<h2 style='color:#2E86C1;'>Harsh Bank - Full Transaction Statement</h2>");
//        emailContent.append("<p>Dear <b>")
//                .append(customer.getFirstName()).append(" ").append(customer.getLastName())
//                .append("</b>,</p>");
//
//        boolean hasAnyTransaction = false;
//
//        // Loop through all accounts
//        for (Account acc : accounts) {
//
//            // Find earliest transaction for this account
//            Transaction firstTxn = transactionRepository
//                    .findTopByAccount_AccountIdOrderByTransactionDateAsc(acc.getAccountId());
//
//            if (firstTxn == null) {
//                System.out.println("⚠️ No transactions for account: " + acc.getAccountNumber());
//                continue;
//            }
//
//            // Update earliestStart across all accounts
//            if (earliestStart == null || firstTxn.getTransactionDate().isBefore(earliestStart)) {
//                earliestStart = firstTxn.getTransactionDate();
//            }
//
//            // Fetch all transactions for this account
//            List<Transaction> transactions = transactionRepository
//                    .findByAccount_AccountIdAndTransactionDateBetween(
//                            acc.getAccountId(),
//                            firstTxn.getTransactionDate(),
//                            end
//                    );
//
//            if (transactions.isEmpty()) continue;
//
//            hasAnyTransaction = true;
//
//            // Build table for this account
//            emailContent.append("<h3>Account: ")
//                    .append(acc.getAccountNumber())
//                    .append("</h3>");
//            emailContent.append(buildHtmlTransactionTable(transactions));
//        }
//
//        // If no transactions at all
//        if (!hasAnyTransaction) {
//            System.out.println("No transactions found for customer: " + customer.getEmail());
//            return;
//        }
//
//        // Add date range at top
//        emailContent.insert(emailContent.indexOf("</p>") + 4,
//                "<p>Statement period: <b>" +
//                        earliestStart.toLocalDate() + "</b> to <b>" +
//                        end.toLocalDate() + "</b></p><br>"
//        );
//
//        emailContent.append("<br><p>Thank you for banking with <b>Harsh Bank</b>!</p>");
//        emailContent.append("</body></html>");
//
//        // Send email
//        sendHtmlEmail(
//                customer.getEmail(),
//                "Your Full Transaction Statement",
//                emailContent.toString()
//        );
//    }






// transaction repo  ------->
//List<Transaction> findByAccount_AccountIdAndTransactionDateBetween(
//        Long accountId,
//        LocalDateTime start,
//        LocalDateTime end
//);
//
//Transaction findTopByAccount_AccountIdOrderByTransactionDateAsc(Long accountId);