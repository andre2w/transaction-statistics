package com.n26.transaction;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RetrieveStatistics {
    private TransactionAggregator transactionAggregator;
    private int secondsToLive;

    RetrieveStatistics(TransactionAggregator transactionAggregator,
                       @Value("${secondsToLive}") int secondsToLive) {
        this.transactionAggregator = transactionAggregator;
        this.secondsToLive = secondsToLive;
    }

    public TransactionStatistics retrieve() {
        return transactionAggregator.statisticsOfLast(secondsToLive);
    }
}
