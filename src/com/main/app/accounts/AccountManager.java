package com.main.app.accounts;

import com.main.app.core.FactoryBase;
import com.main.app.users.Customer;
import com.main.app.transactions.TransactionType;
import com.main.app.database.AccountDAO;
import com.main.app.database.CustomerDAO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static com.main.app.transactions.TransactionType.DEPOSIT;
import static com.main.app.transactions.TransactionType.WITHDRAWAL;
import static java.lang.Integer.parseInt;
import static java.time.LocalDateTime.now;

public class AccountManager {
    private ArrayList<AccountBase> bankAccounts = new ArrayList<>();
    private static AccountManager instance;
    private AccountManager() {

    }
    public static AccountManager getInstance() {
        if (instance == null) {
            instance = new AccountManager();
        } return instance;
    }

    public AccountBase getAccount(int accountId) {
        return bankAccounts.stream()
                .filter(account -> Objects.equals(account.getAccountId(), accountId))
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("Account does not exist");
                    return null;
                });
    }
    public void addAccount(Customer customer, AccountBase account, FactoryBase.AccountType accountType, Float initialDeposit, String passwordHash) {
        int generatedAccountNumber = generateAccountNumber(customer);
        AccountDAO accountDAO = new AccountDAO();
        account.setAccountNumber(generatedAccountNumber);
        account.setCustomerId(customer.getCustomerId());
        account.setPersonId(customer.getPersonId());
        account.setAccountId(accountDAO.saveNew(
                customer,
                generatedAccountNumber,
                accountType,
                account.getAccountBalance(),
                LocalDate.now(),
                passwordHash
        ));
        customer.addAccount(account);
        accountDAO.updateAccountBalance(account, initialDeposit);
        account.getTransactions().addTransaction(DEPOSIT, initialDeposit, account.getAccountId(), account);
        bankAccounts.add(account);
    }
    public void addToAccountBalance(AccountBase account, Float amount) {
        if (amount > 0) {
            AccountDAO accountDAO = new AccountDAO();
            Float accBalance = account.getAccountBalance();
            accBalance += amount;
            account.setAccountBalance(accBalance);
            accountDAO.updateAccountBalance(account, accBalance);
            account.getTransactions().addTransaction(DEPOSIT, amount, account.getAccountId(), account);
        }
    }
    public void subtractFromAccountBalance(AccountBase account, Float amount) {
        if (amount > 0) {
            AccountDAO accountDAO = new AccountDAO();
        Float accBalance = account.getAccountBalance();
        accBalance -= amount;
        account.setAccountBalance(accBalance);
        accountDAO.updateAccountBalance(account, accBalance);
        account.getTransactions().addTransaction(WITHDRAWAL, amount, account.getAccountId(), account);
        }
    }
    public boolean accountExists(String userName) {
        return bankAccounts.stream().anyMatch(existingAccount -> Objects.equals(existingAccount.getUserName(), userName));
        // TODO: Check in the database directly if the list might not have the most recent data
    }
    public ArrayList<AccountBase> getBankAccounts() {
        return bankAccounts;
    }
    public void clearBankAccountList() {
        bankAccounts = new ArrayList<>();
    }
    public int generateAccountNumber(Customer customer) {
        String customerId = String.valueOf(customer.getCustomerId());
        String personId = String.valueOf(customer.getPersonId());
        return Integer.parseInt(customerId + personId);
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
    public void printAccounts() {
        for (AccountBase account : getBankAccounts()) {
            System.out.print("Account: " + account.getAccountId());
            System.out.println(", Created: " + account.getDateAccountCreated());
        }
    }
    public void printAccountInfo(AccountBase account) {
        CustomerDAO customerDAO = new CustomerDAO();
        String userName = customerDAO.getCustomer(account.getCustomerId()).getUserName();
        System.out.println("ACCOUNT INFO:");
        System.out.println("You are customer: " + userName);
        System.out.println("Your account number is: " + account.getAccountNumber());
        System.out.println("Account created: " + getDateTimeNowAsString());
        System.out.println("Your Balance Is: " + account.getAccountBalance());
        System.out.println("Balance last updated: " + account.getAccountUpdated());
        System.out.println(account.getTransactions().toString());
    }
    private String getDateTimeNowAsString() {
        LocalDateTime dateTimeNow = now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss");
        return dateTimeNow.format(formatter);
    }
}
