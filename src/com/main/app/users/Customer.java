package com.main.app.users;

import com.main.app.core.FactoryBase;
import com.main.app.accounts.AccountBase;

import java.util.ArrayList;

public class Customer extends Person {
    private ArrayList<AccountBase> accounts;
    private FactoryBase.UserType userType;
    private int customerId;
    private String userName;

    public Customer(FactoryBase.UserType userType, PersonalInformation personalInformation, String userName) {
        super(personalInformation);
        this.userType = userType;
        this.accounts = new ArrayList<>();
        this.customerId = -1;
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
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
