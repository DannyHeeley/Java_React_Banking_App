package com.core.accounts;

import com.app.core.accounts.AccountBase;
import com.app.core.accounts.AccountFactory;
import com.app.core.Bank;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


class AccountFactoryTest {
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
        AccountFactory.newAdultAccount(
                userNameAdult, CORRECT_PASSWORD, 100f, firstName, lastName, dateOfBirth, email
        );
        assertThat(bank.getBankTotalBalance()).isEqualTo(100f);
    }

    @Test
    void bankBalanceSameWithZeroInitialDeposit() {
        AccountFactory.newAdultAccount(
                userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(bank.getBankTotalBalance()).isEqualTo(0f);
    }

    @Test
    void bankBalanceSameWithNullAccount() {
        AccountFactory.newAdultAccount(
                userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        AccountFactory.newAdultAccount(
                userNameAdult, CORRECT_PASSWORD,  1000f, firstName, lastName, dateOfBirth, email
        );
        assertThat(bank.getBankTotalBalance()).isEqualTo(0f);
    }

    // Test cases for createNewAdultAccount:
    @Test
    void createsAdultAccount() {
        AccountBase account = AccountFactory.newAdultAccount(
                userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(account).isNotNull();
    }
    @Test
    void doesNotCreateAccountIfCreatingPasswordThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                AccountFactory.newAdultAccount(
                        "NotCreated", "password",
                0f, firstName, lastName, dateOfBirth, email
                ));
        assertThat(Bank.getInstance().getBankAccounts()).isEmpty();
    }

    @Test
    void adultAccountWithDepositHasBalance() {
        Float initialDeposit = 100f;
        AccountBase account = AccountFactory.newAdultAccount(
                userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(account.getAccountBalance()).isEqualTo(initialDeposit);
    }

    @Test
    void adultAccountWithZeroDepositHasZeroBalance() {
        Float initialDeposit = 0f;
        AccountBase account = AccountFactory.newAdultAccount(
                userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(account.getAccountBalance()).isEqualTo(initialDeposit);
    }

    @Test
    void cannotCreateAdultAccountWithNegativeInitialDeposit() {
        Float initialDeposit = -1000f;
        AccountBase account = AccountFactory.newAdultAccount(
                userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(account).isNull();
    }

    @Test
    void exceptionHandledIfAdultAccountExistsWithSameUserName() {
        AccountFactory.newAdultAccount(
                userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertDoesNotThrow(() -> AccountFactory.newAdultAccount(
                userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        ));
    }

    @Test
    void doesNotAllowDuplicateAdultAccount() {
        AccountBase account1 = AccountFactory.newAdultAccount(
                userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        AccountFactory.newAdultAccount(
                userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(Bank.getInstance().getBankAccounts()).containsOnlyOnce(account1);
    }
    @Test
    void doesNotThrowExceptionIfAdultAccountsHaveUniqueUserNames() {
        assertDoesNotThrow(() -> AccountFactory.newAdultAccount(
                userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        ));
        assertDoesNotThrow(() -> AccountFactory.newAdultAccount(
                "Another Adult", CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        ));
    }

    // Test cases for createNewStudentAccount:
    @Test
    void createsStudentAccount() {
        AccountBase account = AccountFactory.newStudentAccount(
                userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(account).isNotNull();
    }

    @Test
    void studentAccountWithDepositHasBalance() {
        Float initialDeposit = 100f;
        AccountBase account = AccountFactory.newStudentAccount(
                userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(account.getAccountBalance()).isEqualTo(initialDeposit);
    }

    @Test
    void studentAccountWithZeroDepositHasZeroBalance() {
        Float initialDeposit = 0f;
        AccountBase account = AccountFactory.newStudentAccount(
                userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(account.getAccountBalance()).isEqualTo(initialDeposit);
    }

    @Test
    void studentAccountNegativeInitialDepositThrowsException() {
        Float initialDeposit = -1000f;
        AccountBase account = AccountFactory.newStudentAccount(
                userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(account).isNull();
    }

    @Test
    void exceptionHandledIfStudentAccountExistsWithSameUserName() {
        AccountFactory.newStudentAccount(
                userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertDoesNotThrow(() -> AccountFactory.newStudentAccount(
                userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        ));
    }
    @Test
    void doesNotAllowDuplicateStudentAccount() {
        AccountBase account1 = AccountFactory.newStudentAccount(
                userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        AccountFactory.newStudentAccount(
                userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(Bank.getInstance().getBankAccounts()).containsOnlyOnce(account1);
    }

    @Test
    void doesNotThrowExceptionIfStudentAccountsHaveUniqueUserNames() {
        assertDoesNotThrow(() -> AccountFactory.newStudentAccount(
                userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        ));
        assertDoesNotThrow(() -> AccountFactory.newStudentAccount(
                "Another Student", CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        ));
    }
}
