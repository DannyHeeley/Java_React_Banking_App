package com.main.app.transactions;

import com.main.app.accounts.AccountManager;

import java.time.LocalDateTime;

public class TransactionEntry {
    private final LocalDateTime dateTime;
    private final float value;
    private final TransactionType type;
    private final double balance;

    public TransactionEntry(TransactionType type, float value, float balance) {
        this.dateTime = AccountManager.getDateTimeNow();
        this.type = type;
        this.value = value;
        this.balance = balance;
    }

    public LocalDateTime getTransactionDateTime() {
        return dateTime;
    }

    public double getTransactionValue() {
        return value;
    }

    public TransactionType getType() {
        return type;
    }

    public double getBalance() {
        return balance;
    }
}