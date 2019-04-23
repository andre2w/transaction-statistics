package com.n26.controllers;

import com.n26.transaction.TransactionService;
import com.n26.transaction.TransactionStatistics;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static com.n26.builders.TransactionStatisticsBuilder.aTransactionStatistics;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class StatisticsControllerShould {

    @Test
    public void return_statistics_from_transactions() {
        TransactionStatistics transactionStatistics = aTransactionStatistics().build();
        TransactionService transactionService = mock(TransactionService.class);
        given(transactionService.statistics()).willReturn(transactionStatistics);

        ResponseEntity result = new StatisticsController(transactionService).index();

        assertEquals(ResponseEntity.ok(transactionStatistics), result);
    }
}
