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
    private int accountNumber = -1;
    private final LocalDate dateCreated;
    private LocalDate dateAccountLastUpdated;
    private String passwordHash;
    private final Transactions transactions;
    private int accountId = -1;
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
        this.accountId = DatabaseService
                .addAccountEntryToDatabase(
                        customer,
                        accountNumber,
                        accountType,
                        currentBalance,
                        LocalDate.now(),
                        passwordHash
                );
        this.accountNumber = generateAccountNumber(customer);
        transactions.addTransaction(this, DEPOSIT, this.currentBalance, this.accountId);
        AccountManager.getInstance().addAccount(this);
    }

    abstract void deposit(Float amount);
    public void withdraw(Float amount) {
        handleNegativeArgument(WITHDRAWAL, amount);
        if (amount <= getAccountBalance()) {
            subtractFromAccountBalance(amount);
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
    public void addToAccountBalance(Float amount) {
        this.currentBalance += amount;
        if (amount > 0) {
            DatabaseService.updateAccountBalanceInDatabase(this, currentBalance);
        }
    }
    public void subtractFromAccountBalance(Float amount) {
        this.currentBalance -= amount;
        if (amount > 0) {
            DatabaseService.updateAccountBalanceInDatabase(this, currentBalance);
        }
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

    private int generateAccountNumber(Customer customer) {
        return parseInt(String.valueOf(customer.getCustomerId()) + String.valueOf(accountId));
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

    public void printAccountInfo() {
        System.out.println("ACCOUNT INFO:");
        System.out.println("You are customer: " + getUserName());
        System.out.println("Your account number is: " + getAccountNumber());
        System.out.println("Account created: " + getDateTimeNowAsString());
        System.out.println("Your Balance Is: " + getAccountBalance());
        System.out.println("Balance last updated: " + getDateAccountLastUpdated());
        System.out.println(getTransactions().toString());
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

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    private String getDateTimeNowAsString() {
        LocalDateTime dateTimeNow = now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss");
        return dateTimeNow.format(formatter);
    }
}
