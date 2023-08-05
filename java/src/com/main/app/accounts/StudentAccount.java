package com.main.app.accounts;

import com.main.app.Bank;
import com.main.app.entities.Customer;

import static com.main.app.accounts.AccountType.STUDENT;
import static com.main.app.transactions.TransactionType.DEPOSIT;

public class StudentAccount extends AccountBase {
    private final Integer accountLimit;

    public StudentAccount(
            Customer customer, String userName,
            Float initialDeposit,
            String newAccountPassword
    ) {
        super(
                userName,
                initialDeposit,
                STUDENT,
                newAccountPassword,
                customer
                );
        this.accountLimit = 5000;
        System.out.println("Bank Account created successfully for userName: " + userName + " - initial deposit of: " + initialDeposit + ".");
    }

    @Override
    public void deposit(Float amount) {
        handleNegativeArgument(DEPOSIT, amount);
        if (getAccountBalance() + amount <= accountLimit) {
            addToAccountBalance(amount);
            setAccountUpdatedTo(getDateTimeNowAsString());
            Bank.getInstance().updateMainBankBalanceDeposit(amount);
            System.out.println("Deposit of " + amount + " was successful! Your new balance is " + getAccountBalance());
        } else {
            throw new IllegalArgumentException("Deposit failed. Your deposit of " + amount + " would take you over your account limit of " + accountLimit);
        }
    }
}
