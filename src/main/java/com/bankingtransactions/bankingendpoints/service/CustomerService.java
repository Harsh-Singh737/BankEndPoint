package com.bankingtransactions.bankingendpoints.service;

import com.bankingtransactions.bankingendpoints.model.Customer;
import com.bankingtransactions.bankingendpoints.repository.CustomerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers(PageRequest pageRequest) {
        return customerRepository.findAll(pageRequest).getContent();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Optional<Customer> updateCustomer(Long id, Customer customerDetails) {
        return customerRepository.findById(id).map(customer -> {
            customer.setFirstName(customerDetails.getFirstName());
            customer.setLastName(customerDetails.getLastName());
            customer.setEmail(customerDetails.getEmail());
            customer.setPhone(customerDetails.getPhone());
            customer.setAddress(customerDetails.getAddress());
            return customerRepository.save(customer);
        });
    }

    public boolean deleteCustomer(Long id) {
        return customerRepository.findById(id).map(customer -> {
            customerRepository.delete(customer);
            return true;
        }).orElse(false);
    }
}


