package com.n26.controllers;

import com.n26.transaction.RetrieveStatistics;
import org.springframework.http.ResponseEntity;

public class StatisticsController {
    private RetrieveStatistics retrieveStatistics;

    public StatisticsController(RetrieveStatistics retrieveStatistics) {
        this.retrieveStatistics = retrieveStatistics;
    }

    public ResponseEntity index() {
        return ResponseEntity.ok(retrieveStatistics.retrieve());
    }
}
