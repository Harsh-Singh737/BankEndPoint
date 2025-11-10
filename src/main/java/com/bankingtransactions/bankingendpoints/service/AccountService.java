package com.bankingtransactions.bankingendpoints.service;

import com.bankingtransactions.bankingendpoints.model.Account;
import com.bankingtransactions.bankingendpoints.model.Customer;
import com.bankingtransactions.bankingendpoints.repository.AccountRepository;
import com.bankingtransactions.bankingendpoints.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmailService emailService;



    public List<Account> getAllAccounts(PageRequest pageRequest) {
        return accountRepository.findAll(pageRequest).getContent();
    }


    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Optional<Account> createAccount(Long customerId, Account account) {
        return customerRepository.findById(customerId).map(customer -> {
            // 🔹 Step 1: Link customer with account
            account.setCustomer(customer);

            // 🔹 Step 2: Save account initially (to generate ID)
            Account savedAccount = accountRepository.save(account);

            // 🔹 Step 3: Generate account number after saving
            String generatedAccNumber = String.format("ACC%05d", savedAccount.getAccountId());
            savedAccount.setAccountNumber(generatedAccNumber);

            // 🔹 Step 4: Send welcome email using customer's email
            emailService.sendEmail(
                    customer.getEmail(),
                    "Welcome to Harsh Bank!",
                    "Dear " + customer.getFirstName() + ",\n\n"
                            + "Your new account has been created successfully.\n"
                            + "Account Number: " + generatedAccNumber + "\n\n"
                            + "Thank you for choosing Harsh Bank!"
            );

            // 🔹 Step 5: Save again with generated account number
            return accountRepository.save(savedAccount);
        });
    }






    public Account updateAccount(Long accountId, Account updatedData) {
        return accountRepository.findById(accountId).map(existingAccount -> {

            if (updatedData.getAccountType() != null) {
                existingAccount.setAccountType(updatedData.getAccountType());
            }

            if (updatedData.getBalance() != null) {
                existingAccount.setBalance(updatedData.getBalance());
            }

            if (updatedData.getAccountNumber() != null) {
                existingAccount.setAccountNumber(updatedData.getAccountNumber());
            }

            return accountRepository.save(existingAccount);

        }).orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
    }





    public boolean deleteAccount(Long accountId) {
        return accountRepository.findById(accountId).map(account -> {
            Customer customer = account.getCustomer();

            // 🔹 Step 1: Delete the account
            accountRepository.delete(account);

            // 🔹 Step 2: Send account deletion email
            emailService.sendEmail(
                    customer.getEmail(),
                    "Account Deletion Confirmation - Harsh Bank",
                    "Dear " + customer.getFirstName() + ",\n\n"
                            + "Your account (Account Number: " + account.getAccountNumber() + ") "
                            + "has been successfully deleted from Harsh Bank.\n\n"
                            + "If you did not request this action, please contact our support team immediately.\n\n"
                            + "Thank you for being with Harsh Bank."
            );

            return true;
        }).orElse(false);
    }

}
