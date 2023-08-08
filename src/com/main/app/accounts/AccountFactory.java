package com.main.app.accounts;

import com.main.app.core.FactoryBase;
import com.main.app.users.Customer;
import com.main.app.core.EntityFactory;

import java.time.LocalDate;

public class AccountFactory extends FactoryBase {

    private AccountFactory() {
    }

    public static AccountBase newAdultAccount(
            AccountType accountType, String userName,
            String newAccountPassword, Float initialDeposit,
            String firstName, String lastName,
            LocalDate dateOfBirth, String email
    ){
        AccountBase adultAccount;
        Customer customer;
        String passwordHash;
        try {
            throwExceptionIfDepositIsMinus(initialDeposit);
            throwExceptionIfAccountExists(userName);
            passwordHash = throwExceptionIfPasswordFormatWrong(newAccountPassword);
            adultAccount = new AdultAccount(userName, initialDeposit, passwordHash);
            // Creates person here too
            customer = EntityFactory.newCustomer(userName, firstName, lastName, dateOfBirth, email);
            // Set id's and add account to db and account list
            AccountManager.getInstance().addAccount(customer, adultAccount, accountType, initialDeposit, passwordHash);
        } catch (AccountCreationException e) {
            adultAccount = null;
            System.out.println("Error Creating Acccount: " + e.getMessage());
        }
        return adultAccount;
    }

    public static AccountBase newStudentAccount(
            AccountType accountType, String userName,
            String newAccountPassword, Float initialDeposit,
            String firstName, String lastName,
            LocalDate dateOfBirth, String email
    ){
        AccountBase studentAccount;
        Customer customer;
        String passwordHash;
        try {
            throwExceptionIfDepositIsMinus(initialDeposit);
            throwExceptionIfAccountExists(userName);
            passwordHash = throwExceptionIfPasswordFormatWrong(newAccountPassword);
            studentAccount = new StudentAccount(userName, initialDeposit, passwordHash);
            // Creates person here too
            customer = EntityFactory.newCustomer(userName, firstName, lastName, dateOfBirth, email);
            // Set id's and add account to db and account list
            AccountManager.getInstance().addAccount(customer, studentAccount, accountType, initialDeposit, passwordHash);
        } catch (AccountCreationException e) {
            studentAccount = null;
            System.out.println("Error Creating Acccount: " + e.getMessage());
        }
        return studentAccount;
    }


}
