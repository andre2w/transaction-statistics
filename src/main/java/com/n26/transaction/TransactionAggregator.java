package com.n26.transaction;

import org.springframework.stereotype.Service;

@Service
class TransactionAggregator {

    void add(Transaction transaction) {
    }

    public TransactionStatistics statisticsOfLast(int seconds) {
        throw new UnsupportedOperationException();
    }
}
