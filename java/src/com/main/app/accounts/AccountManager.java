package com.main.app.accounts;

import com.main.app.Bank;
import com.main.app.entities.Customer;
import com.main.app.entities.Person;
import com.main.app.transactions.TransactionType;
import com.main.app.wiring.AccountDAO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import static com.main.app.transactions.TransactionType.DEPOSIT;
import static com.main.app.transactions.TransactionType.WITHDRAWAL;
import static java.lang.Integer.parseInt;
import static java.time.LocalDateTime.now;

public class AccountManager {

    //    responsible for managing various operations related to accounts,
    //    such as interacting with the database to
    //    update account information, handle transactions,
    //    and manage account state.

    private ArrayList<AccountBase> bankAccounts = new ArrayList<>();
    private static AccountManager instance;
    private AccountManager() {

    }
    public static AccountManager getInstance() {
        if (instance == null) {
            instance = new AccountManager();
        } return instance;
    }
    public AccountBase getAccount(String userName) {
        return bankAccounts.stream()
                .filter(account -> userNameMatchesAccount(userName, account))
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("Account does not exist");
                    return null;
                });
        // Get account from database, create a new account populated with the arguments
    }

    public AccountBase addAccount(Customer customer, AccountBase account, AccountType accountType, Float initialDeposit, String passwordHash, Person person) {
        AccountDAO accountDAO = new AccountDAO();
        account.setPersonId(person.getPersonId());
        account.setCustomerId(customer.getCustomerId());
        account.setAccountNumber(AccountManager.getInstance().generateAccountNumber(customer, person));
        account.setAccountId(accountDAO.saveNew(
                customer,
                account.getAccountNumber(),
                accountType,
                account.getAccountBalance(),
                LocalDate.now(),
                passwordHash
        ));
        customer.addAccount(account);
        AccountDAO.updateAccountBalanceInDatabase(account, initialDeposit);
        account.getTransactions().addTransaction(DEPOSIT, initialDeposit, account.getAccountId(), account);
        bankAccounts.add(account);
        return account;
    }

    public void addToAccountBalance(AccountBase account, Float amount) {
        if (amount > 0) {
            Float accBalance = account.getAccountBalance();
            accBalance += amount;
            account.setAccountBalance(accBalance);
            AccountDAO.updateAccountBalanceInDatabase(account, accBalance);
            account.getTransactions().addTransaction(DEPOSIT, amount, account.getAccountId(), account);
        }
    }

    public void subtractFromAccountBalance(AccountBase account, Float amount) {
        if (amount > 0) {
        Float accBalance = account.getAccountBalance();
        accBalance -= amount;
        account.setAccountBalance(accBalance);
        AccountDAO.updateAccountBalanceInDatabase(account, accBalance);
        account.getTransactions().addTransaction(WITHDRAWAL, amount, account.getAccountId(), account);
        }
    }


    public boolean accountExists(String userName) {
        return bankAccounts.stream().anyMatch(account -> Objects.equals(account.getUserName(), userName));
    }
    public ArrayList<AccountBase> getBankAccounts() {
        return bankAccounts;
    }
    public void clearBankAccountList() {
        bankAccounts = new ArrayList<>();
    }
    private boolean userNameMatchesAccount(String userName, AccountBase account) {
        return Objects.equals(account.getUserName(), userName);
    }
    public int generateAccountNumber(Customer customer, Person person) {
        String customerId = String.valueOf(customer.getCustomerId());
        String personId = String.valueOf(person.getPersonId());
        return parseInt(customerId + personId);
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
            System.out.print("Account: " + account.getUserName());
            System.out.println(", Created: " + account.getDateAccountCreated());
        }
    }
    public void printAccountInfo(AccountBase account) {
        System.out.println("ACCOUNT INFO:");
        System.out.println("You are customer: " + account.getUserName());
        System.out.println("Your account number is: " + account.getAccountNumber());
        System.out.println("Account created: " + getDateTimeNowAsString());
        System.out.println("Your Balance Is: " + account.getAccountBalance());
        System.out.println("Balance last updated: " + account.getDateAccountLastUpdated());
        System.out.println(account.getTransactions().toString());
    }
    private String getDateTimeNowAsString() {
        LocalDateTime dateTimeNow = now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss");
        return dateTimeNow.format(formatter);
    }
}
