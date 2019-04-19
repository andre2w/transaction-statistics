package com.n26.transaction;

import com.n26.dtos.TransactionData;
import com.n26.infrastructure.Clock;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AddTransactionShould {

    private static final ZonedDateTime ZONED_DATE_TIME_NOW = ZonedDateTime.now(ZoneId.of("UTC"));
    private static final DateTimeFormatter ISO_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    private static final String NOW = ZONED_DATE_TIME_NOW.format(ISO_FORMAT);
    private static final String TWO_MINUTES_AGO = ZONED_DATE_TIME_NOW.minusMinutes(2).format(ISO_FORMAT);
    private static final String TOMORROW = ZONED_DATE_TIME_NOW.plusDays(1).format(ISO_FORMAT);;
    private TransactionAggregator transactionAggregator;
    private AddTransaction addTransaction;
    private Clock clock;

    @Before
    public void setUp() {
        transactionAggregator = mock(TransactionAggregator.class);
        clock = mock(Clock.class);
        addTransaction = new AddTransaction(transactionAggregator, clock);
        given(clock.now()).willReturn(ZONED_DATE_TIME_NOW);
    }

    @Test
    public void add_transaction_to_repository() {
        TransactionData transactionData = new TransactionData("12.3343", NOW);
        Transaction transaction = new Transaction(new BigDecimal("12.3343"), ZONED_DATE_TIME_NOW);

        addTransaction.execute(transactionData);

        verify(transactionAggregator).add(transaction);
    }

    @Test(expected = TransactionTooOldException.class)
    public void throw_error_case_transaction_is_older_than_60_seconds() {
        TransactionData transactionData = new TransactionData("12.3343", TWO_MINUTES_AGO);

        addTransaction.execute(transactionData);
    }

    @Test(expected = UnprocessableTransactionException.class)
    public void throw_error_case_transaction_happens_in_the_future() {
        TransactionData transactionData = new TransactionData("12.3343", TOMORROW);

        addTransaction.execute(transactionData);
    }
}