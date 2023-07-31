package com.main.app;

import com.main.app.Login.PasswordService;
import com.main.app.accounts.*;

import static com.main.app.accounts.AccountType.*;

public class Main {
    public static void main(String[] args) {
        Bank bank = Bank.getInstance();

        // Demo adult account
        AdultAccount testAccountAdult = (AdultAccount) BankAccountFactory.createAccount(ADULT,"Foo Bar", "password123", 1000f);
        testAccountAdult.deposit(500f);
        System.out.println("");
        testAccountAdult.printAccountInfo();
        System.out.println("");

        // Demo student account
        StudentAccount testAccountStudent = (StudentAccount) BankAccountFactory.createAccount(STUDENT,"Fizz Buzz", "password123", 100f);
        testAccountStudent.deposit(100f);
        System.out.println("");
        testAccountStudent.printAccountInfo();
        System.out.println("");

        // Demo for passwords
        System.out.println("---- Getting account password ----");
        String password = "password_here";
        PasswordService.setPasswordHashForAccount(testAccountAdult.getAccountNumber(), password);
        System.out.println("Hashed password for account " + testAccountAdult.getUserName() + ": " + testAccountAdult.getAccountPasswordHash());
        System.out.println("---- Verifying account password ----");
        boolean isCorrectPassword = PasswordService.authenticateUserPassword(testAccountAdult.getAccountPasswordHash(), password);
        System.out.println("Your password is " + isCorrectPassword);
        System.out.println("");

        bank.printBankInfo();
    }
}
