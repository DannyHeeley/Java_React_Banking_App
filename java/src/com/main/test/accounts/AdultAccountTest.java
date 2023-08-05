package com.main.test.accounts;

import com.main.app.accounts.AccountManager;
import com.main.app.accounts.AdultAccount;
import com.main.app.accounts.PersonalInformation;
import com.main.app.entities.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.main.app.accounts.AccountType.ADULT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdultAccountTest {

    Customer customer;
    String userName = "Adult";
    String newAccountPassword = "Password123!";
    PersonalInformation personalInformation = new PersonalInformation(
            "Fizz", "Buzz", LocalDate.of(1993, 1, 11), "fizzbuzz@gmail.com"
    );

    @BeforeEach
    void setUp() {
        customer = new Customer(ADULT, personalInformation);
    }


    @AfterEach
    void tearDown() {
        AccountManager.clearBankAccountList();
        customer = null;
    }

    // Deposit tests:
    @Test
    void depositIncreasesAccountBalance() {
        AdultAccount adultAccount = new AdultAccount(customer, userName, 0f, newAccountPassword);
        adultAccount.deposit(100f);
        assertThat(adultAccount.getAccountBalance()).isEqualTo(100f);
    }
    @Test
    void depositOfZeroDoesNotChangeBalance() {
        AdultAccount adultAccount = new AdultAccount(customer, userName, 100f, newAccountPassword);
        adultAccount.deposit(0f);
        assertThat(adultAccount.getAccountBalance()).isEqualTo(100f);
    }
    @Test
    void depositWithMinusValueThrowsException() {
        AdultAccount adultAccount1 = new AdultAccount(customer, userName, 0f, newAccountPassword);
        assertThrows(RuntimeException.class, () -> adultAccount1.deposit(-100f));

        AdultAccount adultAccount2 = new AdultAccount(customer, "Another Adult", 100f, newAccountPassword);
        assertThrows(RuntimeException.class, () -> adultAccount2.deposit(-100f));
    }

    // Withdraw tests:
    @Test
    void withdrawalDecreasesBalance() {
        AdultAccount adultAccount = new AdultAccount(customer, userName, 100f, newAccountPassword);
        adultAccount.withdraw(25f);
        assertThat(adultAccount.getAccountBalance()).isEqualTo(75f);
    }
    @Test
    void withdrawalOfZeroDoesNotChangeBalance() {
        AdultAccount adultAccount = new AdultAccount(customer, userName, 100f, newAccountPassword);
        adultAccount.withdraw(0f);
        assertThat(adultAccount.getAccountBalance()).isEqualTo(100f);
    }
    @Test
    void withdrawalWithMinusValueThrowsException() {
        AdultAccount adultAccount1 = new AdultAccount(customer, userName, 0f, newAccountPassword);
        assertThrows(RuntimeException.class, () -> adultAccount1.withdraw(-100f));

        AdultAccount adultAccount2 = new AdultAccount(customer, "Another Adult", 100f, newAccountPassword);
        assertThrows(RuntimeException.class, () -> adultAccount2.withdraw(-100f));
    }

    // Date tests:
    @Test
    void getsDateAccountLastUpdated() {
        AdultAccount adultAccount = new AdultAccount(customer, userName, 100f, newAccountPassword);
        adultAccount.deposit(100f);
        assertThat(adultAccount.getDateAccountLastUpdated()).isNotNull();
    }

    @Test
    void getsAccountCreationDate() {
        AdultAccount adultAccount = new AdultAccount(customer, userName, 100f, newAccountPassword);
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