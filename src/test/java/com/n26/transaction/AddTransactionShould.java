package com.n26.transaction;

import com.n26.infrastructure.Clock;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AddTransactionShould {

    private static final ZonedDateTime NOW = ZonedDateTime.now(ZoneId.of("UTC"));
    private TransactionAggregator transactionAggregator;
    private AddTransaction addTransaction;
    private Clock clock;

    @Before
    public void setUp() throws Exception {
        transactionAggregator = mock(TransactionAggregator.class);
        clock = mock(Clock.class);
        addTransaction = new AddTransaction(transactionAggregator, clock);
        given(clock.now()).willReturn(NOW);
    }

    @Test
    public void add_transaction_to_repository() {
        Transaction transaction = new Transaction(new BigDecimal("12.3343"), NOW);

        addTransaction.execute(transaction);

        verify(transactionAggregator).add(transaction);
    }

    @Test(expected = InvalidTransactionTimestamp.class)
    public void throw_error_case_transaction_is_older_than_60_seconds() {
        Transaction transaction = new Transaction(new BigDecimal("12.3343"), NOW.minusMinutes(2));

        addTransaction.execute(transaction);
    }
}