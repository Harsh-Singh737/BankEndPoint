package com.bankingtransactions.bankingendpoints.service;

import com.bankingtransactions.bankingendpoints.model.Customer;
import com.bankingtransactions.bankingendpoints.repository.CustomerRepository;
import org.springframework.stereotype.Service;


import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmailService emailService;

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer createCustomer(Customer customer) {

        Customer savedCustomer = customerRepository.save(customer);

        String fullName = savedCustomer.getFirstName() + " " + savedCustomer.getLastName();

        // ✅ Send welcome email
        emailService.sendEmail(
                savedCustomer.getEmail(),
                "Welcome to Harsh Bank!",
                "Dear " + fullName + ",\n\n" +
                        "Thank you for registering with Harsh Bank.\n" +
                        "Your profile has been successfully created.\n\n" +
                        "We’re excited to have you with us!\n\n" +
                        "Warm regards,\nHarsh Bank Support"
        );
        return savedCustomer;
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

            String fullName = customer.getFirstName() + " " + customer.getLastName();

            // ✅ Send account deletion email
            emailService.sendEmail(
                    customer.getEmail(),
                    "Goodbye from Harsh Bank",
                    "Dear " + fullName + ",\n\n" +
                            "Your profile has been deleted from Harsh Bank.\n" +
                            "We’re sorry to see you go. If you ever wish to return, you’re always welcome.\n\n" +
                            "Thank you for being part of our banking family.\n\n" +
                            "Best regards,\nHarsh Bank Support"
            );
            return true;
        }).orElse(false);
    }
}


