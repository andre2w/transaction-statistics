package com.n26.infrastructure;

import com.n26.transaction.Transaction;
import com.n26.transaction.TransactionStatistics;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;

@Component
public class TransactionStatisticsStore {
    private Map<Long, TransactionStatistics> statisticsBySecond;

    public TransactionStatisticsStore() {
        this.statisticsBySecond = new ConcurrentHashMap<>();
    }

    public void add(Transaction transaction) {
        statisticsBySecond.compute(transaction.epochSeconds(),
                (epochSecond, transactionStatistics) -> addTransaction(transaction, transactionStatistics));
    }

    public void clear() {
        statisticsBySecond.clear();
    }

    public List<TransactionStatistics> statisticsFrom(long startSecond) {
        return statisticsBySecond.entrySet().stream()
                .filter(isNewThan(startSecond))
                .map(Map.Entry::getValue)
                .collect(toList());
    }

    public int size() {
        return statisticsBySecond.size();
    }

    private TransactionStatistics addTransaction(Transaction transaction, TransactionStatistics transactionStatistics) {
        if (transactionStatistics == null) {
            return TransactionStatistics.from(transaction);
        }
        return transactionStatistics.add(transaction);
    }

    private Predicate<Map.Entry<Long, TransactionStatistics>> isNewThan(long startSecond) {
        return entry -> entry.getKey() > startSecond;
    }
}
