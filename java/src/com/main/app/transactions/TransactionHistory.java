package com.main.app.transactions;

import com.main.app.HandleDateTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionHistory implements HandleDateTime {
    private static List<TransactionEntry> transactionHistory;
    public TransactionHistory() {
        transactionHistory = new ArrayList<>();
    }
    public void addTransaction(TransactionType type, float value, float balance) {
        TransactionEntry entry = new TransactionEntry(type, value, balance);
        transactionHistory.add(entry);
    }
    private class TransactionEntry {
        private final LocalDateTime dateTime;
        private final float transactionValue;
        private final TransactionType type;
        private final Float balance;
        private TransactionEntry(TransactionType type, float value, float balance) {
            this.dateTime = getDateTimeNow();
            this.type = type;
            this.transactionValue = value;
            this.balance = balance;
        }
        private LocalDateTime getTransactionDateTime() {
            return dateTime;
        }
        private double getTransactionValue() {
            return transactionValue;
        }
        private TransactionType getTransactionType() {
            return type;
        }
        private double getTransactionBalance() {
            return balance;
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
            sb.append("Balance: ").append(entry.getTransactionBalance()).append("\n");
        }
        return sb.toString();
    }
}

