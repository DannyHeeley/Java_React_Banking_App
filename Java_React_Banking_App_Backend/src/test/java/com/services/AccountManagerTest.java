package com.services;

import com.app.core.accounts.AccountBase;
import com.app.core.accounts.AccountFactory;
import com.app.services.AccountManager;
import com.app.core.accounts.AdultAccount;
import com.app.core.Bank;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountManagerTest {

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
        bankAccounts = Bank.getInstance().getBankAccounts();
    }

    @AfterEach
    void tearDown() {
        AccountManager.getInstance().clearBankAccountList();
        Bank.getInstance().resetBank();
    }

    @Test
    void returnsEmptyListOfBankAccounts() {
        assertThat(bankAccounts).isNotNull();
        assertThat(bankAccounts).isEmpty();
        assertThat(bankAccounts).isEqualTo(new ArrayList<>());
    }

    @Test
    void getsAllBankAccounts() {
        ArrayList<AccountBase> theseBankAccounts = Bank.getInstance().getBankAccounts();

        AccountBase account1 = AccountFactory.newAdultAccount(
                userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(theseBankAccounts).containsExactly(account1);

        AccountBase account2 = AccountFactory.newStudentAccount(
                userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(theseBankAccounts).containsExactly(account1, account2);

        AccountBase account3 = AccountFactory.newStudentAccount(
                "Another Student", CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(theseBankAccounts).containsExactly(account1, account2, account3);
    }

    @Test
    void accountsHasOneAdultAccount() {
        AdultAccount account = (AdultAccount) AccountFactory.newAdultAccount(
                userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(bankAccounts).containsOnlyOnce(account);
    }

    @Test
    void duplicateAdultAccountIsNotAddedToAccounts() {
        AdultAccount account1 = (AdultAccount) AccountFactory.newAdultAccount(
                userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        AccountFactory.newAdultAccount(
                userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(bankAccounts).containsOnly(account1);
    }

    @Test
    void accountsHasOneStudentAccount() {
        AccountBase account = AccountFactory.newStudentAccount(
                userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(bankAccounts).containsOnlyOnce(account);
    }

    @Test
    void duplicateStudentAccountIsNotAddedToAccounts() {
        AccountBase account1 = AccountFactory.newStudentAccount(
                userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        AccountFactory.newStudentAccount(
                userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(bankAccounts).containsOnly(account1);
    }

    @Test
    void returnsCorrectAccount() {
        AccountBase account1 = AccountFactory.newAdultAccount(
                userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        AccountBase account2 = AccountFactory.newStudentAccount(
                userNameStudent, CORRECT_PASSWORD, 150f, firstName, lastName, dateOfBirth, email
        );
        assertThat(AccountManager.getInstance().getAccount(account1.getAccountId())).isEqualTo(account1);
        assertThat(AccountManager.getInstance().getAccount(account1.getAccountId())).isNotEqualTo(account2);
    }

    @Test
    void trueIfAccountExists() {
        AccountBase adultAccount = AccountFactory.newAdultAccount(
                userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        AccountBase studentAccount = AccountFactory.newStudentAccount(
                userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertTrue(AccountManager.getInstance().accountExists(adultAccount.getUserName()));
        assertTrue(AccountManager.getInstance().accountExists(studentAccount.getUserName()));
    }
}
