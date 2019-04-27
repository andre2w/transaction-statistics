package com.n26.parsers;

import com.n26.dtos.TransactionData;
import com.n26.infrastructure.Clock;
import com.n26.transaction.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Component
public class TransactionParser {
    private Clock clock;

    public TransactionParser(Clock clock) {
        this.clock = clock;
    }

    public Optional<Transaction> parse(TransactionData transactionData) {
        BigDecimal amount;
        ZonedDateTime timestamp;

        try {
            amount = new BigDecimal(transactionData.amount());
        } catch (NumberFormatException err) {
            return Optional.empty();
        }

        try {
            timestamp = clock.parse(transactionData.timestamp());
        } catch (DateTimeParseException err) {
            return Optional.empty();
        }

        return Optional.of(new Transaction(amount, timestamp));
    }
}
