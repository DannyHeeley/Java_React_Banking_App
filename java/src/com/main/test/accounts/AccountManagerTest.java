package com.main.test.accounts;

import com.main.app.accounts.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.main.app.accounts.AccountType.ADULT;
import static com.main.app.accounts.AccountType.STUDENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountManagerTest {

    private static final String CORRECT_PASSWORD = "Password123!";
    ArrayList<AccountBase> bankAccounts;

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

        AccountBase account1 = BankAccountFactory.createAccount(ADULT,"Foo Bar", CORRECT_PASSWORD, 50f);
        assertThat(theseBankAccounts).containsExactly(account1);

        AccountBase account2 = BankAccountFactory.createAccount(STUDENT, "A Student", CORRECT_PASSWORD, 0f);
        assertThat(theseBankAccounts).containsExactly(account1, account2);

        AccountBase account3 = BankAccountFactory.createAccount(STUDENT, "Another Student", CORRECT_PASSWORD, 0f);
        assertThat(theseBankAccounts).containsExactly(account1, account2, account3);
    }

    @Test
    void accountsHasOneAdultAccount() {
        AdultAccount account1 = (AdultAccount) BankAccountFactory.createAccount(ADULT,"Foo Bar", CORRECT_PASSWORD, 50f);
        assertThat(bankAccounts).containsOnlyOnce(account1);
    }

    @Test
    void duplicateAdultAccountIsNotAddedToAccounts() {
        AdultAccount account1 = (AdultAccount) BankAccountFactory.createAccount(ADULT,"Fizz Buzz", CORRECT_PASSWORD, 50f);
        AdultAccount account2 = (AdultAccount) BankAccountFactory.createAccount(ADULT,"Fizz Buzz", CORRECT_PASSWORD, 50f);
        assertThat(bankAccounts).containsOnly(account1);
        assertThat(bankAccounts).containsOnly(account2);
    }

    @Test
    void accountsHasOneStudentAccount() {
        StudentAccount account1 = (StudentAccount) BankAccountFactory.createAccount(STUDENT,"Fizz Buzz", CORRECT_PASSWORD, 50f);
        assertThat(bankAccounts).containsOnlyOnce(account1);
    }

    @Test
    void duplicateStudentAccountIsNotAddedToAccounts() {
        StudentAccount account1 = (StudentAccount) BankAccountFactory.createAccount(STUDENT,"Fizz Buzz", CORRECT_PASSWORD, 50f);
        StudentAccount account2 = (StudentAccount) BankAccountFactory.createAccount(STUDENT,"Fizz Buzz", CORRECT_PASSWORD, 50f);
        assertThat(bankAccounts).containsOnly(account1);
        assertThat(bankAccounts).containsOnly(account2);
    }

    @Test
    void returnsCorrectAccount() {
        AdultAccount account1 = (AdultAccount) BankAccountFactory.createAccount(ADULT,"Foo Bar", CORRECT_PASSWORD, 50f);
        StudentAccount account2 = (StudentAccount) BankAccountFactory.createAccount(STUDENT,"Fizz Buzz", CORRECT_PASSWORD, 50f);
        assertThat(bankAccounts).containsOnlyOnce(account1);
        assertThat(AccountManager.getAccount("Foo Bar")).isEqualTo(account1);
        assertThat(AccountManager.getAccount("Foo Bar")).isNotEqualTo(account2);
    }

    @Test
    void trueIfAccountExists() {
        BankAccountFactory.createAccount(ADULT,"Foo Bar", CORRECT_PASSWORD, 50f);
        assertTrue(AccountManager.accountExists("Foo Bar"));
    }

    @Test
    void falseIfAccountExists() {
        BankAccountFactory.createAccount(ADULT,"Foo Bar", CORRECT_PASSWORD, 50f);
        assertFalse(AccountManager.accountExists("Fizz Buzz"));
    }
}
