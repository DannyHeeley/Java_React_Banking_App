package com.main.app.accounts;

import com.main.app.Bank;

public class AdultAccount extends AccountBase {
    public AdultAccount(String userName, Float initialDeposit) {
        super(userName, initialDeposit);
        System.out.println("Bank Account created successfully for customer: " + userName + " - initial deposit of: " + initialDeposit + ".");
    }
    @Override
    public void deposit(Float amount) {
        if (amount >= 0) {
            addToAccountBalance(amount);
            setAccountUpdatedTo(getDateTimeNowAsString());
            Bank.getInstance().updateBalanceDeposit(amount);
            System.out.println("Deposit of " + amount + " was successful! Your new balance is " + getBalance());
        }else {
            throw new RuntimeException("Deposit amount must be a positive number");
        }
    }
}
