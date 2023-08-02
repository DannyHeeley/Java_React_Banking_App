package com.main.app.accounts;

import com.main.app.HandleDateTime;
import com.main.app.transactions.Transactions;
import com.main.app.transactions.TransactionType;

import java.util.ArrayList;

import static com.main.app.transactions.TransactionType.DEPOSIT;
import static com.main.app.transactions.TransactionType.WITHDRAWAL;

public abstract class AccountBase implements HandleDateTime {
    private Float balance;
    private final String bankName;
    private final String userName;
    private final int accountNumber;
    private final String dateAccountCreated;
    private String dateAccountLastUpdated;
    private String accountPasswordHash;
    private final Transactions accountTransactionHistory;

    AccountBase(String userName, Float balance) {
        this.bankName = "CashMoney Banking Services";
        this.userName = userName;
        this.accountNumber = generateAccountNumber();
        this.balance = balance;
        this.dateAccountCreated = getDateTimeNowAsString();
        this.accountPasswordHash = null;
        this.dateAccountLastUpdated = null;
        this.accountTransactionHistory = new Transactions();
    }

    abstract void deposit(Float amount);
    public void withdraw(Float amount) {
        handleNegativeArgument(WITHDRAWAL, amount);
        if (amount <= getBalance()) {
            subtractFromAccountBalance(amount);
            System.out.println("Withdrawal of " + amount + " Successful, your balance is: " + getBalance());
            setAccountUpdatedTo(getDateTimeNowAsString());
        } else {
            throw new IllegalArgumentException("Withdrawal unsuccessful, your do not have enough balance to cover the requested withdrawal amount");
        }
    }

    public Float getBalance() {
        return balance;
    }
    public void addToAccountBalance(Float amount) {
        this.balance += amount;
        accountTransactionHistory.addTransaction(DEPOSIT, amount);
    }
    public void subtractFromAccountBalance(Float amount) {
        this.balance -= amount;
        accountTransactionHistory.addTransaction(WITHDRAWAL, amount);
    }

    public String getAccountPasswordHash() {
        return accountPasswordHash;
    }
    public void setAccountPasswordHash(String hashedPassword) {
        accountPasswordHash = hashedPassword;
    }

    public String getDateAccountLastUpdated() {
        return dateAccountLastUpdated;
    }
    public void setAccountUpdatedTo(String accountUpdated) {
        this.dateAccountLastUpdated = accountUpdated;
    }

    public int getAccountNumber() {return accountNumber; }
    private int generateAccountNumber() {
        ArrayList<AccountBase> bankAccounts = AccountManager.getBankAccounts();
        if (!bankAccounts.isEmpty()) {
            return bankAccounts.get(bankAccounts.size() - 1).accountNumber + 1;
        } else {
            return 1;
        }
    }

    public String getUserName() {
        return userName;
    }
    public String getDateAccountCreated() {
        return dateAccountCreated;
    }
    public Transactions getAccountTransactionHistory() {
        return accountTransactionHistory;
    }

    public void printAccountInfo() {
        System.out.println("ACCOUNT INFO:");
        System.out.println("Bank: " + bankName);
        System.out.println("You are customer: " + getUserName());
        System.out.println("Your account number is: " + getAccountNumber());
        System.out.println("Account created: " + getDateTimeNowAsString());
        System.out.println("Your Balance Is: " + getBalance());
        System.out.println("Balance last updated: " + getDateAccountLastUpdated());
        System.out.println(getAccountTransactionHistory().toString());
    }
    public static void handleNegativeArgument(TransactionType transactionType, Float amount) {
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
