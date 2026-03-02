package com.bankingtransactions.bankingendpoints.controller;

import com.bankingtransactions.bankingendpoints.model.Account;
import com.bankingtransactions.bankingendpoints.model.Customer;
import com.bankingtransactions.bankingendpoints.model.Transaction;
import com.bankingtransactions.bankingendpoints.model.Transfer;
import com.bankingtransactions.bankingendpoints.service.AdminService;
import com.bankingtransactions.bankingendpoints.config.RateLimiterConfig;
import com.bankingtransactions.bankingendpoints.uitils.JwtUtil;

import io.github.bucket4j.Bucket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RateLimiterConfig rateLimiter;

    // =========================
    // ADMIN LOGIN
    // =========================
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.get("username"),
                        request.get("password"))
        );

        String token = jwtUtil.generateToken(authentication.getName());
        return ResponseEntity.ok(Map.of("token", token));
    }

    // =========================
    // COMMON RATE LIMIT CHECK
    // =========================
    private boolean isAllowed(HttpServletRequest request) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        // Unique key per user per endpoint
        String key = username + "_" + request.getRequestURI();

        Bucket bucket = rateLimiter.resolveBucket(key);
        return bucket.tryConsume(1);
    }

    private Sort getSort(String sortBy, String sortOrder) {
        return sortOrder.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
    }

    // =========================
    // GET ALL ACCOUNTS
    // =========================
    @GetMapping("/get-all-accounts")
    public ResponseEntity<?> getAllAccounts(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "accountId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortOrder) {

        if (!isAllowed(request)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too Many Requests.");
        }

        try {
            List<Account> accounts = adminService.getAllAccounts(
                    PageRequest.of(pageNo, pageSize, getSort(sortBy, sortOrder)));

            return ResponseEntity.ok(accounts);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while fetching accounts: " + e.getMessage());
        }
    }

    // =========================
    // GET ALL CUSTOMERS
    // =========================
    @GetMapping("/get-all-customers")
    public ResponseEntity<?> getAllCustomers(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "customerId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortOrder) {

        if (!isAllowed(request)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too Many Requests.");
        }

        try {
            List<Customer> customers = adminService.getAllCustomers(
                    PageRequest.of(pageNo, pageSize, getSort(sortBy, sortOrder)));

            return ResponseEntity.ok(customers);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while fetching customers: " + e.getMessage());
        }
    }

    // =========================
    // GET ALL TRANSACTIONS
    // =========================
    @GetMapping("/get-all-transactions")
    public ResponseEntity<?> getAllTransactions(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "transactionId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortOrder) {

        if (!isAllowed(request)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too Many Requests.");
        }

        try {
            List<Transaction> transactions = adminService.getAllTransactions(
                    PageRequest.of(pageNo, pageSize, getSort(sortBy, sortOrder)));

            return ResponseEntity.ok(transactions);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while fetching transactions: " + e.getMessage());
        }
    }

    // =========================
    // GET ALL TRANSFERS
    // =========================
    @GetMapping("/get-all-transfers")
    public ResponseEntity<?> getAllTransfers(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "transferId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortOrder) {

        if (!isAllowed(request)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too Many Requests.");
        }

        try {
            List<Transfer> transfers = adminService.getAllTransfers(
                    PageRequest.of(pageNo, pageSize, getSort(sortBy, sortOrder)));

            return ResponseEntity.ok(transfers);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while fetching transfers: " + e.getMessage());
        }
    }
}