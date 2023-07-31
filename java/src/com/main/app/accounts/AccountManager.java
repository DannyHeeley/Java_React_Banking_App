package com.main.app.accounts;

import com.main.app.Bank;

import javax.management.InstanceAlreadyExistsException;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;

import static com.main.app.accounts.AccountType.*;

import static java.time.LocalDateTime.now;

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

    public static AccountBase createAccount(AccountType accountType, String userName, String password, Float initialDeposit) {
        try {
            if (Objects.equals(accountType, STUDENT)) {
                return BankAccountFactory.createNewStudentAccount(userName, password, initialDeposit);
            } else if (Objects.equals(accountType, ADULT)) {
                return BankAccountFactory.createNewAdultAccount(userName, password, initialDeposit);
            } else {
                return null;
            }
        } catch(Exception e){
            throw new RuntimeException("Account already exists");
        }
    }

    private static boolean userNameMatchesAccount(String userName, AccountBase account) {
        return Objects.equals(account.getUserName(), userName);
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
            System.out.println(", Created: " + account.getAccountCreated());
        }
    }
    public static void resetBankAccounts() {
        bankAccounts = new ArrayList<>();
    }

}
