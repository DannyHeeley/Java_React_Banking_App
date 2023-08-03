package com.main.app.transactions;

import com.main.app.HandleDateTime;
import com.main.app.database.DatabaseService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Transactions implements HandleDateTime, DatabaseService {
    private static List<TransactionEntry> transactionHistory;
    public Transactions() {
        transactionHistory = new ArrayList<>();
    }
    public void addTransaction(TransactionType transactionType, float amount, int accountId) {
        TransactionEntry entry = new TransactionEntry(transactionType, amount, accountId);
        int relationalId = DatabaseService.updateDatabaseForTransaction(entry.getTransactionType(), entry.getTransactionValue(), LocalDate.now(), LocalTime.now());

        transactionHistory.add(entry);
    }
    private class TransactionEntry {
        private final TransactionType transactionType;
        private final float amount;
        private final LocalDateTime transactionDate;

        private int accountId;
        private TransactionEntry(TransactionType transactionType, float amount, int accountId) {
            this.transactionType = transactionType;
            this.amount = amount;
            this.transactionDate = getDateTimeNow();
            this.accountId = accountId;
        }
        private LocalDateTime getTransactionDateTime() {
            return transactionDate;
        }
        private Float getTransactionValue() {
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

