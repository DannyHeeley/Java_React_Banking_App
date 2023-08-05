package com.main.app.accounts;

import com.main.app.Bank;
import com.main.app.entities.Customer;

import java.time.LocalDate;

import static com.main.app.accounts.AccountType.ADULT;
import static com.main.app.transactions.TransactionType.DEPOSIT;

public class AdultAccount extends AccountBase {

    public AdultAccount(
            Customer customer,
            String userName,
            Float initialDeposit,
            String newAccountPassword
    ) {
        super(
                userName,
                initialDeposit,
                ADULT,
                newAccountPassword,
                customer
                );
        System.out.println("Bank Account created successfully for customer: " + userName + " - initial deposit of: " + initialDeposit + ".");
    }

    @Override
    public void deposit(Float amount) {
        handleNegativeArgument(DEPOSIT, amount);
        AccountManager.getInstance().addToAccountBalance(this, amount);
        getTransactions().addTransaction(this, DEPOSIT, amount, getAccountId());
        setAccountUpdatedTo(LocalDate.now());
        Bank.getInstance().updateMainBankBalanceDeposit(amount);
        System.out.println("Deposit of " + amount + " was successful! Your new balance is " + getAccountBalance());
    }
}
