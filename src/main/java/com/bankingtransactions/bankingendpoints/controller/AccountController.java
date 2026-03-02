package com.bankingtransactions.bankingendpoints.controller;

import com.bankingtransactions.bankingendpoints.exception.ResourceNotFoundException;
import com.bankingtransactions.bankingendpoints.model.Account;
import com.bankingtransactions.bankingendpoints.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {

        Account account = accountService.getAccountById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account not found with ID: " + id));

        return ResponseEntity.ok(account);
    }

    @PostMapping("/customer/{customerId}")
    public ResponseEntity<Account> createAccount(@PathVariable Long customerId,
                                                 @RequestBody Account account) {

        Account createdAccount = accountService.createAccount(customerId, account)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with ID: " + customerId));

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @PutMapping("/update-account/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long accountId,
                                                 @RequestBody Account updatedData) {

        Account updatedAccount = accountService.updateAccount(accountId, updatedData);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {

        boolean deleted = accountService.deleteAccount(id);

        if (!deleted) {
            throw new ResourceNotFoundException("Account not found with ID: " + id);
        }

        return ResponseEntity.noContent().build();
    }
}