package com.bankingtransactions.bankingendpoints.controller;

import com.bankingtransactions.bankingendpoints.exception.ResourceNotFoundException;
import com.bankingtransactions.bankingendpoints.model.Customer;
import com.bankingtransactions.bankingendpoints.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {

        Customer customer = customerService.getCustomerById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with ID: " + id));

        return ResponseEntity.ok(customer);
    }

    @PostMapping("/create")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {

        Customer createdCustomer = customerService.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id,
                                                   @RequestBody Customer customer) {

        Customer updatedCustomer = customerService.updateCustomer(id, customer)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with ID: " + id));

        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {

        boolean deleted = customerService.deleteCustomer(id);

        if (!deleted) {
            throw new ResourceNotFoundException("Customer not found with ID: " + id);
        }

        return ResponseEntity.noContent().build();
    }
}