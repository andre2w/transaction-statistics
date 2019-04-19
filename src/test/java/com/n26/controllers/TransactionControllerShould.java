package com.n26.controllers;

import com.n26.transaction.AddTransaction;
import com.n26.transaction.InvalidTransactionTimestamp;
import com.n26.transaction.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TransactionControllerShould {

    private static final ZonedDateTime NOW = ZonedDateTime.now(ZoneId.of("UTC"));
    public static final ZonedDateTime TWO_MINUTES_AGO = NOW.minusMinutes(2);
    public static final int CREATED = 201;
    public static final int NO_CONTENT = 204;
    public static final int BAD_REQUEST = 400;
    private TransactionController transactionController;
    private AddTransaction addTransaction;

    @Before
    public void setUp() {
        addTransaction = mock(AddTransaction.class);
        transactionController = new TransactionController(addTransaction);
    }

    @Test
    public void return_response_with_code_201_when_transaction_is_added() {
        Transaction transaction = new Transaction(new BigDecimal("12.30"), NOW);

        ResponseEntity result = transactionController.create(transaction);

        verify(addTransaction).execute(transaction);
        assertEquals(ResponseEntity.status(CREATED).build(), result);
    }

    @Test
    public void return_response_with_code_204_when_transaction_is_older_than_60_seconds() {
        Transaction transaction = new Transaction(new BigDecimal("12.30"), TWO_MINUTES_AGO);
        doThrow(InvalidTransactionTimestamp.class).when(addTransaction).execute(transaction);

        ResponseEntity result = transactionController.create(transaction);

        assertEquals(ResponseEntity.status(NO_CONTENT).build(), result);
    }

    @Test
    public void return_response_with_code_400_when_transaction_has_invalid_field() {
        Transaction transactionWithoutAmount = new Transaction(null, TWO_MINUTES_AGO);
        assertEquals(ResponseEntity.status(BAD_REQUEST).build(), transactionController.create(transactionWithoutAmount));

        Transaction transactionWithoutTimestamp = new Transaction(new BigDecimal("12.30"), null);
        assertEquals(ResponseEntity.status(BAD_REQUEST).build(), transactionController.create(transactionWithoutTimestamp));
    }
}
