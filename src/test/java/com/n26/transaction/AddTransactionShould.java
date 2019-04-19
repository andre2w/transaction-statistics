package com.n26.transaction;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AddTransactionShould {

    private static final ZonedDateTime NOW = ZonedDateTime.now(ZoneId.of("UTC"));


    @Test
    public void add_transaction_to_repository() {
        TransactionAggregator transactionAggregator = mock(TransactionAggregator.class);
        AddTransaction addTransaction = new AddTransaction(transactionAggregator);
        Transaction transaction = new Transaction(new BigDecimal("12.3343"), NOW);

        addTransaction.execute(transaction);

        verify(transactionAggregator).add(transaction);
    }
}