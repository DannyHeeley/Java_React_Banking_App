package com.main.app;

import com.main.app.accounts.AccountManager;
import com.main.app.transactions.TransactionType;

import java.util.concurrent.RejectedExecutionException;

import static com.main.app.transactions.TransactionType.DEPOSIT;
import static com.main.app.transactions.TransactionType.WITHDRAWAL;

public class Bank {
    private Float bankTotalBalance;
    private static Bank instance;

    private Bank() {
        this.bankTotalBalance = 0f;
    }

    public static Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }

    public Float getMainBankBalance() {
            return bankTotalBalance;
    }

    public void updateMainBankBalanceDeposit(Float amount) {
        handleNegativeArgument(DEPOSIT, amount);
        bankTotalBalance += amount;
    }

    public void updateMainBankBalanceWithdrawal(Float amount) {
        handleNegativeArgument(WITHDRAWAL, amount);
        if (bankTotalBalance >= amount) {
            bankTotalBalance -= amount;
        } else {
            throw new RejectedExecutionException("Bank balance not enough to cover withdrawal");
        }
    }

    public void resetBank() {
        AccountManager.getInstance().clearBankAccountList();
        bankTotalBalance = 0f;
    }

    public void printBankInfo() {
        System.out.println("Registered bank accounts: ");
        AccountManager.getInstance().printAccounts();
        getInstance().printBankBalance();
    }
    private void printBankBalance() {
        System.out.println("The total balance in the bank is: " + getMainBankBalance());
    }
    private void handleNegativeArgument(TransactionType transactionType, Float amount) {
        if (amount < 0) {
            if (transactionType == DEPOSIT) {
                throw new IllegalArgumentException("Deposit amount must be a positive number");
            }
            if (transactionType == WITHDRAWAL) {
                throw new IllegalArgumentException("Withdrawal amount must be a positive number");
            }
        }
    }
}
