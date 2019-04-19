package com.n26.transaction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class Transaction {
    private final BigDecimal amount;
    private final ZonedDateTime timestamp;

    public Transaction(BigDecimal amount, ZonedDateTime timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    boolean isInvalid(ZonedDateTime now) {
        return ChronoUnit.SECONDS.between(timestamp, now) > 60;
    }

    public BigDecimal amount() {
        return amount;
    }

    public ZonedDateTime timestamp() {
        return timestamp;
    }

    public boolean hasNullField() {
        return amount() == null || timestamp() == null;
    }
}
