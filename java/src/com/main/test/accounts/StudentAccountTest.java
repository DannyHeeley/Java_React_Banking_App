package com.main.test.accounts;

import com.main.app.Bank;
import com.main.app.accounts.AccountManager;
import com.main.app.accounts.PersonalInformation;
import com.main.app.accounts.StudentAccount;
import com.main.app.entities.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.main.app.accounts.AccountType.STUDENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentAccountTest {
    Customer customer;
    String userName = "Student";
    String newAccountPassword = "Password123!";
    PersonalInformation personalInformation = new PersonalInformation(
            "Foo", "Bar", LocalDate.of(2003, 8, 21), "foobar@gmail.com"
    );

    @BeforeEach
    void setUp() {
        customer = new Customer(STUDENT, personalInformation);
    }


    @AfterEach
    void tearDown() {
        AccountManager.clearBankAccountList();
        customer = null;
        Bank.getInstance().resetBank();
    }

    // Deposit tests:
    @Test
    void depositIncreasesAccountBalance() {
        StudentAccount studentAccount = new StudentAccount(customer, userName, 0f, newAccountPassword);
        studentAccount.deposit(100f);
        assertThat(studentAccount.getAccountBalance()).isEqualTo(100f);
    }
    @Test
    void depositOfZeroDoesNotChangeBalance() {
        StudentAccount studentAccount = new StudentAccount(customer, userName, 100f, newAccountPassword);
        studentAccount.deposit(0f);
        assertThat(studentAccount.getAccountBalance()).isEqualTo(100f);
    }
    @Test
    void depositWithMinusValueThrowsException() {
        StudentAccount studentAccount1 = new StudentAccount(customer, userName, 0f, newAccountPassword);
        assertThrows(RuntimeException.class, () -> studentAccount1.deposit(-100f));

        StudentAccount studentAccount2 = new StudentAccount(customer, "Another Student", 100f, newAccountPassword);
        assertThrows(RuntimeException.class, () -> studentAccount2.deposit(-100f));
    }

    // Withdraw tests:
    @Test
    void withdrawalDecreasesBalance() {
        StudentAccount studentAccount = new StudentAccount(customer, userName, 100f, newAccountPassword);
        studentAccount.withdraw(25f);
        assertThat(studentAccount.getAccountBalance()).isEqualTo(75f);
    }
    @Test
    void withdrawalOfZeroDoesNotChangeBalance() {
        StudentAccount studentAccount = new StudentAccount(customer, userName, 100f, newAccountPassword);
        studentAccount.withdraw(0f);
        assertThat(studentAccount.getAccountBalance()).isEqualTo(100f);
    }
    @Test
    void withdrawalWithMinusValueThrowsException() {
        StudentAccount studentAccount1 = new StudentAccount(customer, userName, 0f, newAccountPassword);
        assertThrows(RuntimeException.class, () -> studentAccount1.withdraw(-100f));

        StudentAccount studentAccount2 = new StudentAccount(customer, "Another Student", 100f, newAccountPassword);
        assertThrows(RuntimeException.class, () -> studentAccount2.withdraw(-100f));
    }

    // Date tests:
    @Test
    void getsDateAccountLastUpdated() {
        StudentAccount studentAccount = new StudentAccount(customer, userName, 100f, newAccountPassword);
        studentAccount.deposit(100f);
        assertThat(studentAccount.getDateAccountLastUpdated()).isNotNull();
    }

    @Test
    void getsAccountCreationDate() {
        StudentAccount studentAccount = new StudentAccount(customer, userName, 100f, newAccountPassword);
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