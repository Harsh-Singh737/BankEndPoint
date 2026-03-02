package com.bankingtransactions.bankingendpoints.service;

import com.bankingtransactions.bankingendpoints.eventListener.AccountCreatedEvent;
import com.bankingtransactions.bankingendpoints.model.Account;
import com.bankingtransactions.bankingendpoints.model.Customer;
import com.bankingtransactions.bankingendpoints.repository.AccountRepository;
import com.bankingtransactions.bankingendpoints.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;


    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @Transactional
    public Optional<Account> createAccount(Long customerId, Account account) {
        return customerRepository.findById(customerId).map(customer -> {
            // 🔹 Step 1: Link customer with account
            account.setCustomer(customer);

            // 🔹 Step 2: Save account initially (to generate ID)
            Account savedAccount = accountRepository.save(account);

            // 🔹 Step 3: Generate account number after saving
            String generatedAccNumber = String.format("ACC%05d", savedAccount.getAccountId());
            savedAccount.setAccountNumber(generatedAccNumber);

            // 🔹 Step 4: Save again with generated account number
            Account finalAccount = accountRepository.save(savedAccount);

            String fullName = customer.getFirstName() + " " + customer.getLastName();

            // 🔹 Step 5: Sending mail Async
            eventPublisher.publishEvent(
                    new AccountCreatedEvent(
                            customer.getEmail(),
                            fullName,
                            generatedAccNumber
                    )
            );

            return finalAccount;
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
            String fullName = customer.getFirstName() + " " + customer.getLastName();
            emailService.sendEmail(
                    customer.getEmail(),
                    "Account Deletion Confirmation - Harsh Bank",
                    "Dear " + fullName + ",\n\n"
                            + "Your account (Account Number: " + account.getAccountNumber() + ") "
                            + "has been successfully deleted from Harsh Bank.\n\n"
                            + "If you did not request this action, please contact our support team immediately.\n\n"
                            + "Thank you for being with Harsh Bank."
            );

            return true;
        }).orElse(false);
    }

}
