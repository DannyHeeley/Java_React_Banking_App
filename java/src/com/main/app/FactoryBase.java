package com.main.app;

import com.main.app.accounts.AccountBase;
import com.main.app.accounts.AccountManager;
import com.main.app.transactions.Transactions;

import static com.main.app.transactions.TransactionType.DEPOSIT;

public class FactoryBase {

    public static void handleUpdate(AccountBase acc, Float initialDeposit) {
        Transactions transactions = new Transactions();
        transactions.addTransaction(acc, DEPOSIT, initialDeposit, acc.getAccountId());
        AccountManager.addAccount(acc);
        Bank.getInstance().updateMainBankBalanceDeposit(initialDeposit);
    }

    public static void throwErrorIfAccountExists(String userName)
            throws AccountCreationException {
        if (AccountManager.accountExists(userName)) {
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
