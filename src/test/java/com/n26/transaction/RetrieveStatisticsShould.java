package com.n26.transaction;

import org.junit.Test;

import static com.n26.builders.TransactionStatisticsBuilder.aTransactionStatistics;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class RetrieveStatisticsShould {

    public static final int SECONDS_TO_LIVE = 60;

    @Test
    public void return_statistics_for_last_sixty_seconds() {
        TransactionStatistics transactionStatistics = aTransactionStatistics().build();
        TransactionAggregator transactionAggregator = mock(TransactionAggregator.class);
        given(transactionAggregator.statisticsOfLast(SECONDS_TO_LIVE)).willReturn(transactionStatistics);

        TransactionStatistics result = new RetrieveStatistics(transactionAggregator, SECONDS_TO_LIVE).retrieve();

        assertEquals(transactionStatistics, result);
    }
}