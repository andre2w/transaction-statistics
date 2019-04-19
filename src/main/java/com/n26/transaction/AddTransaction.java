package com.n26.transaction;

import com.n26.infrastructure.Clock;
import org.springframework.stereotype.Service;

@Service
public class AddTransaction {

    private TransactionAggregator transactionAggregator;
    private Clock clock;

    AddTransaction(TransactionAggregator transactionAggregator, Clock clock) {
        this.transactionAggregator = transactionAggregator;
        this.clock = clock;
    }

    public void execute(Transaction transaction) {
        if (transaction.isInvalid(clock.now())) {
            throw new InvalidTransactionTimestamp();
        }

        transactionAggregator.add(transaction);
    }
}
