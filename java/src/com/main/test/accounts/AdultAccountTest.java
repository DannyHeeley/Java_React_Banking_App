package com.main.test.accounts;

import com.main.app.accounts.AccountManager;
import com.main.app.accounts.AdultAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdultAccountTest {

    @AfterEach
    void tearDown() {
        AccountManager.clearBankAccountList();
    }

    // Deposit tests:
    @Test
    void depositIncreasesAccountBalance() {
        AdultAccount adultAccount = new AdultAccount("An Adult", 0f);
        adultAccount.deposit(100f);
        assertThat(adultAccount.getBalance()).isEqualTo(100f);
    }
    @Test
    void depositOfZeroDoesNotChangeBalance() {
        AdultAccount adultAccount = new AdultAccount("An Adult", 100f);
        adultAccount.deposit(0f);
        assertThat(adultAccount.getBalance()).isEqualTo(100f);
    }
    @Test
    void depositWithMinusValueThrowsException() {
        AdultAccount adultAccount1 = new AdultAccount("An Adult", 0f);
        assertThrows(RuntimeException.class, () -> adultAccount1.deposit(-100f));

        AdultAccount adultAccount2 = new AdultAccount("Another Adult", 100f);
        assertThrows(RuntimeException.class, () -> adultAccount2.deposit(-100f));
    }

    // Withdraw tests:
    @Test
    void withdrawalDecreasesBalance() {
        AdultAccount adultAccount = new AdultAccount("An Adult", 100f);
        adultAccount.withdraw(25f);
        assertThat(adultAccount.getBalance()).isEqualTo(75f);
    }
    @Test
    void withdrawalOfZeroDoesNotChangeBalance() {
        AdultAccount adultAccount = new AdultAccount("An Adult", 100f);
        adultAccount.withdraw(0f);
        assertThat(adultAccount.getBalance()).isEqualTo(100f);
    }
    @Test
    void withdrawalWithMinusValueThrowsException() {
        AdultAccount adultAccount1 = new AdultAccount("An Adult", 0f);
        assertThrows(RuntimeException.class, () -> adultAccount1.withdraw(-100f));

        AdultAccount adultAccount2 = new AdultAccount("Another Adult", 100f);
        assertThrows(RuntimeException.class, () -> adultAccount2.withdraw(-100f));
    }

    // Date tests:
    @Test
    void getsDateAccountLastUpdated() {
        AdultAccount adultAccount = new AdultAccount("An Adult", 100f);
        adultAccount.deposit(100f);
        assertThat(adultAccount.getDateAccountLastUpdated()).isNotNull();
    }

    @Test
    void getsAccountCreationDate() {
        AdultAccount adultAccount = new AdultAccount("An Adult", 100f);
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