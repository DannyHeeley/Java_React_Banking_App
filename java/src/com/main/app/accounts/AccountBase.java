package com.main.app.accounts;

import com.main.app.database.DatabaseService;
import com.main.app.entities.Customer;
import com.main.app.transactions.Transactions;
import com.main.app.transactions.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.main.app.transactions.TransactionType.DEPOSIT;
import static com.main.app.transactions.TransactionType.WITHDRAWAL;
import static java.lang.Integer.parseInt;
import static java.time.LocalDateTime.now;

public abstract class AccountBase implements DatabaseService {
    private Float currentBalance;
    private String userName;
    private final int accountNumber;
    private final LocalDate dateCreated;
    private LocalDate dateAccountLastUpdated;
    private String passwordHash;
    private final Transactions transactions;
    private final int accountId;
    private AccountType accountType;

    AccountBase(
            String userName,
            Float currentBalance,
            AccountType accountType,
            String passwordHash,
            Customer customer
    ) {
        this.userName = userName;
        this.accountType = accountType;
        this.currentBalance = currentBalance;
        this.dateCreated = LocalDate.now();
        this.dateAccountLastUpdated = LocalDate.now();
        this.transactions = new Transactions();
        this.passwordHash = passwordHash;
        this.accountNumber = AccountManager.getInstance().generateAccountNumber(customer);
        this.accountId = DatabaseService
                .addAccountEntryToDatabase(
                        customer,
                        accountNumber,
                        accountType,
                        currentBalance,
                        LocalDate.now(),
                        passwordHash
                );
    }

    abstract void deposit(Float amount);
    public void withdraw(Float amount) {
        AccountManager.getInstance().handleNegativeArgument(WITHDRAWAL, amount);
        if (amount <= getAccountBalance()) {
            AccountManager.getInstance().subtractFromAccountBalance(this, amount);
            transactions.addTransaction(this, WITHDRAWAL, amount, this.getAccountId());
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

}
