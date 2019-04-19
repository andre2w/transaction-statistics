package com.n26.transaction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class Transaction {
    private final BigDecimal amount;
    private final ZonedDateTime timestamp;

    public Transaction(BigDecimal amount, ZonedDateTime timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public ZonedDateTime timestamp() {
        return timestamp;
    }

    public boolean hasNullField() {
        return amount == null || timestamp == null;
    }
}
