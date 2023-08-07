package com.main.app.accounts;

import com.main.app.transactions.Transactions;

import java.time.LocalDate;

import static com.main.app.transactions.TransactionType.WITHDRAWAL;

public abstract class AccountBase {

    private AccountType accountType;
    private Float currentBalance;
    private String userName;
    private final LocalDate dateCreated;
    private LocalDate dateAccountLastUpdated;
    private String passwordHash;
    private final Transactions transactions;
    private int accountId;
    private int customerId;
    private int accountNumber;
    private int personId;


    AccountBase(
            String userName,
            AccountType accountType,
            String passwordHash
    ) {
        this.userName = userName;
        this.accountType = accountType;
        this.currentBalance = 0f;
        this.dateCreated = LocalDate.now();
        this.dateAccountLastUpdated = LocalDate.now();
        this.transactions = new Transactions();
        this.passwordHash = passwordHash;
        this.accountNumber = -1;
        this.customerId = -1;
        this.accountId = -1;
        this.personId = -1;
    }

    public abstract void deposit(Float amount);
    public void withdraw(Float amount) {
        AccountManager.getInstance().handleNegativeArgument(WITHDRAWAL, amount);
        if (amount <= getAccountBalance()) {
            AccountManager.getInstance().subtractFromAccountBalance(this, amount);
            setAccountUpdatedTo(LocalDate.now());
            System.out.println("Withdrawal of " + amount + " Successful, your currentBalance is: " + getAccountBalance());
        } else {
            throw new IllegalArgumentException("Withdrawal unsuccessful, your do not have enough currentBalance to cover the requested withdrawal amount");
        }
    }

    public Float getAccountBalance() {
        return currentBalance;
    }

    public void setAccountBalance(Float newBalance) {
        this.currentBalance = newBalance;
    }

    public String getAccountPasswordHash() {
        return passwordHash;
    }
    public void setAccountPasswordHash(String hashedPassword) {
        passwordHash = hashedPassword;
    }

    public LocalDate getDateAccountLastUpdated() {
        return dateAccountLastUpdated;
    }
    public void setAccountUpdatedTo(LocalDate accountUpdated) {
        this.dateAccountLastUpdated = accountUpdated;
    }

    public int getAccountNumber() {return accountNumber; }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String newUserName) {
        this.userName = newUserName;
    }
    public LocalDate getDateAccountCreated() {
        return dateCreated;
    }
    public Transactions getTransactions() {
        return transactions;
    }

    public int getAccountId() {
        return accountId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int newCustomerId) {
        this.customerId = newCustomerId;
    }

    public void setAccountId(int newAccountId) {
        this.accountId = newAccountId;
    }

    public void setAccountNumber(int newAccountNumber) {
        this.accountNumber = newAccountNumber;
    }

    public void setPersonId(int newPersonId) {
        this.personId = newPersonId;
    }
}
