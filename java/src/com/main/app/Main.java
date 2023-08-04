package com.main.app;

import com.main.app.Login.PasswordService;
import com.main.app.accounts.AdultAccount;
import com.main.app.accounts.AccountFactory;
import com.main.app.accounts.StudentAccount;
import com.main.app.entities.EntityFactory;

import java.time.LocalDate;

import static com.main.app.accounts.AccountType.*;
import static com.main.app.entities.EntityType.ADMINISTRATOR;
import static com.main.app.entities.EntityType.EMPLOYEE;

public class Main {
    public static void main(String[] args) {
        Bank bank = Bank.getInstance();
        String userNameAdult = "Adult";
        String userNameStudent = "Student";
        String password = "Password123!";
        String firstName = "Fizz";
        String lastName = "Buzz";
        LocalDate dateOfBirth = LocalDate.of(1993, 1, 11);
        String emailAdult = "fizzbuzz@gmail.com";
        String emailStudent = "foobar@gmail.com";
        String emailEmployee = "jordanbelfort@gmail.com";

        // Demo adult account
        AdultAccount testAccountAdult = (AdultAccount) AccountFactory.createAccount(
                ADULT,
                userNameAdult,
                "Password123!",
                1000f,
                firstName,
                lastName,
                dateOfBirth,
                emailAdult
        );
        testAccountAdult.deposit(500f);
        System.out.println("");
        testAccountAdult.printAccountInfo();
        System.out.println("");

        // Demo student account
        StudentAccount testAccountStudent = (StudentAccount) AccountFactory.createAccount(
                STUDENT,
                userNameStudent,
                password,
                100f,
                "Foo",
                "Bar",
                dateOfBirth,
                emailStudent
        );
        testAccountStudent.deposit(100f);
        System.out.println("");
        testAccountStudent.printAccountInfo();
        System.out.println("");

        // Demo employee account
        EntityFactory.createEntityForType(
                EMPLOYEE,
                "Hunter.S",
                "Thompsan",
                dateOfBirth,
                emailEmployee
        );
        EntityFactory.createEntityForType(
                ADMINISTRATOR,
                "Jordan",
                "Bellfort",
                dateOfBirth,
                emailEmployee
        );

        // Demo for passwords
        System.out.println("---- Enter password ----");
        System.out.println("Password entered: " + password);
        System.out.println("---- Getting password hash for account ----");
        System.out.println("Hashed password for account " + testAccountAdult.getUserName() + ": " + testAccountAdult.getAccountPasswordHash());
        System.out.println("---- Verifying account password ----");
        boolean isCorrectPassword = PasswordService.authenticateUserPassword(testAccountAdult, password);
        System.out.println("Your password is " + isCorrectPassword);
        System.out.println("");

        bank.printBankInfo();
    }
}
