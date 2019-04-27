package com.n26.transaction;

import com.n26.dtos.TransactionData;
import com.n26.infrastructure.Clock;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static com.n26.builders.TransactionStatisticsBuilder.aTransactionStatistics;
import static com.n26.fixtures.TimeFixtures.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TransactionServiceShould {

    private static final String TWELVE_EUROS_AND_THIRTY_CENTS = "12.3343";
    private static final int SECONDS_TO_LIVE = 60;
    private TransactionService transactionService;
    private TransactionAggregator transactionAggregator;

    @Before
    public void setUp() {
        transactionAggregator = mock(TransactionAggregator.class);
        Clock clock = mock(Clock.class);
        transactionService = new TransactionService(transactionAggregator, clock, SECONDS_TO_LIVE);
        given(clock.now()).willReturn(ZONED_DATE_TIME_NOW);
    }

    @Test
    public void add_transaction_to_repository() {
        Transaction transaction = new Transaction(new BigDecimal(TWELVE_EUROS_AND_THIRTY_CENTS), ZONED_DATE_TIME_NOW);

        transactionService.add(transaction);

        verify(transactionAggregator).add(transaction);
    }

    @Test(expected = TransactionTooOldException.class)
    public void throw_error_case_transaction_is_older_than_60_seconds() {
        Transaction transaction = new Transaction(new BigDecimal(TWELVE_EUROS_AND_THIRTY_CENTS), ZONED_DATE_TIME_NOW.minusSeconds(60));

        transactionService.add(transaction);
    }

    @Test(expected = UnprocessableTransactionException.class)
    public void throw_error_case_transaction_happens_in_the_future() {
        Transaction transaction = new Transaction(new BigDecimal(TWELVE_EUROS_AND_THIRTY_CENTS), ZONED_DATE_TIME_NOW.plusSeconds(10));

        transactionService.add(transaction);
    }

    @Test
    public void delete_stored_statistics_from_aggregator() {
        transactionService.deleteAll();

        verify(transactionAggregator).clear();
    }

    @Test
    public void return_statistics_for_last_sixty_seconds() {
        TransactionStatistics transactionStatistics = aTransactionStatistics().build();
        given(transactionAggregator.statisticsOfLast(SECONDS_TO_LIVE)).willReturn(transactionStatistics);

        TransactionStatistics result = transactionService.statistics();

        assertEquals(transactionStatistics, result);
    }
}