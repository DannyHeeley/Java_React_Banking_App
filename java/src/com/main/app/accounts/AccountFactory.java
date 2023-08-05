package com.main.app.accounts;

import com.main.app.Bank;
import com.main.app.FactoryBase;
import com.main.app.database.DatabaseService;
import com.main.app.entities.Customer;

import java.time.LocalDate;
import java.util.Objects;

import static com.main.app.accounts.AccountType.*;
import static com.main.app.login.PasswordService.hashPassword;

public class AccountFactory extends FactoryBase implements DatabaseService {

    private AccountFactory() {
    }

    public static AccountBase createAccount(
            AccountType accountType, String userName,
            String newAccountPassword, Float initialDeposit,
            String firstName, String lastName,
            LocalDate dateOfBirth, String email
    ) {
        AccountBase account = null;
        String passwordHash;
        Customer customer;
        try {
            passwordHash = hashPassword(newAccountPassword);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            throw(e);
        }
        try {
            customer = createNewCustomer(accountType, firstName, lastName, dateOfBirth, email);
            customer.addAccount(account);
            account = handleCreatingAccountForType(
                    accountType,
                    userName,
                    initialDeposit,
                    passwordHash,
                    customer
            );
            AccountManager.getInstance().addAccount(account);
            Bank.getInstance().updateMainBankBalanceDeposit(initialDeposit);
        } catch (AccountCreationException e) {
            System.out.println("Error Creating Acccount: " + e.getMessage());
        }
        return account;
    }

    private static Customer createNewCustomer(
            AccountType accountType, String firstName,
            String lastName, LocalDate dateOfBirth, String email
    ) {
        PersonalInformation personalInformation = new PersonalInformation(
                firstName, lastName, dateOfBirth, email
        );
        return new Customer(accountType, personalInformation);
    }

    private static AccountBase handleCreatingAccountForType(
            AccountType accountType, String userName, Float initialDeposit,
            String newAccountPassword, Customer customer
    ) throws AccountCreationException {
        if (Objects.equals(accountType, STUDENT)) {
            return studentAccount(customer, userName, initialDeposit, newAccountPassword);
        } else if (Objects.equals(accountType, ADULT)) {
            return adultAccount(customer, userName, initialDeposit, newAccountPassword);
        }
        throw new AccountCreationException("Account type must be valid");
    }

    private static AdultAccount adultAccount(
            Customer customer, String userName, Float initialDeposit, String newAccountPassword
    ) throws AccountCreationException {
        throwErrorIfAccountExists(userName);
        throwErrorIfDepositIsMinus(initialDeposit);
        return new AdultAccount(
                customer, userName, initialDeposit, newAccountPassword
        );
    }

    private static StudentAccount studentAccount(
            Customer customer, String userName, Float initialDeposit, String newAccountPassword
    ) throws AccountCreationException {
        FactoryBase.throwErrorIfAccountExists(userName);
        FactoryBase.throwErrorIfDepositIsMinus(initialDeposit);
        return new StudentAccount(
                customer, userName, initialDeposit, newAccountPassword
        );
    }
}
