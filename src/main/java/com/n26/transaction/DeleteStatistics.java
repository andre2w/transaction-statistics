package com.n26.transaction;

public class DeleteStatistics {
    private TransactionAggregator transactionAggregator;

    public DeleteStatistics(TransactionAggregator transactionAggregator) {
        this.transactionAggregator = transactionAggregator;
    }

    public void execute() {
        transactionAggregator.clean();
    }
}
