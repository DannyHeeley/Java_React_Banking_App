package com.app.core.accounts;

import com.app.services.AccountManager;

import static com.app.core.FactoryBase.AccountType.STUDENT;

public class StudentAccount extends AccountBase {
    private final Integer accountLimit;

    public StudentAccount(String userName, float balance, String passwordHash) {
        super(STUDENT, balance, passwordHash, userName);
        this.accountLimit = 5000;
    }

    @Override
    public void deposit(float amount) {
        if (getAccountBalance() + amount <= accountLimit) {
            AccountManager.getInstance().addToAccountBalance(this, amount);
            System.out.println(
                    "Deposit of " + amount + " was successful! Your new balance is "
                            + getAccountBalance());
        } else {
            throw new IllegalArgumentException(
                    "Deposit failed. Your deposit of " + amount
                            + " would take you over your account limit of " + accountLimit
            );
        }
    }
}
