package com.n26.parsers;

import com.n26.dtos.TransactionData;
import com.n26.infrastructure.Clock;
import com.n26.transaction.Transaction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

public class TransactionParser {
    private Clock clock;

    public TransactionParser(Clock clock) {
        this.clock = clock;
    }

    public Optional<Transaction> parse(TransactionData transactionData) {
        BigDecimal amount = new BigDecimal(transactionData.amount());
        ZonedDateTime timestamp = clock.parse(transactionData.timestamp());
        return Optional.of(new Transaction(amount, timestamp));
    }
}
