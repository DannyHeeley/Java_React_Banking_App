package com.main.app.accounts;

import com.main.app.Bank;

import java.time.LocalDateTime;
import java.util.*;

import static com.main.app.accounts.AccountType.*;

import static java.time.LocalDateTime.now;

public class AccountManager {

    //    responsible for managing various operations related to accounts,
    //    such as updating account information, handling transactions,
    //    and managing account state.

    static ArrayList<AccountBase> bankAccounts = Bank.getInstance().getBankAccounts();

    public AccountBase getAccount(String userName) {
        return bankAccounts.stream()
                .filter(account -> userNameMatchesAccount(userName, account))
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("Account does not exist");
                    return null;
                });
    }

    public AccountBase createAccount(AccountType accountType, String userName, String password, Float initialDeposit) {
        if (Objects.equals(accountType, STUDENT)) {
            return BankAccountFactory.createNewStudentAccount(userName, password, 100f);
        } if (Objects.equals(accountType, ADULT)) {
            return BankAccountFactory.createNewAdultAccount(userName, password,  initialDeposit);
        } return null;
    }

    private static boolean userNameMatchesAccount(String userName, AccountBase account) {
        return Objects.equals(account.getUserName(), userName);
    }

    public static boolean accountExists(String userName) {
        return bankAccounts.stream().anyMatch(account -> Objects.equals(account.getUserName(), userName));
    }

    public static LocalDateTime getDateTimeNow() {
        return now();
    }
}
