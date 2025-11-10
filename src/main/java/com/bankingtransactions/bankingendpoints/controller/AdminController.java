package com.bankingtransactions.bankingendpoints.controller;

import com.bankingtransactions.bankingendpoints.model.Account;
import com.bankingtransactions.bankingendpoints.model.Customer;
import com.bankingtransactions.bankingendpoints.model.Transaction;
import com.bankingtransactions.bankingendpoints.model.Transfer;
import com.bankingtransactions.bankingendpoints.service.AccountService;
import com.bankingtransactions.bankingendpoints.service.CustomerService;
import com.bankingtransactions.bankingendpoints.service.TransactionService;
import com.bankingtransactions.bankingendpoints.service.TransferService;
import com.bankingtransactions.bankingendpoints.uitils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {


    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransferService transferService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.get("username"), request.get("password"))
        );
        String token = jwtUtil.generateToken(authentication.getName());
        return ResponseEntity.ok(Map.of("token", token));
    }



    @GetMapping("/get-all-accounts")
    public ResponseEntity<?> getAllAccounts(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "accountId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortOrder) {

        try {
            Sort sort = sortOrder.equalsIgnoreCase("ASC")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            List<Account> accounts = accountService.getAllAccounts(PageRequest.of(pageNo, pageSize, sort));
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while fetching accounts: " + e.getMessage());
        }
    }

    @GetMapping("/get-all-customers")
    public ResponseEntity<?> getAllCustomer(@RequestParam(defaultValue = "0") int pageNo,
                                            @RequestParam(defaultValue = "10") int pageSize,
                                            @RequestParam(defaultValue = "customerId") String sortBy,
                                            @RequestParam(defaultValue = "ASC") String sortOrder) {
        try {
            Sort sort = sortOrder.equalsIgnoreCase("ASC")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
            List<Customer> customers = customerService.getAllCustomers(PageRequest.of(pageNo, pageSize, sort));
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while fetching customers: " + e.getMessage());
        }
    }

    @GetMapping("/get-all-transactions")
    public ResponseEntity<?> getAllTransactions(@RequestParam(defaultValue = "0") int pageNo,
                                                @RequestParam(defaultValue = "10") int pageSize,
                                                @RequestParam(defaultValue = "transactionId") String sortBy,
                                                @RequestParam(defaultValue = "ASC") String sortOrder) {
        try {
            Sort sort = sortOrder.equalsIgnoreCase("ASC")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
            List<Transaction> transactions = transactionService.getAllTransactions(PageRequest.of(pageNo, pageSize, sort));
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while fetching transactions: " + e.getMessage());
        }
    }

    @GetMapping("/get-all-transfers")
    public ResponseEntity<?> getAllTransfers(@RequestParam(defaultValue = "0") int pageNo,
                                             @RequestParam(defaultValue = "10") int pageSize,
                                             @RequestParam(defaultValue = "transferId") String sortBy,
                                             @RequestParam(defaultValue = "ASC") String sortOrder) {
        try {
            Sort sort = sortOrder.equalsIgnoreCase("ASC")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
            List<Transfer> transactions = transferService.getAllTransfers(PageRequest.of(pageNo, pageSize, sort));
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while fetching transactions: " + e.getMessage());
        }
    }
}