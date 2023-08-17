package com.app.core.accounts;

import com.app.services.AccountManager;

import static com.app.core.FactoryBase.AccountType.ADULT;

public class AdultAccount extends AccountBase {

    public AdultAccount(String userName, Float balance, String passwordHash) {
        super(ADULT, balance, passwordHash, userName);
    }

    @Override
    public void deposit(float amount) {
        AccountManager.getInstance().addToAccountBalance(this, amount);
        System.out.println(
                "Deposit of " + amount + " was successful! Your new balance is " + getAccountBalance()
        );
    }
}
