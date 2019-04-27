package com.n26.parsers;

import com.n26.dtos.TransactionData;
import com.n26.infrastructure.Clock;
import com.n26.transaction.Transaction;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static com.n26.fixtures.TimeFixtures.NOW;
import static com.n26.fixtures.TimeFixtures.ZONED_DATE_TIME_NOW;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class TransactionParserShould {

    @Test
    public void parse_transaction_data_into_transaction_object() {
        TransactionData transactionData = new TransactionData("12.500", NOW);
        Clock clock = mock(Clock.class);
        given(clock.parse(NOW)).willReturn(ZONED_DATE_TIME_NOW);
        TransactionParser transactionParser = new TransactionParser(clock);

        Optional<Transaction> result = transactionParser.parse(transactionData);

        Transaction transaction = new Transaction(new BigDecimal("12.500"), ZONED_DATE_TIME_NOW);
        assertEquals(transaction, result.get());
    }

    @Test
    public void return_empty_optional_when_fails_to_parse_amount() {
        TransactionData transactionData = new TransactionData("", NOW);
        Clock clock = mock(Clock.class);
        given(clock.parse(NOW)).willReturn(ZONED_DATE_TIME_NOW);
        TransactionParser transactionParser = new TransactionParser(clock);

        Optional<Transaction> result = transactionParser.parse(transactionData);

        assertFalse(result.isPresent());
    }
}
