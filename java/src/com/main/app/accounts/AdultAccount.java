package com.main.app.accounts;

import com.main.app.Bank;
import com.main.app.database.DatabaseService;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.main.app.transactions.TransactionType.DEPOSIT;

public class AdultAccount extends AccountBase {

    public AdultAccount(
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
        System.out.println("Bank Account created successfully for customer: " + userName + " - initial deposit of: " + initialDeposit + ".");
    }

    @Override
    public void deposit(Float amount) {
        handleNegativeArgument(DEPOSIT, amount);
        addToAccountBalance(amount);
        DatabaseService.updateAccountBalanceInDatabase(getAccountBalance());
        setAccountUpdatedTo(getDateTimeNowAsString());
        Bank.getInstance().updateMainBankBalanceDeposit(amount);
        System.out.println("Deposit of " + amount + " was successful! Your new balance is " + getAccountBalance());
    }
}
