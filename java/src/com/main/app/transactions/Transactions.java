package com.main.app.transactions;

import com.main.app.accounts.AccountBase;
import com.main.app.database.DatabaseService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Transactions implements DatabaseService {
    private final List<TransactionEntry> transactionHistory;
    public Transactions() {
        this.transactionHistory = new ArrayList<>();
    }
    public void addTransaction(AccountBase account, TransactionType transactionType, float amount, int accountId) {
        TransactionEntry entry = new TransactionEntry(account, transactionType, amount, accountId);
        transactionHistory.add(entry);
    }
    private class TransactionEntry {
        private final TransactionType transactionType;
        private final float amount;
        private final LocalDate transactionDate;
        private final LocalTime transactionTime;
        private final int accountId;
        private final int transactionId;
        private TransactionEntry(AccountBase account, TransactionType transactionType, float amount, int accountId) {
            this.transactionType = transactionType;
            this.amount = amount;
            this.transactionDate = LocalDate.now();
            this.transactionTime = LocalTime.now();
            this.accountId = accountId;
            this.transactionId = DatabaseService.addTransactionEntryToDatabase(
                    account, transactionType, amount
            );
        }
        private LocalDate getTransactionDate() {
            return transactionDate;
        }
        private LocalTime getTransactionTime() {
            return transactionTime;
        }
        private Float getTransactionValue() {
            return amount;
        }
        private TransactionType getTransactionType() {
            return transactionType;
        }

        public int getTransactionId() {
            return transactionId;
        }

        public int getAccountId() {
            return accountId;
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction History:\n");
        for (TransactionEntry entry : transactionHistory) {
            sb.append("Date: ").append(parseDateToString((entry.getTransactionDate()))).append(", ");
            sb.append("Time: ").append(parseTimeToString((entry.getTransactionTime()))).append(", ");
            sb.append("Value: ").append(entry.getTransactionValue()).append(", ");
            sb.append("Type: ").append(entry.getTransactionType()).append(", ");
        }
        return sb.toString();
    }
    private String parseDateToString(LocalDate dateTimeNow) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTimeNow.format(formatter);
    }

    private String parseTimeToString(LocalTime dateTimeNow) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return dateTimeNow.format(formatter);
    }
}

