package com.main.app;

import com.main.app.accounts.AccountBase;
import com.main.app.accounts.AccountManager;

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

    public Float getBankBalance() {
            return bankTotalBalance;
    }

    public void updateBalanceDeposit(Float amount) {
        AccountBase.handleNegativeArgument(DEPOSIT, amount);
        bankTotalBalance += amount;
    }

    public void updateBalanceWithdrawal(Float amount) {
        AccountBase.handleNegativeArgument(WITHDRAWAL, amount);
        if (bankTotalBalance >= amount) {
            bankTotalBalance -= amount;
        } else {
            throw new RejectedExecutionException("Bank balance not enough to cover withdrawal");
        }
    }

    public void resetBank() {
        AccountManager.clearBankAccountList();
        bankTotalBalance = 0f;
    }

    public void printBankInfo() {
        System.out.println("Registered bank accounts: ");
        AccountManager.printAccounts();
        getInstance().printBankBalance();
    }
    private void printBankBalance() {
        System.out.println("The total balance in the bank is: " + getBankBalance());
    }

}
