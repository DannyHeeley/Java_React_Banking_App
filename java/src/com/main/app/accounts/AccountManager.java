package com.main.app.accounts;

import java.util.ArrayList;
import java.util.Objects;

public class AccountManager {

    //    responsible for managing various operations related to accounts,
    //    such as updating account information, handling transactions,
    //    and managing account state.


    // Implement DB for accounts

    private static ArrayList<AccountBase> bankAccounts = new ArrayList<>();

    public static AccountBase getAccount(String userName) {
        return bankAccounts.stream()
                .filter(account -> userNameMatchesAccount(userName, account))
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("Account does not exist");
                    return null;
                });
    }

    public static AccountBase addAccount(AccountBase account) {
        bankAccounts.add(account);
        return account;
    }

    public static boolean accountExists(String userName) {
        return bankAccounts.stream().anyMatch(account -> Objects.equals(account.getUserName(), userName));
    }

    public static ArrayList<AccountBase> getBankAccounts() {
        return bankAccounts;
    }

    public static void printAccounts() {
        for (AccountBase account : getBankAccounts()) {
            System.out.print("Account: " + account.getUserName());
            System.out.println(", Created: " + account.getDateAccountCreated());
        }
    }

    public static void clearBankAccountList() {
        bankAccounts = new ArrayList<>();
    }

    private static boolean userNameMatchesAccount(String userName, AccountBase account) {
        return Objects.equals(account.getUserName(), userName);
    }


}
