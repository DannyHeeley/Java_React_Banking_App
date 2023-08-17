package com.app.core.accounts;

import com.app.core.EntityFactory;
import com.app.core.FactoryBase;
import com.app.services.AccountManager;
import com.app.core.users.Customer;

import java.time.LocalDate;

public class AccountFactory extends FactoryBase {

    private AccountFactory() {
    }

    public static AccountBase newAdultAccount(
            String userName,
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
            AccountManager.getInstance().addAccount(customer, adultAccount, initialDeposit);
        } catch (AccountCreationException e) {
            adultAccount = null;
            System.out.println("Error Creating Acccount: " + e.getMessage());
        }
        System.out.println("Bank Account created successfully for customer: "
                        + userName + " - initial deposit of: " + initialDeposit + "."
        );
        return adultAccount;
    }

    public static AccountBase newStudentAccount(
            String userName,
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
            AccountManager.getInstance().addAccount(customer, studentAccount, initialDeposit);
        } catch (AccountCreationException e) {
            studentAccount = null;
            System.out.println("Error Creating Acccount: " + e.getMessage());
        }
        System.out.println("Bank Account created successfully for customer: " + userName + " - initial deposit of: " + initialDeposit + "."
        );
        return studentAccount;
    }


}
