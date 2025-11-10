package com.bankingtransactions.bankingendpoints.repository;

import com.bankingtransactions.bankingendpoints.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
