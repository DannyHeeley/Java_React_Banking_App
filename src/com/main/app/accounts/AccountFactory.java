package com.main.app.accounts;

import com.main.app.FactoryBase;
import com.main.app.entities.Customer;
import com.main.app.entities.EntityFactory;
import com.main.app.entities.Person;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Objects;

import static com.main.app.FactoryBase.AccountType.ADULT;
import static com.main.app.FactoryBase.AccountType.STUDENT;
import static com.main.app.accounts.PasswordService.hashPassword;

public class AccountFactory extends FactoryBase {

    private AccountFactory() {
    }

    public static AccountBase newAccount(
            AccountType accountType, String userName,
            String newAccountPassword, Float initialDeposit,
            String firstName, String lastName,
            LocalDate dateOfBirth, String email
    ) {
        PersonalInformation pi = new PersonalInformation(firstName, lastName, dateOfBirth, email);
        String passwordHash;
        AccountBase account = null;
        Customer customer;
        Person person;
        try {
            passwordHash = hashPassword(newAccountPassword);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            throw(e);
        }
        try {
            // Create person, customer, account, set id's, and save to database
            person = createNewPerson(pi);
            customer = EntityFactory.customer(pi, person, userName);
            account = createNewAccount(accountType, userName, initialDeposit, passwordHash, customer, person);
        } catch (AccountCreationException e) {
            System.out.println("Error Creating Acccount: " + e.getMessage());
        }
        return account;
    }

    @NotNull
    private static AccountBase createNewAccount(
            AccountType accountType, String userName, Float initialDeposit,
            String passwordHash, Customer customer,
            Person person) throws AccountCreationException {
        AccountBase account = handleCreatingAccountForType(
                accountType,
                userName,
                initialDeposit,
                passwordHash
        );
        AccountManager.getInstance().addAccount(customer, account, accountType, initialDeposit, passwordHash, person);
        return account;
    }

    private static AccountBase handleCreatingAccountForType(
            AccountType accountType, String userName, Float initialDeposit,
            String newAccountPassword
    ) throws AccountCreationException {
        if (Objects.equals(accountType, STUDENT)) {
            return studentAccount(userName, initialDeposit, newAccountPassword);
        } else if (Objects.equals(accountType, ADULT)) {
            return adultAccount(userName, initialDeposit, newAccountPassword);
        }
        throw new AccountCreationException("Account type must be valid");
    }

    private static AdultAccount adultAccount(
            String userName, Float initialDeposit, String newAccountPassword
    ) throws AccountCreationException {
        FactoryBase.throwErrorIfDepositIsMinus(initialDeposit);
        AdultAccount account = new AdultAccount(userName, initialDeposit, newAccountPassword);
        FactoryBase.throwErrorIfAccountExists(account.getAccountId());
        return account;
    }

    private static StudentAccount studentAccount(
            String userName, Float initialDeposit, String newAccountPassword
    ) throws AccountCreationException {
        FactoryBase.throwErrorIfDepositIsMinus(initialDeposit);
        StudentAccount account = new StudentAccount(userName, initialDeposit, newAccountPassword);
        FactoryBase.throwErrorIfAccountExists(account.getAccountId());
        return account;
    }
}
