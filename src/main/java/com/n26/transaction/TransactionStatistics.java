package com.n26.transaction;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;
import java.util.Objects;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.valueOf;

public class TransactionStatistics {
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal sum;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal avg;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal max;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal min;

    private int count;

    public static TransactionStatistics from(Transaction transaction) {
        return new TransactionStatistics(
                transaction.amount(),
                transaction.amount(),
                transaction.amount(),
                transaction.amount(),
                1);
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

    public TransactionStatistics add(Transaction transaction) {
        BigDecimal newSum = sum.add(transaction.amount());
        BigDecimal newCount = BigDecimal.valueOf(count + 1);

        return new TransactionStatistics(
            newSum,
            newSum.divide(newCount, ROUND_HALF_UP),
            max.max(transaction.amount()),
            min.min(transaction.amount()),
            newCount.intValue());
    }

    TransactionStatistics merge(TransactionStatistics transactionStatistics) {
        int mergedCount = this.count + transactionStatistics.count;
        BigDecimal mergedSum = sum.add(transactionStatistics.sum);
        return new TransactionStatistics(
            mergedSum,
            mergedSum.divide(valueOf(mergedCount), BigDecimal.ROUND_HALF_UP),
            max.max(transactionStatistics.max),
            min.min(transactionStatistics.min),
            mergedCount
        );
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
