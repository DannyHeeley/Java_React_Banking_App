package com.main.app.accounts;

import com.main.app.Bank;

public class StudentAccount extends AccountBase {
    private final Integer accountLimit;

    public StudentAccount(String userName, Float initialDeposit) {
        super(userName, initialDeposit);
        this.accountLimit = 5000;
        System.out.println("Bank Account created successfully for userName: " + userName + " - initial deposit of: " + initialDeposit + ".");
    }

    @Override
    public void deposit(Float amount) {
        if (getBalance() + amount <= accountLimit) {
            addBalance(amount);
            setAccountUpdated(getDateTimeNowAsString());
            Bank.getInstance().updateBalanceDeposit(amount);
            System.out.println("Deposit of " + amount + " was successful! Your new balance is " + getBalance());
        } else {
            System.out.println("Deposit failed. Your deposit of " + amount + " would take you over your account limit of " + accountLimit);
            System.out.println("Your balance is: " + getBalance());
        }
    }

}