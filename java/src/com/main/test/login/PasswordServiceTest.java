package com.main.test.login;

import com.main.app.accounts.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static com.main.app.accounts.AccountType.ADULT;
import static com.main.app.accounts.AccountType.STUDENT;
import static com.main.app.login.PasswordService.*;
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
        testAccountAdult = AccountFactory.createAccount(
                ADULT, userNameAdult, CORRECT_PASSWORD,0f, firstName, lastName, dateOfBirth, email
        );
        testAccountStudent = AccountFactory.createAccount(
                STUDENT, userNameStudent, CORRECT_PASSWORD,0f, firstName, lastName, dateOfBirth, email
        );
    }

    @AfterEach
    void tearDown() {
        AccountManager.clearBankAccountList();
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
                        AccountFactory.createAccount(
                        ADULT, "User 2", "password",
                                0f, firstName, lastName, dateOfBirth, email
                )),

                () -> assertThrows(IllegalArgumentException.class, () ->
                        AccountFactory.createAccount(
                        ADULT, "User 3", "p@ssw0rd",
                                0f, firstName, lastName, dateOfBirth, email
                )),

                () -> assertThrows(IllegalArgumentException.class, () ->
                        AccountFactory.createAccount(
                        ADULT, "User 4", "UPPERCASE",
                                0f, firstName, lastName, dateOfBirth, email
                )),

                () -> assertThrows(IllegalArgumentException.class, () ->
                        AccountFactory.createAccount(
                        ADULT, "User 5", "12345678",
                                0f, firstName, lastName, dateOfBirth, email
                ))
        );
    }

    @Test
    void doesNotThrowIfAuthenticatingAdultPassWithCorrectFormat() {
        AccountBase testAccount2 = AccountFactory.createAccount(
                ADULT, "User 2", "Pa$$w0rd",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount3 = AccountFactory.createAccount(
                ADULT, "User 3", "123abcABC!",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount4 = AccountFactory.createAccount(
                ADULT, "User 4", "aBcD123!",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount5 = AccountFactory.createAccount(
                ADULT, "User 5", "P@ssw0rd",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount6 = AccountFactory.createAccount(
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
                        AccountFactory.createAccount(
                        STUDENT, "User 2", "password",
                                0f, firstName, lastName, dateOfBirth, email
                )),

                () -> assertThrows(IllegalArgumentException.class, () ->
                        AccountFactory.createAccount(
                        STUDENT, "User 3", "p@ssw0rd",
                                0f, firstName, lastName, dateOfBirth, email
                )),

                () -> assertThrows(IllegalArgumentException.class, () -> AccountFactory.createAccount(
                        STUDENT, "User 4", "UPPERCASE",
                        0f, firstName, lastName, dateOfBirth, email
                )),

                () -> assertThrows(IllegalArgumentException.class, () -> AccountFactory.createAccount(
                        STUDENT, "User 5", "12345678",
                        0f, firstName, lastName, dateOfBirth, email
                ))
        );
    }

    @Test
    void doesNotThrowIfAuthenticatingPasswordWithCorrectFormatStudent() {
        AccountBase testAccount2 = AccountFactory.createAccount(
                STUDENT, "User 2", "Pa$$w0rd",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount3 = AccountFactory.createAccount(
                STUDENT, "User 3", "123abcABC!",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount4 = AccountFactory.createAccount(
                STUDENT, "User 4", "aBcD123!",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount5 = AccountFactory.createAccount(
                STUDENT, "User 5", "P@ssw0rd",
                0f, firstName, lastName, dateOfBirth, email
        );
        AccountBase testAccount6 = AccountFactory.createAccount(
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