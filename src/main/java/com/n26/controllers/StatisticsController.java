package com.n26.controllers;

import com.n26.transaction.RetrieveStatistics;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class StatisticsController {
    private RetrieveStatistics retrieveStatistics;

    StatisticsController(RetrieveStatistics retrieveStatistics) {
        this.retrieveStatistics = retrieveStatistics;
    }

    @GetMapping
    ResponseEntity index() {
        return ResponseEntity.ok(retrieveStatistics.retrieve());
    }
}
