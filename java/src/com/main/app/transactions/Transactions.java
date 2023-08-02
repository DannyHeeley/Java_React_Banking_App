package com.main.app.transactions;

import com.main.app.HandleDateTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transactions implements HandleDateTime {
    private static List<TransactionEntry> transactionHistory;
    public Transactions() {
        transactionHistory = new ArrayList<>();
    }
    public void addTransaction(TransactionType type, float value) {
        TransactionEntry entry = new TransactionEntry(type, value);
        transactionHistory.add(entry);
    }
    private class TransactionEntry {
        private final TransactionType transactionType;
        private final float amount;
        private final LocalDateTime transactionDate;
        private TransactionEntry(TransactionType transactionType, float amount) {
            this.transactionType = transactionType;
            this.amount = amount;
            this.transactionDate = getDateTimeNow();
        }
        private LocalDateTime getTransactionDateTime() {
            return transactionDate;
        }
        private double getTransactionValue() {
            return amount;
        }
        private TransactionType getTransactionType() {
            return transactionType;
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction History:\n");
        for (TransactionEntry entry : transactionHistory) {
            sb.append("Date/Time: ").append(parseDateTimeToString((entry.getTransactionDateTime()))).append(", ");
            sb.append("Value: ").append(entry.getTransactionValue()).append(", ");
            sb.append("Type: ").append(entry.getTransactionType()).append(", ");
        }
        return sb.toString();
    }
}

