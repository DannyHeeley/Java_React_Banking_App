package com.main.app.entities;

import com.main.app.accounts.AccountType;
import com.main.app.database.DatabaseService;

import java.time.LocalDate;

public class Customer extends Person {

    private AccountType accountType;
    private int customerId;

    protected Customer(String firstName, String lastName, LocalDate dateOfBirth, String email, AccountType accountType) {
        super(firstName, lastName, dateOfBirth, email);
        this.accountType = accountType;
        this.customerId = DatabaseService.addCustomerEntryToDatabase(accountType, this);
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
