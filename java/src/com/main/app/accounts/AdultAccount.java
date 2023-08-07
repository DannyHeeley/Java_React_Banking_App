package com.main.app.accounts;

import com.main.app.Bank;

import java.time.LocalDate;

import static com.main.app.accounts.AccountType.ADULT;
import static com.main.app.transactions.TransactionType.DEPOSIT;

public class AdultAccount extends AccountBase {

    public AdultAccount(
            String userName,
            Float initialDeposit,
            String newAccountPassword
    ) {
        super(userName,
              ADULT,
              newAccountPassword
              );
        System.out.println("Bank Account created successfully for customer: " + userName + " - initial deposit of: " + initialDeposit + ".");
    }

    @Override
    public void deposit(Float amount) {
        AccountManager.getInstance().handleNegativeArgument(DEPOSIT, amount);
        AccountManager.getInstance().addToAccountBalance(this, amount);
        setAccountUpdatedTo(LocalDate.now());
        Bank.getInstance().updateMainBankBalanceDeposit(amount);
        System.out.println("Deposit of " + amount + " was successful! Your new balance is " + getAccountBalance());
    }
}
