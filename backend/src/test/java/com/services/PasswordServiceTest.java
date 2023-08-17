package com.services;

import com.app.core.accounts.AccountBase;
import com.app.core.accounts.AccountFactory;
import com.app.services.AccountManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.app.services.PasswordService.authenticateUserPassword;
import static com.app.services.PasswordService.hashPassword;

class PasswordServiceTest {
    AccountBase testAccountAdult;
    AccountBase testAccountStudent;
    String userNameAdult = "Adult";
    String userNameStudent = "Student";
    String firstName = "Fizz";
    String lastName = "Buzz";
    LocalDate dateOfBirth = LocalDate.of(1993, 1, 11);
    String email = "fizzbuzz@gmail.com";
    private static final String CORRECT_PASSWORD = "Password123!";
    private static final String INCORRECT_PASSWORD = "Incorrect123!";
    private static final String INCORRECT_FORMAT = "abc";

    @BeforeEach
    void setUp() {
        testAccountAdult = AccountFactory.newAdultAccount(
                userNameAdult, CORRECT_PASSWORD,0f, firstName, lastName, dateOfBirth, email
        );
        testAccountStudent = AccountFactory.newStudentAccount(
                userNameStudent, CORRECT_PASSWORD,0f, firstName, lastName, dateOfBirth, email
        );
    }

    @AfterEach
    void tearDown() {
        AccountManager.getInstance().clearBankAccountList();
        testAccountAdult = null;
        testAccountStudent = null;
    }

    @Test
    void authenticateIsTrueWithCorrectUserPassword() {
        Assertions.assertTrue(authenticateUserPassword(testAccountAdult, CORRECT_PASSWORD));
        Assertions.assertTrue(authenticateUserPassword(testAccountStudent, CORRECT_PASSWORD));
    }

    @Test
    void authenticateIsFalseWithIncorrectUserPassword() {
        Assertions.assertFalse(authenticateUserPassword(testAccountAdult, INCORRECT_PASSWORD));
        Assertions.assertTrue(authenticateUserPassword(testAccountStudent, CORRECT_PASSWORD));
    }

