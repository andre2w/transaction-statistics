package com.n26.infrastructure;

import com.n26.transaction.Transaction;
import org.junit.Test;

import java.math.BigDecimal;

import static com.n26.fixtures.TimeFixtures.ZONED_DATE_TIME_NOW;
import static org.junit.Assert.assertEquals;

public class TransactionStatisticsStoreShould {

    private static final Transaction FIRST_TRANSACTION =
            new Transaction(new BigDecimal("100.00"), ZONED_DATE_TIME_NOW.minusSeconds(1));

    private static final Transaction SECOND_TRANSACTION =
            new Transaction(new BigDecimal("50.00"), ZONED_DATE_TIME_NOW.minusSeconds(1));

    @Test
    public void delete_transactions_before_timestamp() {
        TransactionStatisticsStore transactionsStatisticsStore = new TransactionStatisticsStore();

        transactionsStatisticsStore.add(FIRST_TRANSACTION);
        transactionsStatisticsStore.add(SECOND_TRANSACTION);

        transactionsStatisticsStore.deleteStatisticsBefore(ZONED_DATE_TIME_NOW.toEpochSecond());

        assertEquals(0, transactionsStatisticsStore.size());
    }

    @Test
    public void delete_transactions_in_the_exact_second() {
        TransactionStatisticsStore transactionsStatisticsStore = new TransactionStatisticsStore();

        transactionsStatisticsStore.add(FIRST_TRANSACTION);
        transactionsStatisticsStore.add(SECOND_TRANSACTION);

        long epochSecond = ZONED_DATE_TIME_NOW.minusSeconds(1).toEpochSecond();
        transactionsStatisticsStore.deleteStatisticsBefore(epochSecond);

        assertEquals(0, transactionsStatisticsStore.size());
    }
}