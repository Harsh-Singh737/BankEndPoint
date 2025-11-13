package com.bankingtransactions.bankingendpoints.listener;

import com.bankingtransactions.bankingendpoints.model.Transaction;
import com.bankingtransactions.bankingendpoints.service.TransactionNotificationService;
import jakarta.persistence.PostPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionEntityListener {

    @Autowired
    private TransactionNotificationService notificationService;

    @PostPersist
    public void onTransactionCreated(Transaction txn) {
        notificationService.notifyCustomer(txn);
    }
}
