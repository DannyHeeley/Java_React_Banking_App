package com.main.app.accounts;

import com.main.app.Bank;
import com.main.app.FactoryBase;
import com.main.app.transactions.Transactions;

import java.time.LocalDate;

import static com.main.app.transactions.TransactionType.WITHDRAWAL;

public abstract class AccountBase {
    private FactoryBase.AccountType accountType;
    private Float currentBalance;
    private final LocalDate dateCreated;
    private LocalDate dateAccountLastUpdated;
    private String passwordHash;
    private final Transactions transactions;
    private int accountId;
    private int customerId;
    private int accountNumber;

    AccountBase(
            FactoryBase.AccountType accountType,
            Float initialDeposit, String passwordHash
    ) {
        this.accountType = accountType;
        this.currentBalance = initialDeposit;
        Bank.getInstance().updateBankDeposit(initialDeposit);
        this.dateCreated = LocalDate.now();
        this.dateAccountLastUpdated = LocalDate.now();
        this.transactions = new Transactions();
        this.passwordHash = passwordHash;
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
        return dateAccountLastUpdated;
    }
    public void setAccountUpdated(LocalDate accountUpdated) {
        this.dateAccountLastUpdated = accountUpdated;
    }
    public LocalDate getDateAccountCreated() {
        return dateCreated;
    }
}
