package com.main.app;

import com.main.app.Login.PasswordService;
import com.main.app.accounts.AdultAccount;
import com.main.app.accounts.AccountFactory;
import com.main.app.accounts.StudentAccount;

import java.time.LocalDate;

import static com.main.app.accounts.AccountType.ADULT;
import static com.main.app.accounts.AccountType.STUDENT;

public class Main {
    public static void main(String[] args) {
        Bank bank = Bank.getInstance();
        String userNameAdult = "Adult";
        String userNameStudent = "Student";
        String firstName = "Fizz";
        String lastName = "Buzz";
        LocalDate dateOfBirth = LocalDate.of(1993, 1, 11);
        String email = "fizzbuzz@gmail.com";

        // Demo adult account
        AdultAccount testAccountAdult = (AdultAccount) AccountFactory.createAccount(
                ADULT, userNameAdult, "Password123!", 1000f, firstName, lastName, dateOfBirth, email
        );
        testAccountAdult.deposit(500f);
        System.out.println("");
        testAccountAdult.printAccountInfo();
        System.out.println("");

        // Demo student account
        StudentAccount testAccountStudent = (StudentAccount) AccountFactory.createAccount(
                STUDENT,userNameStudent, "Password123!", 100f, firstName, lastName, dateOfBirth, email
        );
        testAccountStudent.deposit(100f);
        System.out.println("");
        testAccountStudent.printAccountInfo();
        System.out.println("");

        // Demo for passwords
        System.out.println("---- Enter password ----");
        String password = "Password123!";
        System.out.println("Password entered: " + password);
        System.out.println("---- Getting password hash for account ----");
        System.out.println("Hashed password for account " + testAccountAdult.getUserName() + ": " + testAccountAdult.getAccountPasswordHash());
        System.out.println("---- Verifying account password ----");
        boolean isCorrectPassword = PasswordService.authenticateUserPassword(testAccountAdult, password);
        System.out.println("Your password is " + isCorrectPassword);
        System.out.println("");

        bank.printBankInfo();
        System.out.println("");

    }
}
