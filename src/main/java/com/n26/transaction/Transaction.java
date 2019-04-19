package com.n26.transaction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

class Transaction {
    private final BigDecimal amount;
    private final ZonedDateTime timestamp;

    Transaction(BigDecimal amount, ZonedDateTime timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    Long epochSeconds() {
        return timestamp.toEpochSecond();
    }

    BigDecimal amount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(amount, that.amount) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, timestamp);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}
