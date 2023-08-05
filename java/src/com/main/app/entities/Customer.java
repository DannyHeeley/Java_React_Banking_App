package com.main.app.entities;

import com.main.app.accounts.AccountBase;
import com.main.app.accounts.AccountType;
import com.main.app.accounts.PersonalInformation;
import com.main.app.database.DatabaseService;

import java.util.ArrayList;

public class Customer extends Person {
    private ArrayList<AccountBase> accounts;
    private AccountType accountType;
    private final int customerId;

    public Customer(AccountType accountType, PersonalInformation personalInformation) {
        super(personalInformation);
        this.accountType = accountType;
        this.accounts = new ArrayList<>();
        this.customerId = DatabaseService.addCustomerEntryToDatabase(accountType, this);
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public ArrayList<AccountBase> getAccounts() {
        return accounts;
    }

    public void addAccount(AccountBase anAccount) {
        this.accounts.add(anAccount);
    }

    public int getCustomerId() {
        return customerId;
    }

}
