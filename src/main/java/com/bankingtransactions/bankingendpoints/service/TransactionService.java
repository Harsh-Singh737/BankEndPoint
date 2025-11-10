package com.bankingtransactions.bankingendpoints.service;

import com.bankingtransactions.bankingendpoints.model.Account;
import com.bankingtransactions.bankingendpoints.model.Transaction;
import com.bankingtransactions.bankingendpoints.repository.AccountRepository;
import com.bankingtransactions.bankingendpoints.repository.TransactionRepository;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<Transaction> getAllTransactions(PageRequest pageRequest) {
        return transactionRepository.findAll(pageRequest).getContent();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public Optional<Transaction> createTransaction(Long accountId, Transaction transaction) {
        return accountRepository.findById(accountId).map(account -> {
            transaction.setAccount(account);
            transaction.setTransactionDate(LocalDateTime.now());
            return transactionRepository.save(transaction);
        });
    }

    public Optional<Transaction> updateTransaction(Long id, Transaction transactionDetails) {
        return transactionRepository.findById(id).map(transaction -> {
            transaction.setTransactionType(transactionDetails.getTransactionType());
            transaction.setAmount(transactionDetails.getAmount());
            transaction.setDescription(transactionDetails.getDescription());
            return transactionRepository.save(transaction);
        });
    }

    public boolean deleteTransaction(Long id) {
        return transactionRepository.findById(id).map(transaction -> {
            transactionRepository.delete(transaction);
            return true;
        }).orElse(false);
    }
}
