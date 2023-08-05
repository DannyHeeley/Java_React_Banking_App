package com.main.app;

import com.main.app.accounts.AccountManager;

public class FactoryBase {

    public static void throwErrorIfAccountExists(String userName)
            throws AccountCreationException {
        if (AccountManager.getInstance().accountExists(userName)) {
            throw new AccountCreationException("Account already exists");
        }
    }

    public static void throwErrorIfDepositIsMinus(Float initialDeposit)
            throws AccountCreationException {
        if (initialDeposit < 0) {
            throw new AccountCreationException("Cannot create account with a negative deposit");
        }
    }

    public static class AccountCreationException extends Exception {
        public AccountCreationException(String message) {
            super(message);
        }
    }
}
