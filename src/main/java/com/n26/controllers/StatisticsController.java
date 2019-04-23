package com.n26.controllers;

import com.n26.transaction.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class StatisticsController {
    private TransactionService transactionService;

    StatisticsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    ResponseEntity index() {
        return ResponseEntity.ok(transactionService.statistics());
    }
}
