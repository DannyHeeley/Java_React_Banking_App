package com.main.test.login;

import com.main.app.accounts.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.RejectedExecutionException;

import static com.main.app.Login.PasswordService.*;
import static org.junit.jupiter.api.Assertions.*;

class PasswordServiceTest {
    AccountBase testAccountAdult;
    AccountBase testAccountStudent;
    int accountNumber;
    private static final String CORRECT_PASSWORD = "Password123!";
    private static final String INCORRECT_PASSWORD = "Incorrect123!";
    private static final String INCORRECT_FORMAT = "abc";

    @BeforeEach
    void setUp() {
        testAccountAdult = BankAccountFactory.createAccount(AccountType.ADULT, "Adult User", CORRECT_PASSWORD, 0f);
        testAccountStudent = BankAccountFactory.createAccount(AccountType.STUDENT, "Student User", CORRECT_PASSWORD, 0f);
        accountNumber = testAccountAdult.getAccountNumber();
    }

    @AfterEach
    void tearDown() {
        AccountManager.clearBankAccountList();
    }

    @Test
    void authenticateIsTrueWithCorrectUserPassword() {
        assertTrue(authenticateUserPassword(testAccountAdult, CORRECT_PASSWORD));
        assertTrue(authenticateUserPassword(testAccountStudent, CORRECT_PASSWORD));
    }

    @Test
    void authenticateIsFalseWithIncorrectUserPassword() {
        assertFalse(authenticateUserPassword(testAccountAdult, INCORRECT_PASSWORD));
        assertTrue(authenticateUserPassword(testAccountStudent, CORRECT_PASSWORD));
    }

    @Test
    void authenticateIsFalseIfPasswordIsNull() {
        assertThrows(RejectedExecutionException.class, () -> authenticateUserPassword(testAccountAdult, null));
        assertThrows(RejectedExecutionException.class, () -> authenticateUserPassword(testAccountStudent, null));
    }

    @Test
    void setsPasswordHashForAccount() {
        assertTrue(testAccountAdult.getAccountPasswordHash() != null);
        assertTrue(testAccountStudent.getAccountPasswordHash() != null);
    }

    @Test
    void throwsExceptionIfNewPasswordIsNull() {
        assertThrows(RuntimeException.class, () -> setPasswordHashForAccount(testAccountAdult, null));
        assertThrows(RuntimeException.class, () -> setPasswordHashForAccount(testAccountStudent, null));
    }

    @Test
    void throwsExceptionIfNewPasswordIsEmptyString() {
        assertThrows(RuntimeException.class, () -> setPasswordHashForAccount(testAccountAdult, ""));
        assertThrows(RuntimeException.class, () -> setPasswordHashForAccount(testAccountStudent, ""));
    }

    @Test
    void enforcesPasswordRulesWhenSettingWithIncorrectFormat() {
        assertThrows(RejectedExecutionException.class, () -> setPasswordHashForAccount(testAccountAdult, INCORRECT_FORMAT));
        assertThrows(RejectedExecutionException.class, () -> setPasswordHashForAccount(testAccountStudent, INCORRECT_FORMAT));
    }

    @Test
    void doesNotThrowIfSettingPasswordWithCorrectFormat() {
        assertDoesNotThrow(() -> setPasswordHashForAccount(testAccountAdult, CORRECT_PASSWORD));
        assertDoesNotThrow(() -> setPasswordHashForAccount(testAccountStudent, CORRECT_PASSWORD));
    }

    @Test
    void enforcesPassRulesWhenAuthenticatingAdultPassWithIncorrectFormat() {
        assertAll(
                () -> assertThrows(RejectedExecutionException.class, () -> authenticateUserPassword(testAccountAdult, INCORRECT_FORMAT)),
                () -> assertThrows(RejectedExecutionException.class, () -> BankAccountFactory.createAccount(AccountType.ADULT, "User 2", "password", 0f)),
                () -> assertThrows(RejectedExecutionException.class, () -> BankAccountFactory.createAccount(AccountType.ADULT, "User 3", "p@ssw0rd", 0f)),
                () -> assertThrows(RejectedExecutionException.class, () -> BankAccountFactory.createAccount(AccountType.ADULT, "User 4", "UPPERCASE", 0f)),
                () -> assertThrows(RejectedExecutionException.class, () -> BankAccountFactory.createAccount(AccountType.ADULT, "User 5", "12345678", 0f))
        );
    }

