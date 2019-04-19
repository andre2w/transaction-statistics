package com.n26.transaction;

import org.springframework.stereotype.Service;

@Service
public class RetrieveStatistics {
    private TransactionAggregator transactionAggregator;

    RetrieveStatistics(TransactionAggregator transactionAggregator) {
        this.transactionAggregator = transactionAggregator;
    }

    public TransactionStatistics retrieve() {
        return transactionAggregator.statisticsOfLast(60);
    }
}
