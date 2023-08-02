package com.main.app.accounts;

import com.main.app.Bank;
import com.main.app.Login.PasswordService;

import java.time.LocalDate;
import java.util.Objects;

import static com.main.app.accounts.AccountType.ADULT;
import static com.main.app.accounts.AccountType.STUDENT;

public class BankAccountFactory {
    static AccountBase acc;
    private BankAccountFactory() {}

    public static AccountBase createAccount(
            AccountType accountType,
            String userName,
            String newAccountPassword,
            Float initialDeposit,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String email
    ) {
        try {
            acc = createAccountForAccountType(accountType,
                    userName,
                    initialDeposit,
                    firstName,
                    lastName,
                    dateOfBirth,
                    email
            );
            Bank.getInstance().updateBalanceDeposit(initialDeposit);
            AccountManager.addAccount(acc);
        } catch(AccountCreationException e){
            System.out.println("Error Creating Acccount: " + e.getMessage());
        }
        setAccountPassword(acc, newAccountPassword);
        return acc;
    }

    private static AccountBase createAccountForAccountType(
            AccountType accountType,
            String userName,
            Float initialDeposit,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String email
    )
            throws AccountCreationException {
        if (Objects.equals(accountType, STUDENT)) {
            return BankAccountFactory.createNewStudentAccount(
                    userName,
                    initialDeposit,
                    firstName,
                    lastName,
                    dateOfBirth,
                    email);
        } else if (Objects.equals(accountType, ADULT)) {
            return BankAccountFactory.createNewAdultAccount(
                    userName,
                    initialDeposit,
                    firstName,
                    lastName,
                    dateOfBirth,
                    email
            );
        }
        throw new AccountCreationException("Account type must be valid");
    }

    private static AdultAccount createNewAdultAccount(
            String userName,
            Float initialDeposit,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String email
    ) throws AccountCreationException {
        throwErrorIfAccountCannotBeCreated(userName, initialDeposit);
        return new AdultAccount(
                userName,
                initialDeposit,
                firstName,
                lastName,
                dateOfBirth,
                email
        );
    }

    private static StudentAccount createNewStudentAccount(
            String userName,
            Float initialDeposit,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String email
    ) throws AccountCreationException {
        throwErrorIfAccountCannotBeCreated(userName, initialDeposit);
        return new StudentAccount(
                userName,
                initialDeposit,
                firstName,
                lastName,
                dateOfBirth,
                email
        );
    }

    private static void setAccountPassword(AccountBase account, String newAccountPassword) {
        PasswordService.setPasswordHashForAccount(account, newAccountPassword);
    }

    private static void throwErrorIfAccountCannotBeCreated(String userName, Float initialDeposit)
            throws AccountCreationException {
        if (initialDeposit < 0f) {
            throw new AccountCreationException("Cannot create account with a negative deposit");
        } else if (AccountManager.accountExists(userName)) {
            throw new AccountCreationException("Account already exists");
        }
    }

    public static class AccountCreationException extends Exception {
        public AccountCreationException(String message) {
            super(message);
        }
    }
}
