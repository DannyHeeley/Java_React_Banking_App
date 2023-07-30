package com.main.app.transactions;

import com.main.app.HandleDateTime;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistory implements HandleDateTime {
    private static List<TransactionEntry> transactionHistory;

    public TransactionHistory() {
        transactionHistory = new ArrayList<>();
    }

    public static void addTransaction(TransactionType type, float value, float balance) {
        TransactionEntry entry = new TransactionEntry(type, value, balance);
        transactionHistory.add(entry);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction History:\n");
        for (TransactionEntry entry : transactionHistory) {
            sb.append("Date/Time: ").append(parseDateTimeToString((entry.getTransactionDateTime()))).append(", ");
            sb.append("Value: ").append(entry.getTransactionValue()).append(", ");
            sb.append("Type: ").append(entry.getType()).append(", ");
            sb.append("Balance: ").append(entry.getBalance()).append("\n");
        }
        return sb.toString();
    }
}

