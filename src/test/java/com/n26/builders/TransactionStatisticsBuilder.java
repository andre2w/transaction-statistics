package com.n26.builders;

import com.n26.transaction.TransactionStatistics;

import java.math.BigDecimal;

public class TransactionStatisticsBuilder {
    private BigDecimal sum = new BigDecimal("0.00");
    private BigDecimal avg = new BigDecimal("0.00");
    private BigDecimal max = new BigDecimal("0.00");
    private BigDecimal min = new BigDecimal("0.00");
    private int count = 0;

    private TransactionStatisticsBuilder() {}

    public static TransactionStatisticsBuilder aTransactionStatistics() {
        return new TransactionStatisticsBuilder();
    }

    public TransactionStatisticsBuilder withSum(BigDecimal sum) {
        this.sum = sum;
        return this;
    }

    public TransactionStatisticsBuilder withAvg(BigDecimal avg) {
        this.avg = avg;
        return this;
    }

    public TransactionStatisticsBuilder withMax(BigDecimal max) {
        this.max = max;
        return this;
    }

    public TransactionStatisticsBuilder withMin(BigDecimal min) {
        this.min = min;
        return this;
    }

    public TransactionStatisticsBuilder withCount(int count) {
        this.count = count;
        return this;
    }

    public TransactionStatistics build() {
        return new TransactionStatistics(sum, avg, max, min, count);
    }
}