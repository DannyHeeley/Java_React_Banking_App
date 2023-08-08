package com.main.app.transactions;

import com.main.app.accounts.AccountBase;
import com.main.app.database.TransactionDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Transactions {
    private final List<TransactionEntry> transactionHistory;
    public Transactions() {
        this.transactionHistory = new ArrayList<>();
    }
    public void addTransaction(TransactionType transactionType, float amount, int accountId, AccountBase account) {
        TransactionDAO transactionDAO = new TransactionDAO();
        TransactionEntry entry = new TransactionEntry(transactionType, amount, accountId);
        entry.setTransactionId(transactionDAO.saveNew(account.getAccountId(), transactionType, amount));
        transactionHistory.add(entry);
    }

    public List<TransactionEntry> getTransactions() {
        return transactionHistory;
    }
    public class TransactionEntry {
        private final TransactionType transactionType;
        private final float amount;
        private final LocalDate transactionDate;
        private final LocalTime transactionTime;
        private final int accountId;
        private int transactionId;
        private TransactionEntry(TransactionType transactionType, float amount, int accountId) {
            this.transactionType = transactionType;
            this.amount = amount;
            this.transactionDate = LocalDate.now();
            this.transactionTime = LocalTime.now();
            this.accountId = accountId;
            this.transactionId = -1;
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
        public void setTransactionId(int newTransactionId) {
            this.transactionId = newTransactionId;
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

