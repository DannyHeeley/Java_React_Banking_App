package com.main.test.accounts;

import com.main.app.accounts.AccountBase;
import com.main.app.accounts.AccountFactory;
import com.main.app.accounts.AccountManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.main.app.accounts.AccountType.ADULT;
import static com.main.app.accounts.AccountType.STUDENT;
import static com.main.app.accounts.PasswordService.authenticateUserPassword;
import static com.main.app.accounts.PasswordService.hashPassword;
import static org.junit.jupiter.api.Assertions.*;

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
        testAccountAdult = AccountFactory.createAccountNewUser(
                ADULT, userNameAdult, CORRECT_PASSWORD,0f, firstName, lastName, dateOfBirth, email
        );
        testAccountStudent = AccountFactory.createAccountNewUser(
                STUDENT, userNameStudent, CORRECT_PASSWORD,0f, firstName, lastName, dateOfBirth, email
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
        assertThrows(IllegalArgumentException.class, () ->
                authenticateUserPassword(testAccountAdult, null));
        assertThrows(IllegalArgumentException.class, () ->
                authenticateUserPassword(testAccountStudent, null));
    }

    @Test
    void setsPasswordHashForAccount() {
        assertNotNull(testAccountAdult.getAccountPasswordHash());
        assertNotNull(testAccountStudent.getAccountPasswordHash());
    }

    @Test
    void throwsExceptionIfNewPasswordIsNull() {
        assertThrows(RuntimeException.class, () ->
                hashPassword(null)
        );
        assertThrows(RuntimeException.class, () ->
                hashPassword(null)
        );
    }

    @Test
    void throwsExceptionIfNewPasswordIsEmptyString() {
        assertThrows(RuntimeException.class, () ->
                hashPassword("")
        );
        assertThrows(RuntimeException.class, () ->
                hashPassword("")
        );
    }

    @Test
    void enforcesPasswordRulesWhenSettingWithIncorrectFormat() {
        assertThrows(IllegalArgumentException.class, () ->
                hashPassword(INCORRECT_FORMAT)
        );
        assertThrows(IllegalArgumentException.class, () ->
                hashPassword(INCORRECT_FORMAT)
        );
    }

    @Test
    void doesNotThrowIfSettingPasswordWithCorrectFormat() {
        assertDoesNotThrow(() ->
                hashPassword(CORRECT_PASSWORD)
        );
        assertDoesNotThrow(() ->
                hashPassword(CORRECT_PASSWORD)
        );
    }

    @Test
    void enforcesPassRulesWhenAuthenticatingAdultPassWithIncorrectFormat() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () ->
                        authenticateUserPassword(testAccountAdult, INCORRECT_FORMAT)
                ),

                () -> assertThrows(IllegalArgumentException.class, () ->
                        AccountFactory.createAccountNewUser(
                        ADULT, "User 2", "password",
                                0f, firstName, lastName, dateOfBirth, email
                )),

                () -> assertThrows(IllegalArgumentException.class, () ->
                        AccountFactory.createAccountNewUser(
                        ADULT, "User 3", "p@ssw0rd",
                                0f, firstName, lastName, dateOfBirth, email
                )),

                () -> assertThrows(IllegalArgumentException.class, () ->
                        AccountFactory.createAccountNewUser(
                        ADULT, "User 4", "UPPERCASE",
                                0f, firstName, lastName, dateOfBirth, email
                )),

                () -> assertThrows(IllegalArgumentException.class, () ->
                        AccountFactory.createAccountNewUser(
                        ADULT, "User 5", "12345678",
                                0f, firstName, lastName, dateOfBirth, email
                ))
        );
    }

    @Test
    void doesNotThrowIfAuthenticatingAdultPassWithCorrectFormat() {
        AccountBase testAccount2 = AccountFactory.createAccountNewUser(
                ADULT, "User 2", "Pa$$w0rd",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount3 = AccountFactory.createAccountNewUser(
                ADULT, "User 3", "123abcABC!",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount4 = AccountFactory.createAccountNewUser(
                ADULT, "User 4", "aBcD123!",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount5 = AccountFactory.createAccountNewUser(
                ADULT, "User 5", "P@ssw0rd",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount6 = AccountFactory.createAccountNewUser(
                ADULT, "User 6", "P@$$W0RD!",
                0f, firstName, lastName, dateOfBirth, email
        );
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
    void enforcesPassRulesWhenAuthenticatingStudentPassWithIncorrectFormat() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () ->
                        authenticateUserPassword(testAccountStudent, INCORRECT_FORMAT)
                ),

                () -> assertThrows(IllegalArgumentException.class, () ->
                        AccountFactory.createAccountNewUser(
                        STUDENT, "User 2", "password",
                                0f, firstName, lastName, dateOfBirth, email
                )),

                () -> assertThrows(IllegalArgumentException.class, () ->
                        AccountFactory.createAccountNewUser(
                        STUDENT, "User 3", "p@ssw0rd",
                                0f, firstName, lastName, dateOfBirth, email
                )),

                () -> assertThrows(IllegalArgumentException.class, () -> AccountFactory.createAccountNewUser(
                        STUDENT, "User 4", "UPPERCASE",
                        0f, firstName, lastName, dateOfBirth, email
                )),

                () -> assertThrows(IllegalArgumentException.class, () -> AccountFactory.createAccountNewUser(
                        STUDENT, "User 5", "12345678",
                        0f, firstName, lastName, dateOfBirth, email
                ))
        );
    }

    @Test
    void doesNotThrowIfAuthenticatingPasswordWithCorrectFormatStudent() {
        AccountBase testAccount2 = AccountFactory.createAccountNewUser(
                STUDENT, "User 2", "Pa$$w0rd",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount3 = AccountFactory.createAccountNewUser(
                STUDENT, "User 3", "123abcABC!",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount4 = AccountFactory.createAccountNewUser(
                STUDENT, "User 4", "aBcD123!",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount5 = AccountFactory.createAccountNewUser(
                STUDENT, "User 5", "P@ssw0rd",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount6 = AccountFactory.createAccountNewUser(
                STUDENT, "User 6", "P@$$W0RD!",
                0f, firstName, lastName, dateOfBirth, email
        );
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