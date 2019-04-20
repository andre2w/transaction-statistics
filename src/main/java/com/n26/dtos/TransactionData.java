package com.n26.dtos;

public class TransactionData {
    private String amount;
    private final String timestamp;

    public TransactionData(String amount, String timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String amount() {
        return amount;
    }

    public String timestamp() {
        return timestamp;
    }

    public boolean hasInvalidField() {
        if (amount != null && timestamp != null) {
            return amount.isEmpty() || timestamp.isEmpty();
        }

        return true;
    }
}
