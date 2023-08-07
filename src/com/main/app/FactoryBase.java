package com.main.app;

import com.main.app.accounts.AccountManager;
import com.main.app.accounts.PersonalInformation;
import com.main.app.entities.Person;
import com.main.app.wiring.PersonDAO;

public class FactoryBase {

    public static Person createNewPerson(PersonalInformation pi) {
        PersonDAO personDAO = new PersonDAO();
        Person person = new Person(pi);
        person.setPersonId(personDAO.saveNew(pi.getFirstName(), pi.getLastName(), pi.getDateOfBirth(), pi.getEmail()));
        return person;
    }

    public static void throwErrorIfAccountExists(int accountId)
            throws AccountCreationException {
        if (AccountManager.getInstance().accountExists(accountId)) {
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

    public enum EntityType {
        CUSTOMER,
        EMPLOYEE,
        ADMINISTRATOR,
    }

    public enum AccountType {
        ADULT,
        STUDENT,
    }
}
