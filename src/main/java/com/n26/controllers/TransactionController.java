package com.n26.controllers;

import com.n26.dtos.TransactionData;
import com.n26.parsers.TransactionParser;
import com.n26.transaction.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
class TransactionController {

    private TransactionService transactionService;
    private TransactionParser transactionParser;

    TransactionController(TransactionService transactionService, TransactionParser transactionParser) {
        this.transactionService = transactionService;
        this.transactionParser = transactionParser;
    }

    @PostMapping
    ResponseEntity create(@RequestBody TransactionData transactionData) {


        if (transactionData.hasInvalidField()) {
            return buildResponse(BAD_REQUEST);
        }

        Optional<Transaction> transaction = transactionParser.parse(transactionData);

        if (!transaction.isPresent())
            return buildResponse(UNPROCESSABLE_ENTITY);

        try {
            transactionService.add(transaction.get());
        } catch (TransactionTooOldException err) {
            return buildResponse(NO_CONTENT);
        } catch (UnprocessableTransactionException err) {
            return buildResponse(UNPROCESSABLE_ENTITY);
        }

        return buildResponse(CREATED);
    }

    @DeleteMapping
    ResponseEntity delete() {
        transactionService.deleteAll();
        return buildResponse(NO_CONTENT);
    }

    private ResponseEntity<Object> buildResponse(HttpStatus statusCode) {
        return ResponseEntity.status(statusCode.value()).build();
    }
}
