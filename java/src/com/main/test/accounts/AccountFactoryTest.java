package com.main.test.accounts;

import com.main.app.Bank;
import com.main.app.accounts.AccountBase;
import com.main.app.accounts.AccountFactory;
import com.main.app.accounts.AccountManager;
import com.main.app.accounts.AccountType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.main.app.accounts.AccountType.ADULT;
import static com.main.app.accounts.AccountType.STUDENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountFactoryTest {
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
        AccountFactory.createAccountNewUser(
                ADULT, userNameAdult, CORRECT_PASSWORD, 100f, firstName, lastName, dateOfBirth, email
        );
        assertThat(bank.getMainBankBalance()).isEqualTo(100f);
    }

    @Test
    void bankBalanceSameWithZeroInitialDeposit() {
        AccountFactory.createAccountNewUser(
                ADULT, userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(bank.getMainBankBalance()).isEqualTo(0f);
    }

    @Test
    void bankBalanceSameWithNullAccount() {
        AccountFactory.createAccountNewUser(
                ADULT, userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        AccountFactory.createAccountNewUser(
                ADULT, userNameAdult, CORRECT_PASSWORD,  1000f, firstName, lastName, dateOfBirth, email
        );
        assertThat(bank.getMainBankBalance()).isEqualTo(0f);
    }

    // Test cases for createNewAdultAccount:
    @Test
    void createsAdultAccount() {
        AccountBase account = AccountFactory.createAccountNewUser(
                ADULT, userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(account).isNotNull();
    }
    @Test
    void doesNotCreateAccountIfCreatingPasswordThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                AccountFactory.createAccountNewUser(
                AccountType.ADULT, "NotCreated", "password",
                0f, firstName, lastName, dateOfBirth, email
                ));
        assertThat(AccountManager.getInstance().getBankAccounts()).isEmpty();
    }

    @Test
    void adultAccountWithDepositHasBalance() {
        Float initialDeposit = 100f;
        AccountBase account = AccountFactory.createAccountNewUser(
                ADULT, userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(account.getAccountBalance()).isEqualTo(initialDeposit);
    }

    @Test
    void adultAccountWithZeroDepositHasZeroBalance() {
        Float initialDeposit = 0f;
        AccountBase account = AccountFactory.createAccountNewUser(
                ADULT, userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(account.getAccountBalance()).isEqualTo(initialDeposit);
    }

    @Test
    void cannotCreateAdultAccountWithNegativeInitialDeposit() {
        Float initialDeposit = -1000f;
        AccountBase account = AccountFactory.createAccountNewUser(
                ADULT, userNameAdult, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(account).isNull();
    }

    @Test
    void exceptionHandledIfAdultAccountExistsWithSameUserName() {
        AccountFactory.createAccountNewUser(
                ADULT, userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertDoesNotThrow(() -> AccountFactory.createAccountNewUser(
                ADULT, userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        ));
    }

    @Test
    void doesNotAllowDuplicateAdultAccount() {
        AccountBase account1 = AccountFactory.createAccountNewUser(
                ADULT, userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        AccountFactory.createAccountNewUser(
                ADULT, userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(AccountManager.getInstance().getBankAccounts()).containsOnlyOnce(account1);
    }
    @Test
    void doesNotThrowExceptionIfAdultAccountsHaveUniqueUserNames() {
        assertDoesNotThrow(() -> AccountFactory.createAccountNewUser(
                ADULT, userNameAdult, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        ));
        assertDoesNotThrow(() -> AccountFactory.createAccountNewUser(
                ADULT, "Another Adult", CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        ));
    }

    // Test cases for createNewStudentAccount:
    @Test
    void createsStudentAccount() {
        AccountBase account = AccountFactory.createAccountNewUser(
                STUDENT, userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(account).isNotNull();
    }

    @Test
    void studentAccountWithDepositHasBalance() {
        Float initialDeposit = 100f;
        AccountBase account = AccountFactory.createAccountNewUser(
                STUDENT, userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(account.getAccountBalance()).isEqualTo(initialDeposit);
    }

    @Test
    void studentAccountWithZeroDepositHasZeroBalance() {
        Float initialDeposit = 0f;
        AccountBase account = AccountFactory.createAccountNewUser(
                STUDENT, userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(account.getAccountBalance()).isEqualTo(initialDeposit);
    }

    @Test
    void studentAccountNegativeInitialDepositThrowsException() {
        Float initialDeposit = -1000f;
        AccountBase account = AccountFactory.createAccountNewUser(
                STUDENT, userNameStudent, CORRECT_PASSWORD, initialDeposit, firstName, lastName, dateOfBirth, email
        );
        assertThat(account).isNull();
    }

    @Test
    void exceptionHandledIfStudentAccountExistsWithSameUserName() {
        AccountFactory.createAccountNewUser(
                STUDENT, userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertDoesNotThrow(() -> AccountFactory.createAccountNewUser(
                STUDENT, userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        ));
    }
    @Test
    void doesNotAllowDuplicateStudentAccount() {
        AccountBase account1 = AccountFactory.createAccountNewUser(
                STUDENT, userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        AccountFactory.createAccountNewUser(
                STUDENT, userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        );
        assertThat(AccountManager.getInstance().getBankAccounts()).containsOnlyOnce(account1);
    }

    @Test
    void doesNotThrowExceptionIfStudentAccountsHaveUniqueUserNames() {
        assertDoesNotThrow(() -> AccountFactory.createAccountNewUser(
                STUDENT, userNameStudent, CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        ));
        assertDoesNotThrow(() -> AccountFactory.createAccountNewUser(
                STUDENT, "Another Student", CORRECT_PASSWORD, 0f, firstName, lastName, dateOfBirth, email
        ));
    }
}
