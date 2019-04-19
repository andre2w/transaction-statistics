package com.n26.transaction;

import com.n26.infrastructure.Clock;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
class TransactionAggregator {

    private Map<Long, TransactionStatistics> statisticsBySecond = new ConcurrentHashMap<>();
    private Clock clock;

    public TransactionAggregator(Clock clock) {
        this.clock = clock;
    }

    void add(Transaction transaction) {
        if (!statisticsBySecond.containsKey(transaction.epochSeconds())) {
            statisticsBySecond.put(transaction.epochSeconds(), TransactionStatistics.empty());
        }

        statisticsBySecond.computeIfPresent(transaction.epochSeconds(),
                (aLong, transactionStatistics) -> transactionStatistics.add(transaction));
    }

    public TransactionStatistics statisticsOfLast(int seconds) {
        ZonedDateTime now = clock.now();
        TransactionStatistics transactionStatistics = TransactionStatistics.empty();
        for (int i = 0; i < seconds; i++) {
            long epochSecond = now.minusSeconds(i).toEpochSecond();
            if (statisticsBySecond.containsKey(epochSecond)) {
                transactionStatistics.merge(statisticsBySecond.get(epochSecond));
            }
        }

        return transactionStatistics;
    }
}
