package com.main.app.accounts;

import com.main.app.core.Bank;
import com.main.app.core.FactoryBase;
import com.main.app.transactions.Transactions;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.main.app.transactions.TransactionType.WITHDRAWAL;

public abstract class AccountBase {
    private FactoryBase.AccountType accountType;
    private Float currentBalance;
    private LocalDate dateCreated;
    private LocalDate dateLastUpdated;
    private LocalTime timeLastUpdated;
    private String passwordHash;
    private final Transactions transactions;
    private int accountId;
    private int customerId;
    private int accountNumber;
    private String userName;
    private int personId;

    AccountBase(
            FactoryBase.AccountType accountType,
            Float initialDeposit, String passwordHash,
            String userName
    ) {
        this.userName = userName;
        this.accountType = accountType;
        this.currentBalance = initialDeposit;
        Bank.getInstance().updateBankDeposit(initialDeposit);
        this.dateCreated = LocalDate.now();
        this.dateLastUpdated = LocalDate.now();
        this.timeLastUpdated = LocalTime.now();
        this.transactions = new Transactions();
        this.passwordHash = passwordHash;
        this.accountId = -1;
        this.customerId = -1;
        this.accountNumber = -1;
        this.personId = -1;
    }

    public abstract void deposit(Float amount);
    public void withdraw(Float amount) {
        AccountManager.getInstance().handleNegativeArgument(WITHDRAWAL, amount);
        if (amount <= getAccountBalance()) {
            AccountManager.getInstance().subtractFromAccountBalance(this, amount);
            setAccountUpdated(LocalDate.now());
            Bank.getInstance().updateBankWithdrawal(amount);
            System.out.println("Withdrawal of " + amount + " Successful, your currentBalance is: " + getAccountBalance());
        } else {
            throw new IllegalArgumentException("Withdrawal unsuccessful, your do not have enough currentBalance to cover the requested withdrawal amount");
        }
    }
    public String getAccountPasswordHash() {
        return passwordHash;
    }
    public Float getAccountBalance() {
        return currentBalance;
    }
    public void setAccountBalance(Float newBalance) {
        this.currentBalance = newBalance;
    }
    public Transactions getTransactions() {
        return transactions;
    }
    public FactoryBase.AccountType getAccountType() {
        return accountType;
    }
    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int newCustomerId) {
        this.customerId = newCustomerId;
    }
    public int getAccountId() {
        return accountId;
    }
    public void setAccountId(int newAccountId) {
        this.accountId = newAccountId;
    }
    public int getAccountNumber() {return accountNumber; }
    public void setAccountNumber(int newAccountNumber) {
        this.accountNumber = newAccountNumber;
    }
    public LocalDate getAccountUpdated() {
        return dateLastUpdated;
    }
    public void setAccountUpdated(LocalDate accountUpdated) {
        this.dateLastUpdated = accountUpdated;
    }
    public LocalDate getDateAccountCreated() {
        return dateCreated;
    }
    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }
    public int getPersonId() {
        return personId;
    }
    public void setPersonId(int personId) {
        this.personId = personId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public LocalTime getTimeLastUpdated() {
        return timeLastUpdated;
    }
    public void setTimeLastUpdated(LocalTime timeLastUpdated) {
        this.timeLastUpdated = timeLastUpdated;
    }
}
