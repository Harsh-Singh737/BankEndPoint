package com.bankingtransactions.bankingendpoints.controller;

import com.bankingtransactions.bankingendpoints.model.Transfer;
import com.bankingtransactions.bankingendpoints.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @GetMapping("/{id}")
    public ResponseEntity<Transfer> getTransferById(@PathVariable Long id) {
        return transferService.getTransferById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<Transfer> createTransfer(@RequestBody Transfer transfer) {
        Transfer created = transferService.createTransfer(transfer);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Transfer> updateTransfer(@PathVariable Long id, @RequestBody Transfer transfer) {
        return transferService.updateTransfer(id, transfer)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTransfer(@PathVariable Long id) {
        return transferService.deleteTransfer(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}

