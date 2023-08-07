package com.main.app.entities;

import com.main.app.FactoryBase;
import com.main.app.accounts.AccountBase;
import com.main.app.accounts.PersonalInformation;

import java.util.ArrayList;

public class Customer extends Person {
    private ArrayList<AccountBase> accounts;
    private FactoryBase.EntityType customerType;
    private int customerId;
    private String userName;
    private int personId;
    public Customer(FactoryBase.EntityType customerType, PersonalInformation personalInformation, String userName) {
        super(personalInformation);
        this.customerType = customerType;
        this.accounts = new ArrayList<>();
        this.customerId = -1;
        this.personId = -1;
        this.userName = userName;
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
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
