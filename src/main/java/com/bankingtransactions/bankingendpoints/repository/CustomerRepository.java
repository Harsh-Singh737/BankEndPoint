package com.bankingtransactions.bankingendpoints.repository;

import com.bankingtransactions.bankingendpoints.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String email);

}
