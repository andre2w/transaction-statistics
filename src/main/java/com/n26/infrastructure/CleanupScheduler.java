package com.n26.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class CleanupScheduler {
    private final TransactionStatisticsStore transactionStatisticsStore;
    private final Clock clock;
    private final int secondsToLive;

    CleanupScheduler(TransactionStatisticsStore transactionStatisticsStore, Clock clock,
                     @Value("${secondsToLive}") int secondsToLive) {
        this.transactionStatisticsStore = transactionStatisticsStore;
        this.clock = clock;
        this.secondsToLive = secondsToLive;
    }

    @Scheduled(fixedRateString = "${secondsToLive}")
    void execute() {
        long epochSecond = clock.now().minusSeconds(secondsToLive).toEpochSecond();
        transactionStatisticsStore.deleteStatisticsBefore(epochSecond);
    }
}
