package com.n26.transaction;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RetrieveStatisticsShould {

    @Test
    public void return_statistics_for_last_sixty_seconds() {
        TransactionStatistics transactionStatistics =
                new TransactionStatistics(new BigDecimal("150.00"), new BigDecimal("75.00"), new BigDecimal("100.00"), new BigDecimal("50.00"), 2);
        TransactionAggregator transactionAggregator = mock(TransactionAggregator.class);
        when(transactionAggregator.statisticsOfLast(60)).thenReturn(transactionStatistics);

        TransactionStatistics result = new RetrieveStatistics(transactionAggregator).retrieve();

        assertEquals(transactionStatistics, result);
    }
}