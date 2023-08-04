package com.main.app.accounts;

import com.main.app.Bank;
import com.main.app.database.DatabaseService;

import java.time.LocalDate;

import static com.main.app.Login.PasswordService.hashPassword;
import static com.main.app.transactions.TransactionType.DEPOSIT;

public class Employee extends AccountBase {

    private AccountType position;
    private final int employeeId;

    public Employee(
            AccountType position, String userName, String firstName,
            String lastName, LocalDate dateOfBirth, String email,
            String newAccountPassword
    ) {
        super(
                userName, 0f, firstName,
                lastName, dateOfBirth, email,
                position, newAccountPassword);
        this.position = position;
        this.employeeId = DatabaseService.addEmployeeEntryToDatabase(
                position,
                this
        );
    }

    public AccountType getPosition() {
        return position;
    }

    public void setPosition(AccountType position) {
        this.position = position;
    }

    @Override
    public void deposit(Float amount) {
        handleNegativeArgument(DEPOSIT, amount);
        addToAccountBalance(amount);
        setAccountUpdatedTo(getDateTimeNowAsString());
        Bank.getInstance().updateMainBankBalanceDeposit(amount);
        System.out.println("Deposit of " + amount + " was successful! Your new balance is " + getAccountBalance());
    }

    public int getEmployeeId() {
        return employeeId;
    }
}
