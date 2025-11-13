package com.bankingtransactions.bankingendpoints.service;

import com.bankingtransactions.bankingendpoints.model.Account;
import com.bankingtransactions.bankingendpoints.model.Customer;
import com.bankingtransactions.bankingendpoints.model.Transaction;
import com.bankingtransactions.bankingendpoints.model.Transfer;
import com.bankingtransactions.bankingendpoints.repository.AccountRepository;
import com.bankingtransactions.bankingendpoints.repository.CustomerRepository;
import com.bankingtransactions.bankingendpoints.repository.TransactionRepository;
import com.bankingtransactions.bankingendpoints.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransferRepository transferRepository;

    public List<Account> getAllAccounts(PageRequest pageRequest) {
        return accountRepository.findAll(pageRequest).getContent();
    }

    public List<Customer> getAllCustomers(PageRequest pageRequest) {
        return customerRepository.findAll(pageRequest).getContent();
    }

    public List<Transaction> getAllTransactions(PageRequest pageRequest) {
        return transactionRepository.findAll(pageRequest).getContent();
    }

    public List<Transfer> getAllTransfers(PageRequest pageRequest) {
        return transferRepository.findAll(pageRequest).getContent();
    }


}
