package com.main.app;

import com.main.app.Login.PasswordService;
import com.main.app.accounts.AccountBase;
import com.main.app.accounts.AccountManager;
import com.main.app.accounts.AdultAccount;
import com.main.app.accounts.StudentAccount;

import static com.main.app.accounts.AccountType.*;

public class Main {
    public static void main(String[] args) {
        Bank bank = Bank.getInstance();
        AccountManager accountManager = new AccountManager();

        // Demo adult account
        AdultAccount testAccountAdult = (AdultAccount) accountManager.createAccount(ADULT,"Foo Bar", "password123", 1000f);
        testAccountAdult.deposit(500f);
        System.out.println("");
        testAccountAdult.printAccountInfo();
        System.out.println("");

        // Demo student account
        StudentAccount testAccountStudent = (StudentAccount) accountManager.createAccount(STUDENT,"Fizz Buzz", "password123", 100f);
        testAccountStudent.deposit(100f);
        System.out.println("");
        testAccountStudent.printAccountInfo();
        System.out.println("");

        // Demo for passwords
        String password = "password_here";
        PasswordService.setPasswordHashForAccount(testAccountAdult.getAccountNumber(), password);
        System.out.println("Hashed password for account " + testAccountAdult.getUserName() + ": " + AccountBase.getAccountPasswordHash());
        boolean isCorrectPassword = PasswordService.authenticateUserPassword(testAccountAdult, password);
        System.out.println("Your password is " + isCorrectPassword);
        System.out.println("");

        bank.printBankInfo();
    }
}
