package com.main.app.accounts;

import com.main.app.Bank;

import java.time.LocalDate;

import static com.main.app.FactoryBase.AccountType.STUDENT;
import static com.main.app.transactions.TransactionType.DEPOSIT;

public class StudentAccount extends AccountBase {
    private final Integer accountLimit;

    public StudentAccount(String userName, Float balance, String passwordHash) {
        super(STUDENT, balance, passwordHash);
        this.accountLimit = 5000;
        System.out.println(
                "Bank Account created successfully for userName: "
                        + userName + " - initial deposit of: " + balance + "."
        );
    }

    @Override
    public void deposit(Float amount) {
        AccountManager.getInstance().handleNegativeArgument(DEPOSIT, amount);
        if (getAccountBalance() + amount <= accountLimit) {
            AccountManager.getInstance().addToAccountBalance(this, amount);
            setAccountUpdated(LocalDate.now());
            Bank.getInstance().updateBankDeposit(amount);
            System.out.println(
                    "Deposit of " + amount
                            + " was successful! Your new balance is " + getAccountBalance());
        } else {
            throw new IllegalArgumentException(
                    "Deposit failed. Your deposit of " + amount
                            + " would take you over your account limit of " + accountLimit
            );
        }
    }
}
