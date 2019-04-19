package com.n26.transaction;

import java.math.BigDecimal;
import java.util.Objects;

public class TransactionStatistics {
    private final BigDecimal sum;
    private final BigDecimal avg;
    private final BigDecimal max;
    private final BigDecimal min;
    private final int count;

    public TransactionStatistics(BigDecimal sum, BigDecimal avg, BigDecimal max, BigDecimal min, int count) {
        this.sum = sum;
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionStatistics that = (TransactionStatistics) o;
        return count == that.count &&
                Objects.equals(sum, that.sum) &&
                Objects.equals(avg, that.avg) &&
                Objects.equals(max, that.max) &&
                Objects.equals(min, that.min);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sum, avg, max, min, count);
    }
}
