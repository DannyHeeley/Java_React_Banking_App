package com.main.test.accounts;

import com.main.app.accounts.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.main.app.accounts.AccountType.ADULT;
import static com.main.app.accounts.AccountType.STUDENT;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class AccountManagerTest {
    ArrayList<AccountBase> bankAccounts;

    @BeforeEach
    void setUp() {
        bankAccounts = AccountManager.getBankAccounts();
    }

    @AfterEach
    void tearDown() {
        AccountManager.resetBankAccounts();
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

        AccountBase account1 = BankAccountFactory.createAccount(ADULT,"Foo Bar", "password123", 50f);
        assertThat(theseBankAccounts).containsExactly(account1);

        AccountBase account2 = BankAccountFactory.createAccount(STUDENT, "A Student", "password", 0f);
        assertThat(theseBankAccounts).containsExactly(account1, account2);

        AccountBase account3 = BankAccountFactory.createAccount(STUDENT, "Another Student", "password", 0f);
        assertThat(theseBankAccounts).containsExactly(account1, account2, account3);
    }

    @Test
    void accountsHasOneAdultAccount() {
        AdultAccount account1 = (AdultAccount) BankAccountFactory.createAccount(ADULT,"Foo Bar", "password123", 50f);
        assertThat(bankAccounts).containsOnlyOnce(account1);
    }

    @Test
    void accountsHasOneStudentAccount() {
        StudentAccount account1 = (StudentAccount) BankAccountFactory.createAccount(STUDENT,"Fizz Buzz", "password123", 50f);
        assertThat(bankAccounts).containsOnlyOnce(account1);
    }

    @Test
    void returnsCorrectAccount() {
        AdultAccount account1 = (AdultAccount) BankAccountFactory.createAccount(ADULT,"Foo Bar", "password123", 50f);
        StudentAccount account2 = (StudentAccount) BankAccountFactory.createAccount(STUDENT,"Fizz Buzz", "password123", 50f);
        assertThat(bankAccounts).containsOnlyOnce(account1);
        assertThat(AccountManager.getAccount("Foo Bar")).isEqualTo(account1);
        assertThat(AccountManager.getAccount("Foo Bar")).isNotEqualTo(account2);
    }

    @Test
    void trueIfAccountExists() {
        BankAccountFactory.createAccount(ADULT,"Foo Bar", "password123", 50f);
        assertTrue(AccountManager.accountExists("Foo Bar"));
    }
}
