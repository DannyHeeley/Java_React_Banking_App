package com.main.test.login;

import com.main.app.accounts.AccountBase;
import com.main.app.accounts.AccountManager;
import com.main.app.accounts.AccountType;
import com.main.app.accounts.BankAccountFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.RejectedExecutionException;

import static com.main.app.Login.PasswordService.*;
import static org.junit.jupiter.api.Assertions.*;

class PasswordServiceTest {
    AccountBase testAccount;
    int accountNumber;
    private static final String CORRECT_PASSWORD = "Password123!";
    private static final String INCORRECT_PASSWORD = "Incorrect123!";
    private static final String INCORRECT_FORMAT = "abc";

    @BeforeEach
    void setUp() {
        testAccount = BankAccountFactory.createAccount(AccountType.ADULT, "Test User", CORRECT_PASSWORD, 0f);
        accountNumber = testAccount.getAccountNumber();
    }

    @AfterEach
    void tearDown() {
        AccountManager.clearBankAccountList();
    }

    @Test
    void authenticateIsTrueWithCorrectUserPassword() {
        assertTrue(authenticateUserPassword(testAccount, CORRECT_PASSWORD));
    }

    @Test
    void authenticateIsFalseWithIncorrectUserPassword() {
        assertFalse(authenticateUserPassword(testAccount, INCORRECT_PASSWORD));
    }

    @Test
    void authenticateIsFalseIfPasswordIsNull() {
        assertThrows(RejectedExecutionException.class, () -> authenticateUserPassword(testAccount, null));
    }

    @Test
    void setsPasswordHashForAccount() {
        assertTrue(testAccount.getAccountPasswordHash() != null);
    }

    @Test
    void throwsExceptionIfNewPasswordIsNull() {
        assertThrows(RuntimeException.class, () -> setPasswordHashForAccount(testAccount, null));
    }

    @Test
    void throwsExceptionIfNewPasswordIsEmptyString() {
        assertThrows(RuntimeException.class, () -> setPasswordHashForAccount(testAccount, ""));
    }

    @Test
    void enforcesPasswordRulesWhenSettingWithIncorrectFormat() {
        assertThrows(RejectedExecutionException.class, () -> setPasswordHashForAccount(testAccount, INCORRECT_FORMAT));
    }

    @Test
    void doesNotThrowIfSettingPasswordWithCorrectFormat() {
        assertDoesNotThrow(() -> setPasswordHashForAccount(testAccount, CORRECT_PASSWORD));
    }

    @Test
    void enforcesPasswordRulesWhenAuthenticatingWithIncorrectFormat() {
        assertAll(
                () -> assertThrows(RejectedExecutionException.class, () -> authenticateUserPassword(testAccount, INCORRECT_FORMAT)),
                () -> assertThrows(RejectedExecutionException.class, () -> BankAccountFactory.createAccount(AccountType.ADULT, "User 2", "password", 0f)),
                () -> assertThrows(RejectedExecutionException.class, () -> BankAccountFactory.createAccount(AccountType.ADULT, "User 3", "p@ssw0rd", 0f)),
                () -> assertThrows(RejectedExecutionException.class, () -> BankAccountFactory.createAccount(AccountType.ADULT, "User 4", "UPPERCASE", 0f)),
                () -> assertThrows(RejectedExecutionException.class, () -> BankAccountFactory.createAccount(AccountType.ADULT, "User 5", "12345678", 0f))
        );
    }

    @Test
    void doesNotThrowIfAuthenticatingPasswordWithCorrectFormat() {
        AccountBase testAccount2 = BankAccountFactory.createAccount(AccountType.ADULT, "User 2", "Pa$$w0rd", 0f);
        AccountBase testAccount3 = BankAccountFactory.createAccount(AccountType.ADULT, "User 3", "123abcABC!", 0f);
        AccountBase testAccount4 = BankAccountFactory.createAccount(AccountType.ADULT, "User 4", "aBcD123!", 0f);
        AccountBase testAccount5 = BankAccountFactory.createAccount(AccountType.ADULT, "User 5", "P@ssw0rd", 0f);
        AccountBase testAccount6 = BankAccountFactory.createAccount(AccountType.ADULT, "User 6", "P@$$W0RD!", 0f);
        assertAll(
                () -> assertDoesNotThrow(() -> authenticateUserPassword(testAccount, CORRECT_PASSWORD)),
                () -> assertDoesNotThrow(() -> authenticateUserPassword(testAccount2, "Pa$$w0rd")),
                () -> assertDoesNotThrow(() -> authenticateUserPassword(testAccount3, "123abcABC!")),
                () -> assertDoesNotThrow(() -> authenticateUserPassword(testAccount4, "aBcD123!")),
                () -> assertDoesNotThrow(() -> authenticateUserPassword(testAccount5, "P@ssw0rd")),
                () -> assertDoesNotThrow(() -> authenticateUserPassword(testAccount6, "P@$$W0RD!"))
        );
    }

}