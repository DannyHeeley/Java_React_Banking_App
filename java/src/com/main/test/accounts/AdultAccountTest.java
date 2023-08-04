package com.main.test.accounts;

import com.main.app.accounts.AccountManager;
import com.main.app.accounts.AdultAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdultAccountTest {
    String userName = "Adult";
    String firstName = "Fizz";
    String lastName = "Buzz";
    LocalDate dateOfBirth = LocalDate.of(1993, 1, 11);
    String email = "fizzbuzz@gmail.com";

    String newAccountPassword = "Password123!";
    @AfterEach
    void tearDown() {
        AccountManager.clearBankAccountList();
    }

    // Deposit tests:
    @Test
    void depositIncreasesAccountBalance() {
        AdultAccount adultAccount = new AdultAccount(userName, 0f, firstName, lastName, dateOfBirth, email, newAccountPassword);
        adultAccount.deposit(100f);
        assertThat(adultAccount.getAccountBalance()).isEqualTo(100f);
    }
    @Test
    void depositOfZeroDoesNotChangeBalance() {
        AdultAccount adultAccount = new AdultAccount(userName, 100f, firstName, lastName, dateOfBirth, email, newAccountPassword);
        adultAccount.deposit(0f);
        assertThat(adultAccount.getAccountBalance()).isEqualTo(100f);
    }
    @Test
    void depositWithMinusValueThrowsException() {
        AdultAccount adultAccount1 = new AdultAccount(userName, 0f, firstName, lastName, dateOfBirth, email, newAccountPassword);
        assertThrows(RuntimeException.class, () -> adultAccount1.deposit(-100f));

        AdultAccount adultAccount2 = new AdultAccount("Another Adult", 100f, firstName, lastName, dateOfBirth, email, newAccountPassword);
        assertThrows(RuntimeException.class, () -> adultAccount2.deposit(-100f));
    }

    // Withdraw tests:
    @Test
    void withdrawalDecreasesBalance() {
        AdultAccount adultAccount = new AdultAccount(userName, 100f, firstName, lastName, dateOfBirth, email, newAccountPassword);
        adultAccount.withdraw(25f);
        assertThat(adultAccount.getAccountBalance()).isEqualTo(75f);
    }
    @Test
    void withdrawalOfZeroDoesNotChangeBalance() {
        AdultAccount adultAccount = new AdultAccount(userName, 100f, firstName, lastName, dateOfBirth, email, newAccountPassword);
        adultAccount.withdraw(0f);
        assertThat(adultAccount.getAccountBalance()).isEqualTo(100f);
    }
    @Test
    void withdrawalWithMinusValueThrowsException() {
        AdultAccount adultAccount1 = new AdultAccount(userName, 0f, firstName, lastName, dateOfBirth, email, newAccountPassword);
        assertThrows(RuntimeException.class, () -> adultAccount1.withdraw(-100f));

        AdultAccount adultAccount2 = new AdultAccount("Another Adult", 100f, firstName, lastName, dateOfBirth, email, newAccountPassword);
        assertThrows(RuntimeException.class, () -> adultAccount2.withdraw(-100f));
    }

    // Date tests:
    @Test
    void getsDateAccountLastUpdated() {
        AdultAccount adultAccount = new AdultAccount(userName, 100f, firstName, lastName, dateOfBirth, email, newAccountPassword);
        adultAccount.deposit(100f);
        assertThat(adultAccount.getDateAccountLastUpdated()).isNotNull();
    }

    @Test
    void getsAccountCreationDate() {
        AdultAccount adultAccount = new AdultAccount(userName, 100f, firstName, lastName, dateOfBirth, email, newAccountPassword);
        assertThat(adultAccount.getDateAccountCreated()).isNotNull();
    }

    // Password tests:
    // --not written due to changing to DB for account data
    @Test
    void returnsAccountPasswordHash() {

    }

    @Test
    void setsAccountPasswordHash() {
    }

}