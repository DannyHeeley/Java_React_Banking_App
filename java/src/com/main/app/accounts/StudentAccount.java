package com.main.app.accounts;

import com.main.app.Bank;
import com.main.app.database.DatabaseService;

import java.time.LocalDate;

import static com.main.app.accounts.AccountType.ADULT;
import static com.main.app.accounts.AccountType.STUDENT;
import static com.main.app.transactions.TransactionType.DEPOSIT;

public class StudentAccount extends AccountBase {
    private final Integer accountLimit;

    public StudentAccount(
            String userName,
            Float initialDeposit,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String email
    ) {
        super(
                userName,
                initialDeposit,
                firstName,
                lastName,
                dateOfBirth,
                email
        );
        this.accountLimit = 5000;
        System.out.println("Bank Account created successfully for userName: " + userName + " - initial deposit of: " + initialDeposit + ".");
    }

    @Override
    public void deposit(Float amount) {
        handleNegativeArgument(DEPOSIT, amount);
        if (getBalance() + amount <= accountLimit) {
            addToAccountBalance(amount);
            setAccountUpdatedTo(getDateTimeNowAsString());
            Bank.getInstance().updateBalanceDeposit(amount);
            System.out.println("Deposit of " + amount + " was successful! Your new balance is " + getBalance());
        } else {
            throw new IllegalArgumentException("Deposit failed. Your deposit of " + amount + " would take you over your account limit of " + accountLimit);
        }
    }
}
