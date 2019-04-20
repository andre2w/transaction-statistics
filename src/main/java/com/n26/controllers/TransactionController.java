package com.n26.controllers;

import com.n26.dtos.TransactionData;
import com.n26.transaction.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
class TransactionController {

    private AddTransaction addTransaction;
    private DeleteStatistics deleteStatistics;

    TransactionController(AddTransaction addTransaction, DeleteStatistics deleteStatistics) {
        this.addTransaction = addTransaction;
        this.deleteStatistics = deleteStatistics;
    }

    @PostMapping
    ResponseEntity create(@RequestBody TransactionData transaction) {

        if (transaction.hasEmptyField()) {
            return buildResponse(BAD_REQUEST);
        }

        try {
            addTransaction.execute(transaction);
        } catch (TransactionTooOldException err) {
            return buildResponse(NO_CONTENT);
        } catch (UnprocessableTransactionException err) {
            return buildResponse(UNPROCESSABLE_ENTITY);
        }

        return buildResponse(CREATED);
    }

    @DeleteMapping
    ResponseEntity delete() {
        deleteStatistics.execute();
        return buildResponse(NO_CONTENT);
    }

    private ResponseEntity<Object> buildResponse(HttpStatus statusCode) {
        return ResponseEntity.status(statusCode.value()).build();
    }
}
