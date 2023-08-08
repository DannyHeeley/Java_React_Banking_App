package com.main.app.core;

import com.main.app.accounts.*;
import com.main.app.services.PasswordService;

import java.time.LocalDate;

import static com.main.app.core.FactoryBase.AccountType.ADULT;
import static com.main.app.core.FactoryBase.AccountType.STUDENT;
import static com.main.app.core.FactoryBase.UserType.ADMINISTRATOR;
import static com.main.app.core.FactoryBase.UserType.EMPLOYEE;

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
        AccountBase testAccountAdult = AccountFactory.newAdultAccount(
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
        AccountManager.getInstance().printAccountInfo(testAccountAdult);
        System.out.println("");

        // Demo student account
        AccountBase testAccountStudent = AccountFactory.newStudentAccount(
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
        AccountManager.getInstance().printAccountInfo(testAccountStudent);
        System.out.println("");

        // Demo employee account
        EntityFactory.newEmployee(
                EMPLOYEE,
                "Hunter.S",
                "Thompsan",
                dateOfBirth,
                emailEmployee
        );
        EntityFactory.newEmployee(
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
        System.out.println("Hashed password for account: " + testAccountAdult.getAccountPasswordHash());
        System.out.println("---- Verifying account password ----");
        boolean isCorrectPassword = PasswordService.authenticateUserPassword(testAccountAdult, password);
        System.out.println("Your password is " + isCorrectPassword);
        System.out.println("");

        bank.printBankInfo();
    }
}
