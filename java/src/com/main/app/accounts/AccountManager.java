package com.main.app.accounts;

import com.main.app.database.DatabaseService;

import java.util.ArrayList;
import java.util.Objects;

import static com.main.app.transactions.TransactionType.DEPOSIT;

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

    public AccountBase addAccount(AccountBase account) {
        account.getTransactions().addTransaction(account, DEPOSIT, account.getAccountBalance(), account.getAccountId());
        bankAccounts.add(account);
        return account;
    }

    public void addToAccountBalance(AccountBase account, Float amount) {
        Float accBalance = account.getAccountBalance();
        accBalance += amount;
        account.setAccountBalance(accBalance);
        if (amount > 0) {
            DatabaseService.updateAccountBalanceInDatabase(account, account.getAccountBalance());
        }
    }
    public void subtractFromAccountBalance(AccountBase account, Float amount) {
        Float accBalance = account.getAccountBalance();
        accBalance -= amount;
        account.setAccountBalance(accBalance);
        if (amount > 0) {
            DatabaseService.updateAccountBalanceInDatabase(account, account.getAccountBalance());
        }
    }
    public boolean accountExists(String userName) {
        return bankAccounts.stream().anyMatch(account -> Objects.equals(account.getUserName(), userName));
    }
    public ArrayList<AccountBase> getBankAccounts() {
        return bankAccounts;
    }
    public void printAccounts() {
        for (AccountBase account : getBankAccounts()) {
            System.out.print("Account: " + account.getUserName());
            System.out.println(", Created: " + account.getDateAccountCreated());
        }
    }
    public void clearBankAccountList() {
        bankAccounts = new ArrayList<>();
    }
    private boolean userNameMatchesAccount(String userName, AccountBase account) {
        return Objects.equals(account.getUserName(), userName);
    }
}