    @Test
    void authenticateIsFalseIfPasswordIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                authenticateUserPassword(testAccountAdult, null));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                authenticateUserPassword(testAccountStudent, null));
    }

    @Test
    void setsPasswordHashForAccount() {
        Assertions.assertNotNull(testAccountAdult.getAccountPasswordHash());
        Assertions.assertNotNull(testAccountStudent.getAccountPasswordHash());
    }

    @Test
    void throwsExceptionIfNewPasswordIsNull() {
        Assertions.assertThrows(RuntimeException.class, () ->
                hashPassword(null)
        );
        Assertions.assertThrows(RuntimeException.class, () ->
                hashPassword(null)
        );
    }

    @Test
    void throwsExceptionIfNewPasswordIsEmptyString() {
        Assertions.assertThrows(RuntimeException.class, () ->
                hashPassword("")
        );
        Assertions.assertThrows(RuntimeException.class, () ->
                hashPassword("")
        );
    }

    @Test
    void enforcesPasswordRulesWhenSettingWithIncorrectFormat() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                hashPassword(INCORRECT_FORMAT)
        );
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                hashPassword(INCORRECT_FORMAT)
        );
    }

    @Test
    void doesNotThrowIfSettingPasswordWithCorrectFormat() {
        Assertions.assertDoesNotThrow(() ->
                hashPassword(CORRECT_PASSWORD)
        );
        Assertions.assertDoesNotThrow(() ->
                hashPassword(CORRECT_PASSWORD)
        );
    }

    @Test
    void enforcesPassRulesWhenAuthenticatingAdultPassWithIncorrectFormat() {
        Assertions.assertAll(
                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        authenticateUserPassword(testAccountAdult, INCORRECT_FORMAT)
                ),

                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        AccountFactory.newAdultAccount(
                                "User 2", "password",
                                0f, firstName, lastName, dateOfBirth, email
                )),

                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        AccountFactory.newAdultAccount(
                                "User 3", "p@ssw0rd",
                                0f, firstName, lastName, dateOfBirth, email
                )),

                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        AccountFactory.newAdultAccount(
                                "User 4", "UPPERCASE",
                                0f, firstName, lastName, dateOfBirth, email
                )),

                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        AccountFactory.newAdultAccount(
                                "User 5", "12345678",
                                0f, firstName, lastName, dateOfBirth, email
                ))
        );
    }

    @Test
    void doesNotThrowIfAuthenticatingAdultPassWithCorrectFormat() {
        AccountBase testAccount2 = AccountFactory.newAdultAccount(
                "User 2", "Pa$$w0rd",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount3 = AccountFactory.newAdultAccount(
                "User 3", "123abcABC!",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount4 = AccountFactory.newAdultAccount(
                "User 4", "aBcD123!",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount5 = AccountFactory.newAdultAccount(
                "User 5", "P@ssw0rd",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount6 = AccountFactory.newAdultAccount(
                "User 6", "P@$$W0RD!",
                0f, firstName, lastName, dateOfBirth, email
        );
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> authenticateUserPassword(testAccountAdult, CORRECT_PASSWORD)),
                () -> Assertions.assertDoesNotThrow(() -> authenticateUserPassword(testAccount2, "Pa$$w0rd")),
                () -> Assertions.assertDoesNotThrow(() -> authenticateUserPassword(testAccount3, "123abcABC!")),
                () -> Assertions.assertDoesNotThrow(() -> authenticateUserPassword(testAccount4, "aBcD123!")),
                () -> Assertions.assertDoesNotThrow(() -> authenticateUserPassword(testAccount5, "P@ssw0rd")),
                () -> Assertions.assertDoesNotThrow(() -> authenticateUserPassword(testAccount6, "P@$$W0RD!"))
        );
    }

    @Test
    void enforcesPassRulesWhenAuthenticatingStudentPassWithIncorrectFormat() {
        Assertions.assertAll(
                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        authenticateUserPassword(testAccountStudent, INCORRECT_FORMAT)
                ),

                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        AccountFactory.newStudentAccount(
                        "User 2", "password",
                                0f, firstName, lastName, dateOfBirth, email
                )),

                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        AccountFactory.newStudentAccount(
                        "User 3", "p@ssw0rd",
                                0f, firstName, lastName, dateOfBirth, email
                )),

                () -> Assertions.assertThrows(IllegalArgumentException.class, () -> AccountFactory.newStudentAccount(
                        "User 4", "UPPERCASE",
                        0f, firstName, lastName, dateOfBirth, email
                )),

                () -> Assertions.assertThrows(IllegalArgumentException.class, () -> AccountFactory.newStudentAccount(
                        "User 5", "12345678",
                        0f, firstName, lastName, dateOfBirth, email
                ))
        );
    }

    @Test
    void doesNotThrowIfAuthenticatingPasswordWithCorrectFormatStudent() {
        AccountBase testAccount2 = AccountFactory.newStudentAccount(
                "User 2", "Pa$$w0rd",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount3 = AccountFactory.newStudentAccount(
                "User 3", "123abcABC!",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount4 = AccountFactory.newStudentAccount(
                "User 4", "aBcD123!",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount5 = AccountFactory.newStudentAccount(
                "User 5", "P@ssw0rd",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount6 = AccountFactory.newStudentAccount(
                "User 6", "P@$$W0RD!",
                0f, firstName, lastName, dateOfBirth, email
        );
        Assertions.assertAll(
                () -> Assertions.assertDoesNotThrow(() -> authenticateUserPassword(testAccountStudent, CORRECT_PASSWORD)),
                () -> Assertions.assertDoesNotThrow(() -> authenticateUserPassword(testAccount2, "Pa$$w0rd")),
                () -> Assertions.assertDoesNotThrow(() -> authenticateUserPassword(testAccount3, "123abcABC!")),
                () -> Assertions.assertDoesNotThrow(() -> authenticateUserPassword(testAccount4, "aBcD123!")),
                () -> Assertions.assertDoesNotThrow(() -> authenticateUserPassword(testAccount5, "P@ssw0rd")),
                () -> Assertions.assertDoesNotThrow(() -> authenticateUserPassword(testAccount6, "P@$$W0RD!"))
        );
    }
}