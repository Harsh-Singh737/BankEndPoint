package com.bankingtransactions.bankingendpoints.service;

import com.bankingtransactions.bankingendpoints.model.Account;
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




    public List<Account> getAllAccounts(PageRequest pageRequest) {
        return accountRepository.findAll(pageRequest).getContent();
    }


    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Optional<Account> createAccount(Long customerId, Account account) {
        return customerRepository.findById(customerId).map(customer -> {
            // 🔹 Step 1: Set the customer to link the relationship
            account.setCustomer(customer);

            // 🔹 Step 2: Save account initially
            Account savedAccount = accountRepository.save(account);

            // 🔹 Step 3: Generate and set account number after ID is known
            String generatedAccNumber = String.format("ACC%05d", savedAccount.getAccountId());
            savedAccount.setAccountNumber(generatedAccNumber);

            // 🔹 Step 4: Save again with account number
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





    public boolean deleteAccount(Long id) {
        return accountRepository.findById(id).map(account -> {
            accountRepository.delete(account);
            return true;
        }).orElse(false);
    }
}
