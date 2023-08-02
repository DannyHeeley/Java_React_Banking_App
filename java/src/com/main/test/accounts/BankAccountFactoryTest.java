package com.main.test.accounts;

import com.main.app.Bank;
import com.main.app.accounts.AccountBase;
import com.main.app.accounts.AccountManager;
import com.main.app.accounts.BankAccountFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.main.app.accounts.AccountType.ADULT;
import static com.main.app.accounts.AccountType.STUDENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class BankAccountFactoryTest {
    private static final String CORRECT_PASSWORD = "Password123!";
    Bank bank;
    String userNameAdult = "Adult";
    String userNameStudent = "Student";
    String firstName = "Fizz";
    String lastName = "Buzz";
    LocalDate dateOfBirth = LocalDate.of(1993, 1, 11);
    String email = "fizzbuzz@gmail.com";
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
        BankAccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, 100f, firstName, lastName, dateOfBirth, email
        );
        assertThat(bank.getBankBalance()).isEqualTo(100f);
    }

    @Test
    void bankBalanceSameWithZeroInitialDeposit() {
        BankAccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(bank.getBankBalance()).isEqualTo(0f);
    }

    @Test
    void bankBalanceSameWithNullAccount() {
        BankAccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        BankAccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD,  1000f, firstName, lastName, dateOfBirth, email
        );
        assertThat(bank.getBankBalance()).isEqualTo(0f);
    }

    // Test cases for createNewAdultAccount:
    @Test
    void createsAdultAccount() {
        AccountBase account = BankAccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(account).isNotNull();
    }
    @Test
    void adultAccountWithDepositHasBalance() {
        Float initialDeposit = 100f;
        AccountBase account = BankAccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(account.getBalance()).isEqualTo(initialDeposit);
    }

    @Test
    void adultAccountWithZeroDepositHasZeroBalance() {
        Float initialDeposit = 0f;
        AccountBase account = BankAccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(account.getBalance()).isEqualTo(initialDeposit);
    }

    @Test
    void adultAccountNegativeInitialDepositThrowsException() {
        Float initialDeposit = -1000f;
        AccountBase account = BankAccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(account.getBalance()).isEqualTo(0);
    }

    @Test
    void exceptionHandledIfAdultAccountExistsWithSameUserName() {
        BankAccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertDoesNotThrow(() -> BankAccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        ));
    }

    @Test
    void doesNotAllowDuplicateAdultAccount() {
        AccountBase account1 = BankAccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        BankAccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(AccountManager.getBankAccounts()).containsOnlyOnce(account1);
    }
    @Test
    void doesNotThrowExceptionIfAdultAccountsHaveUniqueUserNames() {
        assertDoesNotThrow(() -> BankAccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        ));
        assertDoesNotThrow(() -> BankAccountFactory.createAccount(
                ADULT, "Another Adult", CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        ));
    }

    // Test cases for createNewStudentAccount:
    @Test
    void createsStudentAccount() {
        AccountBase account = BankAccountFactory.createAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(account).isNotNull();
    }

    @Test
    void studentAccountWithDepositHasBalance() {
        Float initialDeposit = 100f;
        AccountBase account = BankAccountFactory.createAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(account.getBalance()).isEqualTo(initialDeposit);
    }

    @Test
    void studentAccountWithZeroDepositHasZeroBalance() {
        Float initialDeposit = 0f;
        AccountBase account = BankAccountFactory.createAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(account.getBalance()).isEqualTo(initialDeposit);
    }

    @Test
    void studentAccountNegativeInitialDepositThrowsException() {
        Float initialDeposit = -1000f;
        AccountBase account = BankAccountFactory.createAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(account.getBalance()).isEqualTo(0);
    }

    @Test
    void exceptionHandledIfStudentAccountExistsWithSameUserName() {
        BankAccountFactory.createAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertDoesNotThrow(() -> BankAccountFactory.createAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        ));
    }
    @Test
    void doesNotAllowDuplicateStudentAccount() {
        AccountBase account1 = BankAccountFactory.createAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        BankAccountFactory.createAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(AccountManager.getBankAccounts()).containsOnlyOnce(account1);
    }

    @Test
    void doesNotThrowExceptionIfStudentAccountsHaveUniqueUserNames() {
        assertDoesNotThrow(() -> BankAccountFactory.createAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        ));
        assertDoesNotThrow(() -> BankAccountFactory.createAccount(
                STUDENT, "Another Student", CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        ));
    }
}
