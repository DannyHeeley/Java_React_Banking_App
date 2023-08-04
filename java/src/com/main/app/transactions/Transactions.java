package com.main.app.transactions;

import com.main.app.HandleDateTime;
import com.main.app.accounts.AccountBase;
import com.main.app.database.DatabaseService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transactions implements HandleDateTime, DatabaseService {
    private final List<TransactionEntry> transactionHistory;
    public Transactions() {
        transactionHistory = new ArrayList<>();
    }
    public void addTransaction(AccountBase account, TransactionType transactionType, float amount, int accountId) {
        TransactionEntry entry = new TransactionEntry(account, transactionType, amount, accountId);
        transactionHistory.add(entry);
    }
    private class TransactionEntry {
        private final TransactionType transactionType;
        private final float amount;
        private final LocalDateTime transactionDate;
        private final int accountId;
        private final int transactionId;
        private TransactionEntry(AccountBase account, TransactionType transactionType, float amount, int accountId) {
            this.transactionType = transactionType;
            this.amount = amount;
            this.transactionDate = getDateTimeNow();
            this.accountId = accountId;
            this.transactionId = DatabaseService.addTransactionEntryToDatabase(
                    account, transactionType, amount
            );
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
            sb.append("Date/Time: ").append(parseDateTimeToString((entry.getTransactionDateTime()))).append(", ");
            sb.append("Value: ").append(entry.getTransactionValue()).append(", ");
            sb.append("Type: ").append(entry.getTransactionType()).append(", ");
        }
        return sb.toString();
    }
}

