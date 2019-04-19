package com.n26.transaction;

import com.n26.infrastructure.Clock;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

@Service
class TransactionAggregator {

    private Map<Long, TransactionStatistics> statisticsBySecond = new ConcurrentHashMap<>();
    private Clock clock;

    TransactionAggregator(Clock clock) {
        this.clock = clock;
    }

    void add(Transaction transaction) {
        if (!hasStatisticsForTimestamp(transaction.epochSeconds())) {
            addEmptyTransaction(transaction.epochSeconds());
        }

        statisticsBySecond.computeIfPresent(transaction.epochSeconds(),
                (epochSecond, transactionStatistics) -> transactionStatistics.add(transaction));
    }

    void clean() {
        throw new UnsupportedOperationException();
    }

    private void addEmptyTransaction(long epochSecond) {
        statisticsBySecond.put(epochSecond, TransactionStatistics.empty());
    }

    private boolean hasStatisticsForTimestamp(Long epochSecond) {
        return statisticsBySecond.containsKey(epochSecond);
    }

    TransactionStatistics statisticsOfLast(int seconds) {
        ZonedDateTime now = clock.now();
        TransactionStatistics transactionStatistics = TransactionStatistics.empty();

        IntStream.range(0, seconds).forEach(
                second -> mergeTransactions(transactionStatistics, epochSecondAt(now, second)));

        return transactionStatistics;
    }

    private void mergeTransactions(TransactionStatistics transactionStatistics, long epochSecond) {
        if (hasStatisticsForTimestamp(epochSecond)) {
            transactionStatistics.merge(statisticsBySecond.get(epochSecond));
        }
    }

    private long epochSecondAt(ZonedDateTime now, int second) {
        return now.minusSeconds(second).toEpochSecond();
    }
}
