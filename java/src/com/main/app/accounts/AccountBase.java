package com.main.app.accounts;
import com.main.app.HandleDateTime;
import com.main.app.transactions.TransactionHistory;

import java.util.ArrayList;

import static com.main.app.transactions.TransactionType.*;
import static java.time.LocalDateTime.now;

public abstract class AccountBase implements HandleDateTime {
    private Float balance;
    private final String bankName;
    private final String userName;
    private final int accountNumber;
    private final String dateAccountCreated;
    private String dateAccountLastUpdated;
    private String accountPasswordHash;
    private final TransactionHistory transactionHistory;

    AccountBase(String userName, Float balance) {
        this.bankName = "CashMoney Banking Services";
        this.userName = userName;
        this.accountNumber = generateAccountNumber();
        this.balance = balance;
        this.dateAccountCreated = getDateTimeNowAsString();
        this.accountPasswordHash = null;
        this.dateAccountLastUpdated = null;
        this.transactionHistory = new TransactionHistory();
    }

    abstract void deposit(Float amount);

    public void withdraw( Float amount) {
        if (amount >= 0) {
            if (amount <= getBalance()) {
                subtractFromAccountBalance(amount);
                System.out.println("Withdrawal of " + amount + " Successful, your balance is: " + getBalance());
                setAccountUpdatedTo(getDateTimeNowAsString());
            } else {
                throw new RuntimeException("Withdrawal unsuccessful, your do not have enough balance to cover the requested withdrawal amount");
            }
        } else {
            throw new RuntimeException("Withdrawal amount must be a positive number");
        }
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


    public String getUserName() {
        return userName;
    }

    public String getDateAccountCreated() {
        return dateAccountCreated;
    }

    public Float getBalance() {
        return balance;
    }
    public void addToAccountBalance(Float amount) {
        this.balance += amount;
        TransactionHistory.addTransaction(DEPOSIT, amount, balance);
    }
    public void subtractFromAccountBalance(Float amount) {
        this.balance -= amount;
        TransactionHistory.addTransaction(WITHDRAWAL, amount, balance);
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

    public TransactionHistory getTransactionHistory() {
        return transactionHistory;
    }

    public void printAccountInfo() {
        System.out.println("ACCOUNT INFO:");
        System.out.println("Bank: " + bankName);
        System.out.println("You are customer: " + getUserName());
        System.out.println("Your account number is: " + getAccountNumber());
        System.out.println("Account created: " + getDateTimeNowAsString());
        System.out.println("Your Balance Is: " + getBalance());
        System.out.println("Balance last updated: " + getDateAccountLastUpdated());
        System.out.println(getTransactionHistory().toString());
    }


}
