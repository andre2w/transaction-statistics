package com.n26.transaction;

import com.n26.dtos.TransactionData;
import com.n26.infrastructure.Clock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import static com.n26.infrastructure.Clock.ISO_FORMAT;

@Service
public class TransactionService {
    private TransactionAggregator transactionAggregator;
    private Clock clock;
    private int secondsToLive;

    public TransactionService(TransactionAggregator transactionAggregator, Clock clock,
                              @Value("${secondsToLive}") int secondsToLive) {
        this.transactionAggregator = transactionAggregator;
        this.clock = clock;
        this.secondsToLive = secondsToLive;
    }

    public void add(Transaction transaction) {
        validateTimestamp(transaction.timestamp());
        transactionAggregator.add(transaction);
    }

    public TransactionStatistics statistics() {
        return transactionAggregator.statisticsOfLast(secondsToLive);
    }

    public void deleteAll() {
        transactionAggregator.clear();
    }


    private void validateTimestamp(ZonedDateTime timestamp) {
        ZonedDateTime now = clock.now();

        if (isOlderThan(timestamp, now, secondsToLive)) {
            throw new TransactionTooOldException();
        }

        if (timestamp.isAfter(now)) {
            throw new UnprocessableTransactionException();
        }
    }

    private boolean isOlderThan(ZonedDateTime timestamp, ZonedDateTime now, int seconds) {
        return ChronoUnit.SECONDS.between(timestamp, now) >= seconds;
    }
}
