package com.main.test;

import com.main.app.*;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class BankTest {

    Bank bank;
    @BeforeEach
    void setUp() {
        bank = Bank.getInstance();
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
        assertThat(bank.getBankBalance()).isEqualTo(0f);
    }

    @Test
    void resetsBankBalance() {
        bank.updateBalanceDeposit(1000f);
        bank.resetBank();
        assertThat(bank.getBankBalance()).isEqualTo(0f);
    }

    @Test
    void depositIncreasesBankBalance() {
        bank.updateBalanceDeposit(100f);
        assertThat(bank.getBankBalance()).isEqualTo(100f);
    }

    @Test
    void withdrawalDecreasesBankBalance() {
        bank.updateBalanceDeposit(100f);
        bank.updateBalanceWithdrawal(50f);
        assertThat(bank.getBankBalance()).isEqualTo(50f);
    }

}
