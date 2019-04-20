package com.n26.transaction;

import com.n26.dtos.TransactionData;
import com.n26.infrastructure.Clock;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static com.n26.fixtures.TimeFixtures.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AddTransactionShould {

    private static final String TWELVE_EUROS_AND_THIRTY_CENTS = "12.3343";
    private static final int SECONDS_TO_LIVE = 60;
    private TransactionAggregator transactionAggregator;
    private AddTransaction addTransaction;

    @Before
    public void setUp() {
        transactionAggregator = mock(TransactionAggregator.class);
        Clock clock = mock(Clock.class);
        addTransaction = new AddTransaction(transactionAggregator, clock, SECONDS_TO_LIVE);
        given(clock.now()).willReturn(ZONED_DATE_TIME_NOW);
    }

    @Test
    public void add_transaction_to_repository() {
        TransactionData transactionData = new TransactionData(TWELVE_EUROS_AND_THIRTY_CENTS, NOW);
        Transaction transaction = new Transaction(new BigDecimal(TWELVE_EUROS_AND_THIRTY_CENTS), ZONED_DATE_TIME_NOW);

        addTransaction.execute(transactionData);

        verify(transactionAggregator).add(transaction);
    }

    @Test(expected = TransactionTooOldException.class)
    public void throw_error_case_transaction_is_older_than_60_seconds() {
        TransactionData transactionData = new TransactionData(TWELVE_EUROS_AND_THIRTY_CENTS, TWO_MINUTES_AGO);

        addTransaction.execute(transactionData);
    }

    @Test(expected = UnprocessableTransactionException.class)
    public void throw_error_case_transaction_happens_in_the_future() {
        TransactionData transactionData = new TransactionData(TWELVE_EUROS_AND_THIRTY_CENTS, TOMORROW);

        addTransaction.execute(transactionData);
    }

    @Test(expected = UnprocessableTransactionException.class)
    public void throw_error_case_transaction_amount_is_unparseable() {
        TransactionData transactionData = new TransactionData("ONE MILLION DOLLARS", NOW);
        addTransaction.execute(transactionData);
    }
}