package com.main.app.core;

import com.main.app.accounts.AccountManager;
import com.main.app.database.PersonDAO;
import com.main.app.users.Person;
import com.main.app.users.PersonalInformation;

import static com.main.app.services.PasswordService.hashPassword;

public class FactoryBase {
    public static void throwErrorIfAccountExists(int accountId)
            throws AccountCreationException {
        if (AccountManager.getInstance().accountExists(accountId)) {
            throw new AccountCreationException("Account already exists");
        }
    }

    public static void throwErrorIfDepositIsMinus(Float initialDeposit)
            throws AccountCreationException {
        if (initialDeposit < 0) {
            throw new AccountCreationException("Cannot create account with a negative deposit");
        }
    }
    public static String checkPasswordForErrors(String newAccountPassword) {
        String passwordHash;
        try {
            passwordHash = hashPassword(newAccountPassword);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            throw(e);
        }
        return passwordHash;
    }

    public static class AccountCreationException extends Exception {
        public AccountCreationException(String message) {
            super(message);
        }
    }

    public enum UserType {
        CUSTOMER,
        EMPLOYEE,
        ADMINISTRATOR,
    }

    public enum AccountType {
        ADULT,
        STUDENT,
    }
}
