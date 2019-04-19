package com.n26.controllers;

import com.n26.transaction.AddTransaction;
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
        addTransaction.execute(transaction);
        return ResponseEntity.status(201).build();
    }
}
