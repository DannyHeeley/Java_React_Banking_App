package com.main.app.accounts;

import com.main.app.Bank;
import com.main.app.database.DatabaseService;
import com.main.app.transactions.Transactions;

import java.time.LocalDate;
import java.util.Objects;

import static com.main.app.accounts.AccountType.*;
import static com.main.app.transactions.TransactionType.DEPOSIT;

public class AccountFactory implements DatabaseService {
    private AccountFactory() {}
    public static AccountBase createAccount(
            AccountType accountType, String userName,
            String newAccountPassword, Float initialDeposit,
            String firstName, String lastName,
            LocalDate dateOfBirth, String email
    ) {
        AccountBase acc = null;
        try {
            acc = createAccountForAccountType(accountType,
                    userName,
                    initialDeposit,
                    firstName,
                    lastName,
                    dateOfBirth,
                    email,
                    newAccountPassword
            );
            handleUpdate(acc, initialDeposit);
        } catch (AccountCreationException e) {
            System.out.println("Error Creating Acccount: " + e.getMessage());
        }
        return acc;
    }

    private static AccountBase createAccountForAccountType(
            AccountType accountType, String userName, Float initialDeposit,
            String firstName, String lastName, LocalDate dateOfBirth,
            String email, String newAccountPassword
    )
            throws AccountCreationException {
        if (Objects.equals(accountType, STUDENT)) {
            return studentAccount(userName, initialDeposit, firstName, lastName, dateOfBirth, email, newAccountPassword);
        } else if (Objects.equals(accountType, ADULT)) {
            return adultAccount(userName, initialDeposit, firstName, lastName, dateOfBirth, email, newAccountPassword);
        } else if (Objects.equals(accountType, EMPLOYEE)) {
            return employee(EMPLOYEE, userName, firstName, lastName, dateOfBirth, email, newAccountPassword);
        } else if (Objects.equals(accountType, ADMINISTRATOR)) {
            return employee(ADMINISTRATOR, userName, firstName, lastName, dateOfBirth, email, newAccountPassword);
        }
        throw new AccountCreationException("Account type must be valid");
    }

    private static AdultAccount adultAccount(
            String userName, Float initialDeposit, String firstName,
            String lastName, LocalDate dateOfBirth, String email,
            String newAccountPassword
    ) throws AccountCreationException {
        return AccountFactory.createNewAdultAccount(
                userName,
                initialDeposit,
                firstName,
                lastName,
                dateOfBirth,
                email,
                newAccountPassword
        );
    }

    private static StudentAccount studentAccount(
            String userName, Float initialDeposit, String firstName,
            String lastName, LocalDate dateOfBirth, String email,
            String newAccountPassword
    ) throws AccountCreationException {
        return AccountFactory.createNewStudentAccount(
                userName,
                initialDeposit,
                firstName,
                lastName,
                dateOfBirth,
                email,
                newAccountPassword
        );
    }

    private static Employee employee(
            AccountType accountType, String userName, String firstName,
            String lastName, LocalDate dateOfBirth, String email,
            String newAccountPassword
    ) throws AccountCreationException {
        return AccountFactory.createNewEmployeeAccount(
                accountType,
                userName,
                firstName,
                lastName,
                dateOfBirth,
                email,
                newAccountPassword
        );
    }

    private static AdultAccount createNewAdultAccount(
            String userName, Float initialDeposit, String firstName,
            String lastName, LocalDate dateOfBirth, String email,
            String newAccountPassword
    ) throws AccountCreationException {
        throwsErrorIfAccountExistsOrMinusDeposit(userName, initialDeposit);
        return new AdultAccount(
                userName,
                initialDeposit,
                firstName,
                lastName,
                dateOfBirth,
                email,
                newAccountPassword
        );
    }

    private static StudentAccount createNewStudentAccount(
            String userName, Float initialDeposit, String firstName,
            String lastName, LocalDate dateOfBirth, String email,
            String newAccountPassword
    ) throws AccountCreationException {
        throwsErrorIfAccountExistsOrMinusDeposit(userName, initialDeposit);
        return new StudentAccount(
                userName,
                initialDeposit,
                firstName,
                lastName,
                dateOfBirth,
                email,
                newAccountPassword
        );
    }

    private static Employee createNewEmployeeAccount(
            AccountType accountType, String userName, String firstName, String lastName,
            LocalDate dateOfBirth, String email, String newAccountPassword
    ) throws AccountCreationException {
        throwsErrorIfAccountExistsOrMinusDeposit(userName, 0f);
        return new Employee(
                accountType,
                userName,
                firstName,
                lastName,
                dateOfBirth,
                email,
                newAccountPassword
        );
    }

    private static void handleUpdate(AccountBase acc, Float initialDeposit) {
        Transactions transactions = new Transactions();
        transactions.addTransaction(acc, DEPOSIT, initialDeposit, acc.getAccountId());
        AccountManager.addAccount(acc);
        Bank.getInstance().updateMainBankBalanceDeposit(initialDeposit);
    }

    private static void throwsErrorIfAccountExistsOrMinusDeposit(String userName, Float initialDeposit)
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
