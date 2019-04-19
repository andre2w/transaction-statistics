package com.n26.transaction;

import com.n26.infrastructure.Clock;
import org.junit.Before;
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
    private Clock clock;
    private TransactionAggregator transactionAggregator;
    private static final TransactionStatistics TRANSACTION_STATISTICS =
        new TransactionStatistics(new BigDecimal("150.00"), new BigDecimal("75.00"), new BigDecimal("100.00"), new BigDecimal("50.00"), 2);

    @Before
    public void setUp() throws Exception {
        clock = mock(Clock.class);
        transactionAggregator = new TransactionAggregator(clock);
    }

    @Test
    public void aggregate_transaction_by_second() {
        given(clock.now()).willReturn(NOW);
        transactionAggregator.add(FIRST_TRANSACTION);
        transactionAggregator.add(SECOND_TRANSACTION);

        TransactionStatistics result = transactionAggregator.statisticsOfLast(1);

        assertEquals(TRANSACTION_STATISTICS, result);
    }

    @Test
    public void aggregate_transactions_from_multiple_seconds() {
        given(clock.now()).willReturn(NOW);
        Transaction transactionTwoSecondsAgo = new Transaction(new BigDecimal("50.00"), NOW.minusSeconds(2));
        transactionAggregator.add(FIRST_TRANSACTION);
        transactionAggregator.add(transactionTwoSecondsAgo);

        TransactionStatistics result = transactionAggregator.statisticsOfLast(5);

        assertEquals(TRANSACTION_STATISTICS, result);
    }

    @Test
    public void skip_transactions_from_time_before() {
        given(clock.now()).willReturn(NOW);
        TransactionStatistics transactionStatistics =
                new TransactionStatistics(new BigDecimal("100.00"), new BigDecimal("100.00"), new BigDecimal("100.00"), new BigDecimal("100.00"), 1);
        Transaction transactionTenSecondsAgo = new Transaction(new BigDecimal("50.00"), NOW.minusSeconds(10));
        transactionAggregator.add(FIRST_TRANSACTION);
        transactionAggregator.add(transactionTenSecondsAgo);

        TransactionStatistics result = transactionAggregator.statisticsOfLast(5);

        assertEquals(transactionStatistics, result);
    }

    @Test
    public void clean_stored_transaction_statistics() {
        given(clock.now()).willReturn(NOW);
        transactionAggregator.add(FIRST_TRANSACTION);
        transactionAggregator.add(SECOND_TRANSACTION);

        transactionAggregator.clear();

        assertEquals(TransactionStatistics.empty(), transactionAggregator.statisticsOfLast(60));
    }
}