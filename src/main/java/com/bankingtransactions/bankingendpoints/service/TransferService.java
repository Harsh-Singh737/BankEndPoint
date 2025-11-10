package com.bankingtransactions.bankingendpoints.service;

import com.bankingtransactions.bankingendpoints.model.Transfer;
import com.bankingtransactions.bankingendpoints.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    public List<Transfer> getAllTransfers(PageRequest pageRequest) {
        return transferRepository.findAll(pageRequest).getContent();
    }

    public Optional<Transfer> getTransferById(Long id) {
        return transferRepository.findById(id);
    }

    public Transfer createTransfer(Transfer transfer) {
        return transferRepository.save(transfer);
    }

    public Optional<Transfer> updateTransfer(Long id, Transfer updatedTransfer) {
        return transferRepository.findById(id).map(existing -> {
            existing.setFromAccountId(updatedTransfer.getFromAccountId());
            existing.setToAccountId(updatedTransfer.getToAccountId());
            existing.setAmount(updatedTransfer.getAmount());
            existing.setRemarks(updatedTransfer.getRemarks());
            return transferRepository.save(existing);
        });
    }

    public boolean deleteTransfer(Long id) {
        if (transferRepository.existsById(id)) {
            transferRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

