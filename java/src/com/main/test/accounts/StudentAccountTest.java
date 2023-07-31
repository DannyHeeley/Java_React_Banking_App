package com.main.test.accounts;

import com.main.app.accounts.AccountManager;
import com.main.app.accounts.StudentAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentAccountTest {

    @AfterEach
    void tearDown() {
        AccountManager.clearBankAccountList();
    }

    // Deposit tests:
    @Test
    void depositIncreasesAccountBalance() {
        StudentAccount studentAccount = new StudentAccount("A Student", 0f);
        studentAccount.deposit(100f);
        assertThat(studentAccount.getBalance()).isEqualTo(100f);
    }
    @Test
    void depositOfZeroDoesNotChangeBalance() {
        StudentAccount studentAccount = new StudentAccount("A Student", 100f);
        studentAccount.deposit(0f);
        assertThat(studentAccount.getBalance()).isEqualTo(100f);
    }
    @Test
    void depositWithMinusValueThrowsException() {
        StudentAccount studentAccount1 = new StudentAccount("A Student", 0f);
        assertThrows(RuntimeException.class, () -> studentAccount1.deposit(-100f));

        StudentAccount studentAccount2 = new StudentAccount("Another Adult", 100f);
        assertThrows(RuntimeException.class, () -> studentAccount2.deposit(-100f));
    }

    // Withdraw tests:
    @Test
    void withdrawalDecreasesBalance() {
        StudentAccount studentAccount = new StudentAccount("A Student", 100f);
        studentAccount.withdraw(25f);
        assertThat(studentAccount.getBalance()).isEqualTo(75f);
    }
    @Test
    void withdrawalOfZeroDoesNotChangeBalance() {
        StudentAccount studentAccount = new StudentAccount("A Student", 100f);
        studentAccount.withdraw(0f);
        assertThat(studentAccount.getBalance()).isEqualTo(100f);
    }
    @Test
    void withdrawalWithMinusValueThrowsException() {
        StudentAccount studentAccount1 = new StudentAccount("A Student", 0f);
        assertThrows(RuntimeException.class, () -> studentAccount1.withdraw(-100f));

        StudentAccount studentAccount2 = new StudentAccount("Another Adult", 100f);
        assertThrows(RuntimeException.class, () -> studentAccount2.withdraw(-100f));
    }

    // Date tests:
    @Test
    void getsDateAccountLastUpdated() {
        StudentAccount studentAccount = new StudentAccount("A Student", 100f);
        studentAccount.deposit(100f);
        assertThat(studentAccount.getDateAccountLastUpdated()).isNotNull();
    }

    @Test
    void getsAccountCreationDate() {
        StudentAccount studentAccount = new StudentAccount("A Student", 100f);
        assertThat(studentAccount.getDateAccountCreated()).isNotNull();
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