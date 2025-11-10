package com.bankingtransactions.bankingendpoints.controller;

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
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        try {
            Account account = accountService.getAccountById(id)
                    .orElseThrow(() -> new RuntimeException("Account not found with ID: " + id));
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching account: " + e.getMessage());
        }
    }


    @PostMapping("/customer/{customerId}")
    public ResponseEntity<?> createAccount(@PathVariable Long customerId, @RequestBody Account account) {
        try {
            Account createdAccount = accountService.createAccount(customerId, account)
                    .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating account: " + e.getMessage());
        }
    }



    @PutMapping("/update-account/{accountId}")
    public ResponseEntity<?> updateAccount(@PathVariable Long accountId, @RequestBody Account updatedData) {
        try {
            Account updatedAccount = accountService.updateAccount(accountId, updatedData);
            return ResponseEntity.ok(updatedAccount);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating account: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        try {
            boolean deleted = accountService.deleteAccount(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting account: " + e.getMessage());
        }
    }
}
