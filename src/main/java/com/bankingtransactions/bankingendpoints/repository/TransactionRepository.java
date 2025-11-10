package com.bankingtransactions.bankingendpoints.repository;

import com.bankingtransactions.bankingendpoints.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountCustomerCustomerIdAndTransactionDateBetween(
            Long customerId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
