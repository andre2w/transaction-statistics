package com.n26.controllers;

import com.n26.transaction.AddTransaction;
import com.n26.transaction.InvalidTransactionTimestamp;
import com.n26.transaction.Transaction;
import com.n26.transaction.TransactionInTheFutureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
class TransactionController {

    private AddTransaction addTransaction;

    TransactionController(AddTransaction addTransaction) {
        this.addTransaction = addTransaction;
    }

    @PostMapping
    ResponseEntity create(@RequestBody Transaction transaction) {

        if (transaction.hasNullField()) {
            return buildResponse(BAD_REQUEST);
        }

        try {
            addTransaction.execute(transaction);
        } catch (InvalidTransactionTimestamp err) {
            return buildResponse(NO_CONTENT);
        } catch (TransactionInTheFutureException err) {
            return buildResponse(UNPROCESSABLE_ENTITY);
        }

        return buildResponse(CREATED);
    }

    private ResponseEntity<Object> buildResponse(HttpStatus statusCode) {
        return ResponseEntity.status(statusCode.value()).build();
    }

}
