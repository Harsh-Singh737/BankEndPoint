package com.bankingtransactions.bankingendpoints.repository;

import com.bankingtransactions.bankingendpoints.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
}

