package com.main.test.accounts;

import com.main.app.Bank;
import com.main.app.accounts.AccountBase;
import com.main.app.accounts.AccountManager;
import com.main.app.accounts.BankAccountFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.main.app.accounts.AccountType.ADULT;
import static com.main.app.accounts.AccountType.STUDENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class BankAccountFactoryTest {
    private static final String CORRECT_PASSWORD = "Password123!";
    Bank bank;
    @BeforeEach
    void setUp() {
        bank = Bank.getInstance();
    }

    @AfterEach
    void tearDown() {
        bank.resetBank();
    }

    // Test cases for update to bank:
    @Test
    void increaseBankBalanceWithPositiveInitialDeposit() {
        BankAccountFactory.createAccount(ADULT, "Foo Bar", CORRECT_PASSWORD, 100f);
        assertThat(bank.getBankBalance()).isEqualTo(100f);
    }

    @Test
    void bankBalanceSameWithZeroInitialDeposit() {
        BankAccountFactory.createAccount(ADULT, "Foo Bar", CORRECT_PASSWORD, 0f);
        assertThat(bank.getBankBalance()).isEqualTo(0f);
    }

    @Test
    void bankBalanceSameWithNullAccount() {
        BankAccountFactory.createAccount(ADULT, "Foo Bar", CORRECT_PASSWORD, 0f);
        BankAccountFactory.createAccount(ADULT, "Foo Bar", CORRECT_PASSWORD,  1000f);
        assertThat(bank.getBankBalance()).isEqualTo(0f);
    }

    // Test cases for createNewAdultAccount:
    @Test
    void createsAdultAccount() {
        AccountBase account = BankAccountFactory.createAccount(ADULT, "An Adult", CORRECT_PASSWORD, 0f);
        assertThat(account).isNotNull();
    }
    @Test
    void adultAccountWithDepositHasBalance() {
        Float initialDeposit = 100f;
        AccountBase account = BankAccountFactory.createAccount(ADULT, "An Adult", CORRECT_PASSWORD, initialDeposit);
        assertThat(account.getBalance()).isEqualTo(initialDeposit);
    }

    @Test
    void adultAccountWithZeroDepositHasZeroBalance() {
        Float initialDeposit = 0f;
        AccountBase account = BankAccountFactory.createAccount(ADULT, "An Adult", CORRECT_PASSWORD, initialDeposit);
        assertThat(account.getBalance()).isEqualTo(initialDeposit);
    }

    @Test
    void adultAccountNegativeInitialDepositThrowsException() {
        Float initialDeposit = -1000f;
        AccountBase account = BankAccountFactory.createAccount(ADULT, "An Adult", CORRECT_PASSWORD, initialDeposit);
        assertThat(account.getBalance()).isEqualTo(0);
    }

    @Test
    void exceptionHandledIfAdultAccountExistsWithSameUserName() {
        BankAccountFactory.createAccount(ADULT, "An Adult", CORRECT_PASSWORD, 0f);
        assertDoesNotThrow(() -> BankAccountFactory.createAccount(ADULT, "An Adult", CORRECT_PASSWORD, 0f));
    }

    @Test
    void doesNotAllowDuplicateAdultAccount() {
        AccountBase account1 = BankAccountFactory.createAccount(ADULT, "An Adult", CORRECT_PASSWORD, 0f);
        BankAccountFactory.createAccount(ADULT, "An Adult", CORRECT_PASSWORD, 0f);
        assertThat(AccountManager.getBankAccounts()).containsOnlyOnce(account1);
    }
    @Test
    void doesNotThrowExceptionIfAdultAccountsHaveUniqueUserNames() {
        assertDoesNotThrow(() -> BankAccountFactory.createAccount(ADULT, "An Adult", CORRECT_PASSWORD, 0f));
        assertDoesNotThrow(() -> BankAccountFactory.createAccount(ADULT, "Another Adult", CORRECT_PASSWORD, 0f));
    }

    // Test cases for createNewStudentAccount:
    @Test
    void createsStudentAccount() {
        AccountBase account = BankAccountFactory.createAccount(STUDENT, "A Student", CORRECT_PASSWORD, 0f);
        assertThat(account).isNotNull();
    }

    @Test
    void studentAccountWithDepositHasBalance() {
        Float initialDeposit = 100f;
        AccountBase account = BankAccountFactory.createAccount(STUDENT, "A Student", CORRECT_PASSWORD, initialDeposit);
        assertThat(account.getBalance()).isEqualTo(initialDeposit);
    }

    @Test
    void studentAccountWithZeroDepositHasZeroBalance() {
        Float initialDeposit = 0f;
        AccountBase account = BankAccountFactory.createAccount(STUDENT, "A Student", CORRECT_PASSWORD, initialDeposit);
        assertThat(account.getBalance()).isEqualTo(initialDeposit);
    }

    @Test
    void studentAccountNegativeInitialDepositThrowsException() {
        Float initialDeposit = -1000f;
        AccountBase account = BankAccountFactory.createAccount(STUDENT, "A Student", CORRECT_PASSWORD, initialDeposit);
        assertThat(account.getBalance()).isEqualTo(0);
    }

    @Test
    void exceptionHandledIfStudentAccountExistsWithSameUserName() {
        BankAccountFactory.createAccount(STUDENT, "A Student", CORRECT_PASSWORD, 0f);
        assertDoesNotThrow(() -> BankAccountFactory.createAccount(STUDENT, "A Student", CORRECT_PASSWORD, 0f));
    }
    @Test
    void doesNotAllowDuplicateStudentAccount() {
        AccountBase account1 = BankAccountFactory.createAccount(STUDENT, "A Student", CORRECT_PASSWORD, 0f);
        BankAccountFactory.createAccount(STUDENT, "A Student", CORRECT_PASSWORD, 0f);
        assertThat(AccountManager.getBankAccounts()).containsOnlyOnce(account1);
    }

    @Test
    void doesNotThrowExceptionIfStudentAccountsHaveUniqueUserNames() {
        assertDoesNotThrow(() -> BankAccountFactory.createAccount(STUDENT, "A Student", CORRECT_PASSWORD, 0f));
        assertDoesNotThrow(() -> BankAccountFactory.createAccount(STUDENT, "Another Student", CORRECT_PASSWORD, 0f));
    }
}
