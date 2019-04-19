package com.n26.controllers;

import com.n26.transaction.RetrieveStatistics;
import com.n26.transaction.TransactionStatistics;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class StatisticsControllerShould {

    @Test
    public void return_statistics_from_transactions() {
        TransactionStatistics transactionStatistics =
                new TransactionStatistics(new BigDecimal("150.00"), new BigDecimal("75.00"), new BigDecimal("100.00"), new BigDecimal("50.00"), 2);
        RetrieveStatistics retrieveStatistics = mock(RetrieveStatistics.class);
        given(retrieveStatistics.retrieve()).willReturn(transactionStatistics);

        ResponseEntity result = new StatisticsController(retrieveStatistics).index();

        assertEquals(ResponseEntity.ok(transactionStatistics), result);
    }

}
