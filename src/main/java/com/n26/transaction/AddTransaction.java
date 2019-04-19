package com.n26.transaction;

import com.n26.infrastructure.Clock;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class AddTransaction {

    private TransactionAggregator transactionAggregator;
    private Clock clock;

    AddTransaction(TransactionAggregator transactionAggregator, Clock clock) {
        this.transactionAggregator = transactionAggregator;
        this.clock = clock;
    }

    public void execute(Transaction transaction) {
        validateTransaction(transaction);
        transactionAggregator.add(transaction);
    }

    private void validateTransaction(Transaction transaction) {
        ZonedDateTime now = clock.now();

        if (transaction.isOlderThan(now, 60)) {
            throw new InvalidTransactionTimestamp();
        }

        if (transaction.isAfter(now)) {
            throw new TransactionInTheFutureException();
        }
    }
}
