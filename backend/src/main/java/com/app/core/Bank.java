package com.app.core;

import com.app.core.accounts.AccountBase;
import com.app.services.AccountManager;
import com.app.core.transactions.TransactionType;

import java.util.ArrayList;
import java.util.concurrent.RejectedExecutionException;

public class Bank {
    private Float bankTotalBalance;
    private ArrayList<AccountBase> bankAccounts = new ArrayList<>();
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
    public ArrayList<AccountBase> getBankAccounts() {
        return bankAccounts;
    }
    public void setBankAccounts(ArrayList<AccountBase> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }
    public Float getBankTotalBalance() {
            return bankTotalBalance;
    }
    public void updateBankDeposit(Float amount) {
        AccountManager.getInstance().handleNegativeArgument(TransactionType.DEPOSIT, amount);
        bankTotalBalance += amount;
    }
    public void updateBankWithdrawal(Float amount) {
        AccountManager.getInstance().handleNegativeArgument(TransactionType.WITHDRAWAL, amount);
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
}
