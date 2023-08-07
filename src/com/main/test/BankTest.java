package com.main.test;

import com.main.app.Bank;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatObject;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankTest {

    Bank bank;
    @BeforeEach
    void setUp() {
        bank = Bank.getInstance();

        assertThat(bank.getMainBankBalance()).isEqualTo(0f);
    }

    @AfterEach
    void tearDown() {
        bank.resetBank();
    }

    @Test
    void bankIsSingleton() {
        Bank instance1 = Bank.getInstance();
        Bank instance2 = Bank.getInstance();
        assertThatObject(instance1).isEqualTo(instance2);
    }

    @Test
    void returnsBankBalance() {
        assertThat(bank.getMainBankBalance()).isEqualTo(0f);
    }

    @Test
    void resetsBankBalance() {
        bank.updateBankDeposit(1000f);
        bank.resetBank();
        assertThat(bank.getMainBankBalance()).isEqualTo(0f);
    }

    @Test
    void depositIncreasesBankBalance() {
        bank.updateBankDeposit(100f);
        assertThat(bank.getMainBankBalance()).isEqualTo(100f);
    }

    @Test
    void zeroDepositDoesNotChangeBankBalance() {
        bank.updateBankDeposit(0f);
        assertThat(bank.getMainBankBalance()).isEqualTo(0f);
    }

    @Test
    void minusDepositThrowsException() {
        assertThrows(RuntimeException.class, () -> bank.updateBankDeposit(-100f));
    }

    @Test
    void withdrawalDecreasesBankBalance() {
        bank.updateBankDeposit(100f);
        bank.updateBankWithdrawal(50f);
        assertThat(bank.getMainBankBalance()).isEqualTo(50f);
    }

    @Test
    void zer0WithdrawalDoesNotChangeBankBalance() {
        bank.updateBankDeposit(100f);
        bank.updateBankWithdrawal(0f);
        assertThat(bank.getMainBankBalance()).isEqualTo(100f);
    }

    @Test
    void minusWithdrawalThrowsException() {
        bank.updateBankDeposit(100f);
        assertThrows(RuntimeException.class, () -> bank.updateBankWithdrawal(-100f));
    }


}
