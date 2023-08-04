package com.main.app.accounts;

import com.main.app.FactoryInterface;
import com.main.app.database.DatabaseService;

import java.time.LocalDate;
import java.util.Objects;

import static com.main.app.accounts.AccountType.*;

public class AccountFactory implements DatabaseService, FactoryInterface {
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
            FactoryInterface.handleUpdate(acc, initialDeposit);
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
        }
        throw new AccountCreationException("Account type must be valid");
    }

    private static AdultAccount adultAccount(
            String userName, Float initialDeposit, String firstName,
            String lastName, LocalDate dateOfBirth, String email,
            String newAccountPassword
    ) throws AccountCreationException {
        FactoryInterface.throwErrorIfAccountExists(userName);
        FactoryInterface.throwErrorIfDepositIsMinus(initialDeposit);
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

    private static StudentAccount studentAccount(
            String userName, Float initialDeposit, String firstName,
            String lastName, LocalDate dateOfBirth, String email,
            String newAccountPassword
    ) throws AccountCreationException {
        FactoryInterface.throwErrorIfAccountExists(userName);
        FactoryInterface.throwErrorIfDepositIsMinus(initialDeposit);
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


}
