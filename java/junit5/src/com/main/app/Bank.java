package com.main.app;


import com.main.app.accounts.AccountBase;

import java.util.ArrayList;

public class Bank {
    private Float bankTotalBalance = 0f;
    private static Bank instance;

    // Implement DB for accounts
    private ArrayList<AccountBase> bankAccounts = new ArrayList<>();

    private Bank() {

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
            bankTotalBalance += withdrawal;
    }

    public ArrayList<AccountBase> getBankAccounts() {
        return bankAccounts;
    }
    public void printBankInfo() {
        System.out.println("Registered bank accounts: ");
        getInstance().printAccounts();
        getInstance().printBankBalance();
    }
    private void printBankBalance() {
        System.out.println("The total balance in the bank is: " + getBankBalance());
    }
    private void printAccounts() {
        for (AccountBase account : getBankAccounts()) {
            System.out.print("Account: " + account.getUserName());
            System.out.println(", Created: " + account.getAccountCreated());
        }
    }
}
