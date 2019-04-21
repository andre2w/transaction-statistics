package com.n26.infrastructure;

import org.junit.Test;

import static com.n26.fixtures.TimeFixtures.ZONED_DATE_TIME_NOW;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CleanupSchedulerShould {

    @Test
    public void delete_transaction_statistics_older_than_seconds_to_live() {
        Clock clock = mock(Clock.class);
        int secondsToLive = 1;
        TransactionStatisticsStore transactionStatisticsStore = mock(TransactionStatisticsStore.class);
        CleanupScheduler cleanupScheduler = new CleanupScheduler(transactionStatisticsStore, clock, secondsToLive);
        given(clock.now()).willReturn(ZONED_DATE_TIME_NOW);

        cleanupScheduler.execute();

        long epochSecond = ZONED_DATE_TIME_NOW.minusSeconds(secondsToLive).toEpochSecond();
        verify(transactionStatisticsStore).deleteStatisticsBefore(epochSecond);
    }
}
