package com.main.app.accounts;

import com.main.app.Bank;
import static com.main.app.accounts.AccountType.STUDENT;
import static com.main.app.transactions.TransactionType.DEPOSIT;

import java.time.LocalDate;

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
                email,
                STUDENT
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
