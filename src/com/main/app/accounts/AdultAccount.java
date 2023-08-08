package com.main.app.accounts;

import com.main.app.core.Bank;

import java.time.LocalDate;

import static com.main.app.core.FactoryBase.AccountType.ADULT;
import static com.main.app.transactions.TransactionType.DEPOSIT;

public class AdultAccount extends AccountBase {

    public AdultAccount(String userName, Float balance, String passwordHash) {
        super(ADULT, balance, passwordHash);
        System.out.println(
                "Bank Account created successfully for customer: "
                        + userName + " - initial deposit of: " + balance + "."
        );
    }

    @Override
    public void deposit(Float amount) {
        AccountManager.getInstance().handleNegativeArgument(DEPOSIT, amount);
        AccountManager.getInstance().addToAccountBalance(this, amount);
        setAccountUpdated(LocalDate.now());
        Bank.getInstance().updateBankDeposit(amount);
        System.out.println(
                "Deposit of " + amount + " was successful! Your new balance is " + getAccountBalance()
        );
    }
}
