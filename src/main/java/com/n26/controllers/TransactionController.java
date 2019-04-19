package com.n26.controllers;

import com.n26.transaction.AddTransaction;
import com.n26.transaction.InvalidTransactionTimestamp;
import com.n26.transaction.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TransactionController {

    private AddTransaction addTransaction;

    TransactionController(AddTransaction addTransaction) {
        this.addTransaction = addTransaction;
    }

    @PostMapping
    ResponseEntity create(@RequestBody Transaction transaction) {

        try {
            addTransaction.execute(transaction);
        } catch (InvalidTransactionTimestamp err) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(201).build();
    }
}
