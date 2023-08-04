package com.main.app.accounts;

import com.main.app.HandleDateTime;
import com.main.app.database.DatabaseService;
import com.main.app.transactions.Transactions;
import com.main.app.transactions.TransactionType;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.main.app.transactions.TransactionType.DEPOSIT;
import static com.main.app.transactions.TransactionType.WITHDRAWAL;

public abstract class AccountBase extends Person implements HandleDateTime, DatabaseService {
    private Float currentBalance;
    private final String userName;
    private final int accountNumber;
    private final String dateCreated;
    private String dateAccountLastUpdated;
    private String passwordHash;
    private final Transactions accountTransactionHistory;
    private int accountId;

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
        if (amount <= getAccountBalance()) {
            subtractFromAccountBalance(amount);
            System.out.println("Withdrawal of " + amount + " Successful, your currentBalance is: " + getAccountBalance());
            setAccountUpdatedTo(getDateTimeNowAsString());
            DatabaseService.updateAccountBalanceInDatabase(currentBalance, LocalDate.now(), LocalTime.now());
        } else {
            throw new IllegalArgumentException("Withdrawal unsuccessful, your do not have enough currentBalance to cover the requested withdrawal amount");
        }
    }

    public Float getAccountBalance() {
        return currentBalance;
    }
    public void addToAccountBalance(Float amount) {
        this.currentBalance += amount;
        accountTransactionHistory.addTransaction(this, DEPOSIT, amount, accountId);
        DatabaseService.updateAccountBalanceInDatabase(currentBalance, LocalDate.now(), LocalTime.now());
    }
    public void subtractFromAccountBalance(Float amount) {
        this.currentBalance -= amount;
        accountTransactionHistory.addTransaction(this, WITHDRAWAL, amount, this.getAccountId());
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
        System.out.println("Your Balance Is: " + getAccountBalance());
        System.out.println("Balance last updated: " + getDateAccountLastUpdated());
        System.out.println(getAccountTransactionHistory().toString());
    }
    public void handleNegativeArgument(TransactionType transactionType, Float amount) {
        if (amount < 0) {
            if (transactionType == DEPOSIT) {
                throw new IllegalArgumentException("Deposit amount must be a positive number");
            }
            if (transactionType == WITHDRAWAL) {
                throw new IllegalArgumentException("Withdrawal amount must be a positive number");
            }
        }
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
