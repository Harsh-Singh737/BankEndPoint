package com.bankingtransactions.bankingendpoints.repository;

import com.bankingtransactions.bankingendpoints.model.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = :balance WHERE a.accountId = :accountId")
    void updateBalance(@Param("accountId") Long accountId, @Param("balance") Double balance);
}
