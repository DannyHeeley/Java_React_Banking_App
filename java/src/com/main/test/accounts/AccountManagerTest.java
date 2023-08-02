package com.main.test.accounts;

import com.main.app.accounts.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.main.app.accounts.AccountType.ADULT;
import static com.main.app.accounts.AccountType.STUDENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountManagerTest {

    private static final String CORRECT_PASSWORD = "Password123!";
    ArrayList<AccountBase> bankAccounts;
    String userNameAdult = "Adult";
    String userNameStudent = "Student";
    String firstName = "Fizz";
    String lastName = "Buzz";
    LocalDate dateOfBirth = LocalDate.of(1993, 1, 11);
    String email = "fizzbuzz@gmail.com";
    Float initialDeposit = 50f;

    @BeforeEach
    void setUp() {
        bankAccounts = AccountManager.getBankAccounts();
    }

    @AfterEach
    void tearDown() {
        AccountManager.clearBankAccountList();
    }

    @Test
    void returnsEmptyListOfBankAccounts() {
        assertThat(bankAccounts).isNotNull();
        assertThat(bankAccounts).isEmpty();
        assertThat(bankAccounts).isEqualTo(new ArrayList<>());
    }

    @Test
    void getsAllBankAccounts() {
        ArrayList<AccountBase> theseBankAccounts = AccountManager.getBankAccounts();

        AccountBase account1 = BankAccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(theseBankAccounts).containsExactly(account1);

        AccountBase account2 = BankAccountFactory.createAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(theseBankAccounts).containsExactly(account1, account2);

        AccountBase account3 = BankAccountFactory.createAccount(
                STUDENT, "Another Student", CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(theseBankAccounts).containsExactly(account1, account2, account3);
    }

    @Test
    void accountsHasOneAdultAccount() {
        AccountBase account = (AdultAccount) BankAccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(bankAccounts).containsOnlyOnce(account);
    }

    @Test
    void duplicateAdultAccountIsNotAddedToAccounts() {
        AdultAccount account1 = (AdultAccount) BankAccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        AdultAccount account2 = (AdultAccount) BankAccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(bankAccounts).containsOnly(account1);
        assertThat(bankAccounts).containsOnly(account2);
    }

    @Test
    void accountsHasOneStudentAccount() {
        AccountBase account = BankAccountFactory.createAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(bankAccounts).containsOnlyOnce(account);
    }

    @Test
    void duplicateStudentAccountIsNotAddedToAccounts() {
        AccountBase account1 = BankAccountFactory.createAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        AccountBase account2 = BankAccountFactory.createAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(bankAccounts).containsOnly(account1);
        assertThat(bankAccounts).containsOnly(account2);
    }

    @Test
    void returnsCorrectAccount() {
        AccountBase account1 = BankAccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        AccountBase account2 = BankAccountFactory.createAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, 150f, firstName, lastName, dateOfBirth, email
        );
        assertThat(bankAccounts).containsOnlyOnce(account1);
        assertThat(AccountManager.getAccount(userNameAdult)).isEqualTo(account1);
        assertThat(AccountManager.getAccount(userNameAdult)).isNotEqualTo(account2);
    }

    @Test
    void trueIfAccountExists() {
        BankAccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        BankAccountFactory.createAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertTrue(AccountManager.accountExists(userNameAdult));
        assertTrue(AccountManager.accountExists(userNameStudent));
    }

    @Test
    void falseIfAccountExists() {
        BankAccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        BankAccountFactory.createAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertFalse(AccountManager.accountExists("Fizz Bar"));
        assertFalse(AccountManager.accountExists("Foo Buzz"));
    }
}
