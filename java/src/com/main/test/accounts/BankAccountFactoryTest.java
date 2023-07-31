package com.main.test.accounts;

import com.main.app.Bank;
import com.main.app.accounts.AccountBase;
import com.main.app.accounts.BankAccountFactory;
import com.main.app.accounts.BankAccountFactory.AccountCreationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.main.app.accounts.AccountType.ADULT;
import static com.main.app.accounts.AccountType.STUDENT;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BankAccountFactoryTest {
    Bank bank;
    @BeforeEach
    void setUp() {
        bank = Bank.getInstance();
        bank.resetBank();
    }

    // Test cases for updateBank:
    // 1. Test updating the bank with a positive initial deposit
    // 2. Test updating the bank with an initial deposit of 0
    // 3. Test updating the bank with a negative initial deposit (should not change the bank balance)
    // 4. Test updating the bank with a null account (should not add anything to the bank accounts)
    @Test
    void increaseBankBalanceWithPositiveInitialDeposit() {
        BankAccountFactory.createAccount(ADULT, "Foo Bar", "password", 100f);
        assertThat(bank.getBankBalance()).isEqualTo(100f);
    }

    @Test
    void bankBalanceSameWithZeroInitialDeposit() {
        BankAccountFactory.createAccount(ADULT, "Foo Bar", "password", 0f);
        assertThat(bank.getBankBalance()).isEqualTo(0f);
    }

    @Test
    void bankBalanceSameWithNegativeInitialDeposit() {
        BankAccountFactory.createAccount(ADULT, "Foo Bar", "password", 1000f);
        assertThat(bank.getBankBalance()).isEqualTo(0f);
    }

    @Test
    void bankBalanceSameWithNullAccount() {
        BankAccountFactory.createAccount(ADULT, "Foo Bar", "password", 0f);
        BankAccountFactory.createAccount(ADULT, "Foo Bar", "password",  1000f);
        assertThat(bank.getBankBalance()).isEqualTo(0f);
    }

    // Test cases for createNewAdultAccount:
    // 1. Test creating a new adult account with a positive initial deposit
    // 2. Test creating a new adult account with an initial deposit of 0
    // 3. Test creating a new adult account with a negative initial deposit (should throw an exception)
    // 4. Test creating a new adult account with an existing username (should throw an exception)

    // Test cases for createNewStudentAccount:
    // 1. Test creating a new student account with a positive initial deposit
    // 2. Test creating a new student account with an initial deposit of 0
    // 3. Test creating a new student account with a negative initial deposit (should throw an exception)
    // 4. Test creating a new student account with an existing username (should throw an exception)

    @Test
    void createsAdultAccount() {
        AccountBase account = BankAccountFactory.createAccount(ADULT, "An Adult", "password", 0f);
        assertThat(account).isNotNull();
    }

    @Test
    void createsStudentAccount() {
        AccountBase account = BankAccountFactory.createAccount(STUDENT, "A Student", "password", 0f);
        assertThat(account).isNotNull();
    }

    @Test
    void throwsExceptionIfAccountExistsWithSameUserName() {
        BankAccountFactory.createAccount(STUDENT, "A Student", "password", 0f);
        assertThrows(RuntimeException.class, () -> BankAccountFactory.createAccount(STUDENT, "A Student", "password", 0f));
    }

    @Test
    void doesNotThrowExceptionIfAccountsHaveUniqueUserNames() {
        assertDoesNotThrow(() -> BankAccountFactory.createAccount(STUDENT, "A Student", "password", 0f));
        assertDoesNotThrow(() -> BankAccountFactory.createAccount(STUDENT, "Another Student", "password", 0f));
    }


}
