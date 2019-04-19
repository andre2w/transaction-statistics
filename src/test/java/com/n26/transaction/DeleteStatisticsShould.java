package com.n26.transaction;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DeleteStatisticsShould {

    @Test
    public void delete_stored_statistics_from_aggregator() {
        TransactionAggregator transactionAggregator = mock(TransactionAggregator.class);
        DeleteStatistics deleteStatistics = new DeleteStatistics(transactionAggregator);

        deleteStatistics.execute();

        verify(transactionAggregator).clear();
    }
}