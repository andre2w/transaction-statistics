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

    public void add(TransactionData transactionData) {
        Transaction transaction = parseTransactionData(transactionData);
        transactionAggregator.add(transaction);
    }

    public TransactionStatistics statistics() {
        return transactionAggregator.statisticsOfLast(secondsToLive);
    }

    public void deleteAll() {
        transactionAggregator.clear();
    }

    private Transaction parseTransactionData(TransactionData transactionData) {
        return new Transaction(parseAmount(transactionData.amount()), parseTimestamp(transactionData.timestamp()));
    }

    private BigDecimal parseAmount(String amount) {
        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException err) {
            throw new UnprocessableTransactionException();
        }
    }

    private ZonedDateTime parseTimestamp(String transactionTimestamp) {
        ZonedDateTime now = clock.now();
        ZonedDateTime timestamp;

        try {
            timestamp = ZonedDateTime.parse(transactionTimestamp, ISO_FORMAT);
        } catch (DateTimeParseException err) {
            throw new UnprocessableTransactionException();
        }

        if (isOlderThan(timestamp, now, secondsToLive)) {
            throw new TransactionTooOldException();
        }

        if (timestamp.isAfter(now)) {
            throw new UnprocessableTransactionException();
        }

        return timestamp;
    }

    private boolean isOlderThan(ZonedDateTime timestamp, ZonedDateTime now, int seconds) {
        return ChronoUnit.SECONDS.between(timestamp, now) >= seconds;
    }
}
