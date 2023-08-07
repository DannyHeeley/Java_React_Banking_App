package com.main.app.entities;

import com.main.app.accounts.AccountBase;
import com.main.app.accounts.PersonalInformation;

import java.util.ArrayList;

public class Customer extends Person {
    private ArrayList<AccountBase> accounts;
    private EntityType customerType;
    private int customerId;

    private int personId;

    public Customer(EntityType customerType, PersonalInformation personalInformation) {
        super(personalInformation);
        this.customerType = customerType;
        this.accounts = new ArrayList<>();
        this.customerId = -1;
        this.personId = -1;
    }

    public EntityType getAccountType() {
        return customerType;
    }

    public void setAccountType(EntityType accountType) {
        this.customerType = accountType;
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
    public void setCustomerId(int newCustomerId) {
        this.customerId = newCustomerId;
    }

    @Override
    public int getPersonId() {
        return personId;
    }

    @Override
    public void setPersonId(int personId) {
        this.personId = personId;
    }
}
