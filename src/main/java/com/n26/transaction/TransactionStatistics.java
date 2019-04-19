package com.n26.transaction;

import java.math.BigDecimal;
import java.util.Objects;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.valueOf;

public class TransactionStatistics {
    private BigDecimal sum;
    private BigDecimal avg;
    private BigDecimal max;
    private BigDecimal min;
    private int count;

    static TransactionStatistics empty() {
        return new TransactionStatistics(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(Double.MIN_VALUE), BigDecimal.valueOf(Double.MAX_VALUE), 0);
    }

    public TransactionStatistics() {}

    public TransactionStatistics(BigDecimal sum, BigDecimal avg, BigDecimal max, BigDecimal min, int count) {
        this.sum = sum.setScale(2, ROUND_HALF_UP);
        this.avg = avg.setScale(2, ROUND_HALF_UP);
        this.max = max.setScale(2, ROUND_HALF_UP);
        this.min = min.setScale(2, ROUND_HALF_UP);
        this.count = count;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public BigDecimal getAvg() {
        return avg;
    }

    public BigDecimal getMax() {
        return max;
    }

    public BigDecimal getMin() {
        return min;
    }

    public int getCount() {
        return count;
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

    TransactionStatistics add(Transaction transaction) {
        BigDecimal newSum = sum.add(transaction.amount());
        BigDecimal newCount = new BigDecimal(count + 1);
        return new TransactionStatistics(
                newSum,
                newSum.divide(newCount, BigDecimal.ROUND_HALF_UP),
                max.max(transaction.amount()),
                min.min(transaction.amount()),
                newCount.intValue()
        );
    }

    void merge(TransactionStatistics transactionStatistics) {
        sum = sum.add(transactionStatistics.sum);
        max = max.max(transactionStatistics.max);
        min = min.min(transactionStatistics.min);
        count = count + transactionStatistics.count;
        avg = sum.divide(valueOf(count), BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public String toString() {
        return "TransactionStatistics{" +
                "sum=" + sum +
                ", avg=" + avg +
                ", max=" + max +
                ", min=" + min +
                ", count=" + count +
                '}';
    }
}
