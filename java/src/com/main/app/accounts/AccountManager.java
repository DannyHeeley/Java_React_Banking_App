package com.main.app.accounts;

import com.main.app.database.DatabaseService;

import java.util.ArrayList;
import java.util.Objects;

public class AccountManager implements DatabaseService {

    //    responsible for managing various operations related to accounts,
    //    such as updating account information, handling transactions,
    //    and managing account state.

    private static int lastAccountNumber = 0;
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
    }

    public AccountBase addAccount(AccountBase account) {
        bankAccounts.add(account);
        return account;
    }

    public boolean accountExists(String userName) {
        return bankAccounts.stream().anyMatch(account -> Objects.equals(account.getUserName(), userName));
    }

    public ArrayList<AccountBase> getBankAccounts() {
        //System.out.println(DatabaseService.getAllAccountsFromDatabase());
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

    public static int generateAccountNumber() {
        lastAccountNumber++;
        return lastAccountNumber;
    }

}
