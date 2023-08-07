package com.main.app.accounts;

import com.main.app.FactoryBase;
import com.main.app.entities.Customer;
import com.main.app.entities.Person;
import com.main.app.wiring.CustomerDAO;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Objects;

import static com.main.app.accounts.AccountType.ADULT;
import static com.main.app.accounts.AccountType.STUDENT;
import static com.main.app.accounts.PasswordService.hashPassword;
import static com.main.app.entities.EntityType.CUSTOMER;

public class AccountFactory extends FactoryBase {

    private AccountFactory() {
    }

    public static AccountBase createAccountNewUser(
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
            // Create person, set id, and save to database
            person = createNewPerson(pi);
            // Create a customer, set id's,  save to database
            customer = createNewCustomer(pi, person);
            // Create account, set id's, save to database
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
        // Add account to the customer
        customer.addAccount(account);
        AccountManager.getInstance().addAccount(customer, account, accountType, initialDeposit, passwordHash, person);
        return account;
    }

    private static Customer createNewCustomer(PersonalInformation pi, Person person) {
        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = new Customer(CUSTOMER, pi);
        customer.setCustomerId(customerDAO.saveCustomer(CUSTOMER, person));
        customer.setPersonId(person.getPersonId());
        return customer;
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
        throwErrorIfAccountExists(userName);
        throwErrorIfDepositIsMinus(initialDeposit);
        return new AdultAccount(
                userName, initialDeposit, newAccountPassword
        );
    }

    private static StudentAccount studentAccount(
            String userName, Float initialDeposit, String newAccountPassword
    ) throws AccountCreationException {
        FactoryBase.throwErrorIfAccountExists(userName);
        FactoryBase.throwErrorIfDepositIsMinus(initialDeposit);
        return new StudentAccount(
                userName, initialDeposit, newAccountPassword
        );
    }
}