    @Test
    void doesNotThrowIfAuthenticatingAdultPassWithCorrectFormat() {
        AccountBase testAccount2 = BankAccountFactory.createAccount(AccountType.ADULT, "User 2", "Pa$$w0rd", 0f);
        AccountBase testAccount3 = BankAccountFactory.createAccount(AccountType.ADULT, "User 3", "123abcABC!", 0f);
        AccountBase testAccount4 = BankAccountFactory.createAccount(AccountType.ADULT, "User 4", "aBcD123!", 0f);
        AccountBase testAccount5 = BankAccountFactory.createAccount(AccountType.ADULT, "User 5", "P@ssw0rd", 0f);
        AccountBase testAccount6 = BankAccountFactory.createAccount(AccountType.ADULT, "User 6", "P@$$W0RD!", 0f);
        assertAll(
                () -> assertDoesNotThrow(() -> authenticateUserPassword(testAccountAdult, CORRECT_PASSWORD)),
                () -> assertDoesNotThrow(() -> authenticateUserPassword(testAccount2, "Pa$$w0rd")),
                () -> assertDoesNotThrow(() -> authenticateUserPassword(testAccount3, "123abcABC!")),
                () -> assertDoesNotThrow(() -> authenticateUserPassword(testAccount4, "aBcD123!")),
                () -> assertDoesNotThrow(() -> authenticateUserPassword(testAccount5, "P@ssw0rd")),
                () -> assertDoesNotThrow(() -> authenticateUserPassword(testAccount6, "P@$$W0RD!"))
        );
    }

    @Test
    void enforcesPassRulesWhenAuthenticatingStudentPasstWithIncorrectFormat() {
        assertAll(
                () -> assertThrows(RejectedExecutionException.class, () -> authenticateUserPassword(testAccountStudent, INCORRECT_FORMAT)),
                () -> assertThrows(RejectedExecutionException.class, () -> BankAccountFactory.createAccount(AccountType.STUDENT, "User 2", "password", 0f)),
                () -> assertThrows(RejectedExecutionException.class, () -> BankAccountFactory.createAccount(AccountType.STUDENT, "User 3", "p@ssw0rd", 0f)),
                () -> assertThrows(RejectedExecutionException.class, () -> BankAccountFactory.createAccount(AccountType.STUDENT, "User 4", "UPPERCASE", 0f)),
                () -> assertThrows(RejectedExecutionException.class, () -> BankAccountFactory.createAccount(AccountType.STUDENT, "User 5", "12345678", 0f))
        );
    }

    @Test
    void doesNotThrowIfAuthenticatingPasswordWithCorrectFormatStudent() {
        AccountBase testAccount2 = BankAccountFactory.createAccount(AccountType.STUDENT, "User 2", "Pa$$w0rd", 0f);
        AccountBase testAccount3 = BankAccountFactory.createAccount(AccountType.STUDENT, "User 3", "123abcABC!", 0f);
        AccountBase testAccount4 = BankAccountFactory.createAccount(AccountType.STUDENT, "User 4", "aBcD123!", 0f);
        AccountBase testAccount5 = BankAccountFactory.createAccount(AccountType.STUDENT, "User 5", "P@ssw0rd", 0f);
        AccountBase testAccount6 = BankAccountFactory.createAccount(AccountType.STUDENT, "User 6", "P@$$W0RD!", 0f);
        assertAll(
                () -> assertDoesNotThrow(() -> authenticateUserPassword(testAccountStudent, CORRECT_PASSWORD)),
                () -> assertDoesNotThrow(() -> authenticateUserPassword(testAccount2, "Pa$$w0rd")),
                () -> assertDoesNotThrow(() -> authenticateUserPassword(testAccount3, "123abcABC!")),
                () -> assertDoesNotThrow(() -> authenticateUserPassword(testAccount4, "aBcD123!")),
                () -> assertDoesNotThrow(() -> authenticateUserPassword(testAccount5, "P@ssw0rd")),
                () -> assertDoesNotThrow(() -> authenticateUserPassword(testAccount6, "P@$$W0RD!"))
        );
    }
}