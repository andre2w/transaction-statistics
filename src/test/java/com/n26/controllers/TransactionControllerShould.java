package com.n26.controllers;

import com.n26.transaction.AddTransaction;
import com.n26.transaction.Transaction;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TransactionControllerShould {

    private static final ZonedDateTime NOW = ZonedDateTime.now(ZoneId.of("UTC"));

    @Test
    public void return_response_with_code_201_when_transaction_is_added() {
        AddTransaction addTransaction = mock(AddTransaction.class);
        TransactionController transactionController = new TransactionController(addTransaction);
        Transaction transaction = new Transaction(new BigDecimal("12.30"), NOW);

        ResponseEntity result = transactionController.create(transaction);

        verify(addTransaction).execute(transaction);
        assertEquals(ResponseEntity.status(201).build(), result);
    }
}
