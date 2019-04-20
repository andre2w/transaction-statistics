package com.n26.transaction;

import com.n26.infrastructure.Clock;
import com.n26.infrastructure.TransactionStatisticsStore;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.n26.builders.TransactionStatisticsBuilder.aTransactionStatistics;
import static com.n26.fixtures.TimeFixtures.ZONED_DATE_TIME_NOW;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class TransactionAggregatorShould {

    private static final Transaction FIRST_TRANSACTION = new Transaction(new BigDecimal("100.00"), ZONED_DATE_TIME_NOW);
    private static final Transaction SECOND_TRANSACTION = new Transaction(new BigDecimal("50.00"), ZONED_DATE_TIME_NOW);
    private static final ZonedDateTime TWO_SECONDS_AGO = ZONED_DATE_TIME_NOW.minusSeconds(2);
    private static final ZonedDateTime TEN_SECONDS_AGO = ZONED_DATE_TIME_NOW.minusSeconds(10);

    private Clock clock;
    private TransactionAggregator transactionAggregator;

    private static final TransactionStatistics TRANSACTION_STATISTICS =
            aTransactionStatistics()
                    .withSum(new BigDecimal("150.00"))
                    .withAvg(new BigDecimal("75.00"))
                    .withMax(new BigDecimal("100.00"))
                    .withMin(new BigDecimal("50.00"))
                    .withCount(2)
                    .build();

    private static final TransactionStatistics transactionStatistics =
            aTransactionStatistics()
                    .withSum(new BigDecimal("100.00"))
                    .withAvg(new BigDecimal("100.00"))
                    .withMax(new BigDecimal("100.00"))
                    .withMin(new BigDecimal("100.00"))
                    .withCount(1)
                    .build();

    private static final TransactionStatistics emptyTransactionStatistics = aTransactionStatistics().build();


    @Before
    public void setUp() {
        clock = mock(Clock.class);
        TransactionStatisticsStore transactionStatisticsStore = new TransactionStatisticsStore();
        transactionAggregator = new TransactionAggregator(clock, transactionStatisticsStore);
    }

    @Test
    public void aggregate_transaction_by_second() {
        given(clock.now()).willReturn(ZONED_DATE_TIME_NOW);
        transactionAggregator.add(FIRST_TRANSACTION);
        transactionAggregator.add(SECOND_TRANSACTION);

        TransactionStatistics result = transactionAggregator.statisticsOfLast(1);

        assertEquals(TRANSACTION_STATISTICS, result);
    }

    @Test
    public void aggregate_transactions_from_multiple_seconds() {
        given(clock.now()).willReturn(ZONED_DATE_TIME_NOW);
        Transaction transactionTwoSecondsAgo = new Transaction(new BigDecimal("50.00"), TWO_SECONDS_AGO);
        transactionAggregator.add(FIRST_TRANSACTION);
        transactionAggregator.add(transactionTwoSecondsAgo);

        TransactionStatistics result = transactionAggregator.statisticsOfLast(5);

        assertEquals(TRANSACTION_STATISTICS, result);
    }

    @Test
    public void skip_transactions_from_time_before() {
        given(clock.now()).willReturn(ZONED_DATE_TIME_NOW);

        Transaction transactionTenSecondsAgo = new Transaction(new BigDecimal("50.00"), TEN_SECONDS_AGO);
        transactionAggregator.add(FIRST_TRANSACTION);
        transactionAggregator.add(transactionTenSecondsAgo);

        TransactionStatistics result = transactionAggregator.statisticsOfLast(5);

        assertEquals(transactionStatistics, result);
    }

    @Test
    public void clean_stored_transaction_statistics() {
        given(clock.now()).willReturn(ZONED_DATE_TIME_NOW);
        transactionAggregator.add(FIRST_TRANSACTION);
        transactionAggregator.add(SECOND_TRANSACTION);

        transactionAggregator.clear();


        assertEquals(emptyTransactionStatistics, transactionAggregator.statisticsOfLast(60));
    }
}