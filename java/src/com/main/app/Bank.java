package com.main.app;


import com.main.app.accounts.AccountBase;
import com.main.app.accounts.AccountManager;

import java.util.ArrayList;

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

    public void updateBalanceDeposit(Float deposit) {
            bankTotalBalance += deposit;
    }

    public void updateBalanceWithdrawal(Float withdrawal) {
            bankTotalBalance -= withdrawal;
    }


    public void resetBank() {
        AccountManager.resetBankAccounts();
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
