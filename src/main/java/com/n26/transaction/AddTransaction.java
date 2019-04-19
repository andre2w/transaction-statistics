package com.n26.transaction;

import org.springframework.stereotype.Service;

@Service
public class AddTransaction {

    private TransactionAggregator transactionAggregator;

    public AddTransaction(TransactionAggregator transactionAggregator) {
        this.transactionAggregator = transactionAggregator;
    }

    public void execute(Transaction transaction) {
        transactionAggregator.add(transaction);
    }
}
