package com.main.app.accounts;

import com.main.app.HandleDateTime;
import com.main.app.database.DatabaseService;
import com.main.app.transactions.Transactions;
import com.main.app.transactions.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import static com.main.app.transactions.TransactionType.DEPOSIT;
import static com.main.app.transactions.TransactionType.WITHDRAWAL;

public abstract class AccountBase extends Person implements HandleDateTime {
    private Float currentBalance;
    private final String userName;
    private final int accountNumber;
    private final String dateCreated;
    private String dateAccountLastUpdated;
    private String passwordHash;
    private final Transactions accountTransactionHistory;

    private static int accountId;

    AccountBase(
            String userName,
            Float currentBalance,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String email
    ) {
        super(firstName, lastName, dateOfBirth, email);
        this.userName = userName;
        this.accountNumber = AccountManager.generateAccountNumber();
        this.currentBalance = currentBalance;
        this.dateCreated = getDateTimeNowAsString();
        this.passwordHash = null;
        this.dateAccountLastUpdated = null;
        this.accountTransactionHistory = new Transactions();
        this.accountId = 0;
    }

    abstract void deposit(Float amount);
    public void withdraw(Float amount) {
        handleNegativeArgument(WITHDRAWAL, amount);
        if (amount <= getBalance()) {
            subtractFromAccountBalance(amount);
            System.out.println("Withdrawal of " + amount + " Successful, your currentBalance is: " + getBalance());
            setAccountUpdatedTo(getDateTimeNowAsString());
        } else {
            throw new IllegalArgumentException("Withdrawal unsuccessful, your do not have enough currentBalance to cover the requested withdrawal amount");
        }
    }

    public Float getBalance() {
        return currentBalance;
    }
    public void addToAccountBalance(Float amount) {
        this.currentBalance += amount;
        accountTransactionHistory.addTransaction(DEPOSIT, amount, accountId);
    }
    public void subtractFromAccountBalance(Float amount) {
        this.currentBalance -= amount;
        accountTransactionHistory.addTransaction(WITHDRAWAL, amount, accountId);
    }

    public String getAccountPasswordHash() {
        return passwordHash;
    }
    public void setAccountPasswordHash(String hashedPassword) {
        passwordHash = hashedPassword;
    }

    public String getDateAccountLastUpdated() {
        return dateAccountLastUpdated;
    }
    public void setAccountUpdatedTo(String accountUpdated) {
        this.dateAccountLastUpdated = accountUpdated;
    }

    public int getAccountNumber() {return accountNumber; }
    public String getUserName() {
        return userName;
    }
    public String getDateAccountCreated() {
        return dateCreated;
    }
    public Transactions getAccountTransactionHistory() {
        return accountTransactionHistory;
    }

    public void printAccountInfo() {
        System.out.println("ACCOUNT INFO:");
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

    public static int getAccountId() {
        return accountId;
    }

    public static void setAccountId(int accountId) {
        AccountBase.accountId = accountId;
    }
}
