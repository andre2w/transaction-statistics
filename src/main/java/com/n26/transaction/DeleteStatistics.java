package com.n26.transaction;

import org.springframework.stereotype.Service;

@Service
public class DeleteStatistics {
    private TransactionAggregator transactionAggregator;

    DeleteStatistics(TransactionAggregator transactionAggregator) {
        this.transactionAggregator = transactionAggregator;
    }

    public void execute() {
        transactionAggregator.clear();
    }
}
