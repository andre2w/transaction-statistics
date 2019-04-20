package com.n26.transaction;

import com.n26.infrastructure.Clock;
import com.n26.infrastructure.TransactionStatisticsStore;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
class TransactionAggregator {

    private TransactionStatisticsStore transactionStatisticsStore;
    private Clock clock;

    TransactionAggregator(Clock clock, TransactionStatisticsStore transactionStatisticsStore ) {
        this.clock = clock;
        this.transactionStatisticsStore = transactionStatisticsStore;
    }

    void add(Transaction transaction) {
        transactionStatisticsStore.add(transaction);
    }

    void clear() {
        transactionStatisticsStore.clear();
    }


    TransactionStatistics statisticsOfLast(int seconds) {
        long startTime = clock.now().minusSeconds(seconds).toEpochSecond();

        List<TransactionStatistics> transactionStatistics = transactionStatisticsStore.statisticsFrom(startTime);

        return transactionStatistics.stream()
                .reduce(TransactionStatistics::merge)
                .orElseGet(this::emptyTransactionStatistics);
    }

    private TransactionStatistics emptyTransactionStatistics() {
        return new TransactionStatistics(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0);
    }
}
