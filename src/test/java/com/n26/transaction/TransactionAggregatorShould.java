package com.n26.transaction;

import com.n26.infrastructure.Clock;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class TransactionAggregatorShould {

    private static final ZonedDateTime NOW = ZonedDateTime.now(ZoneId.of("UTC"));
    private static final Transaction FIRST_TRANSACTION = new Transaction(new BigDecimal("100.00"), NOW);
    private static final Transaction SECOND_TRANSACTION = new Transaction(new BigDecimal("50.00"), NOW);

    @Test
    public void aggregate_transaction_by_second() {
        Clock clock = mock(Clock.class);
        TransactionAggregator transactionAggregator = new TransactionAggregator(clock);
        given(clock.now()).willReturn(NOW);
        transactionAggregator.add(FIRST_TRANSACTION);
        transactionAggregator.add(SECOND_TRANSACTION);

        TransactionStatistics result = transactionAggregator.statisticsOfLast(1);

        TransactionStatistics transactionStatistics =
                new TransactionStatistics(new BigDecimal("150.00"), new BigDecimal("75.00"), new BigDecimal("100.00"), new BigDecimal("50.00"), 2);
        assertEquals(transactionStatistics, result);
    }

}