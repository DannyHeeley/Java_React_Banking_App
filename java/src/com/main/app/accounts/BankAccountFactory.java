package com.main.app.accounts;

import com.main.app.Bank;
import com.main.app.Login.PasswordService;

import java.util.Objects;

import static com.main.app.accounts.AccountType.ADULT;
import static com.main.app.accounts.AccountType.STUDENT;

public class BankAccountFactory {

    private BankAccountFactory() {}

    public static AccountBase createAccount(AccountType accountType, String userName, String password, Float initialDeposit) {
        try {
            if (Objects.equals(accountType, STUDENT)) {
                Bank.getInstance().updateBalanceDeposit(initialDeposit);
                AccountBase acc = BankAccountFactory.createNewStudentAccount(userName, password, initialDeposit);
                AccountManager.addAccount(acc);
                return acc;
            } else if (Objects.equals(accountType, ADULT)) {
                Bank.getInstance().updateBalanceDeposit(initialDeposit);
                AccountBase acc = BankAccountFactory.createNewAdultAccount(userName, password, initialDeposit);
                AccountManager.addAccount(acc);
                return acc;
            }
        } catch(AccountCreationException e){
            System.out.println("Error Creating Acccount: " + e.getMessage());
        } return null;
    }

    private static AdultAccount createNewAdultAccount(String userName, String password, Float initialDeposit)
            throws AccountCreationException {
        if (AccountManager.accountExists(userName)) {
            throw new AccountCreationException("Account already exists");
        }
        AdultAccount adultAccount = new AdultAccount(userName, initialDeposit);
        PasswordService.setPasswordHashForAccount(adultAccount.getAccountNumber(), password);
        return adultAccount;
    }

    private static StudentAccount createNewStudentAccount(String userName, String password, Float initialDeposit)
            throws AccountCreationException {
        if (AccountManager.accountExists(userName)) {
            throw new AccountCreationException("Account already exists");
        }
        StudentAccount studentAccount = new StudentAccount(userName, initialDeposit);
        Bank.getInstance().updateBalanceDeposit(initialDeposit );
        PasswordService.setPasswordHashForAccount(studentAccount.getAccountNumber(), password);
        return studentAccount;
    }

    public static class AccountCreationException extends Exception {
        public AccountCreationException(String message) {
            super(message);
        }
    }

}
