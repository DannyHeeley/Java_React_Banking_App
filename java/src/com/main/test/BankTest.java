package com.main.test;

import com.main.app.Bank;
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
        bank.resetBank();
        assertThat(bank.getBankBalance()).isEqualTo(0f);
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
    void zeroDepositDoesNotChangeBankBalance() {
        bank.updateBalanceDeposit(0f);
        assertThat(bank.getBankBalance()).isEqualTo(0f);
    }

    @Test
    void minusDepositThrowsException() {
        assertThrows(RuntimeException.class, () -> bank.updateBalanceDeposit(-100f));
    }

    @Test
    void withdrawalDecreasesBankBalance() {
        bank.updateBalanceDeposit(100f);
        bank.updateBalanceWithdrawal(50f);
        assertThat(bank.getBankBalance()).isEqualTo(50f);
    }

    @Test
    void zer0WithdrawalDoesNotChangeBankBalance() {
        bank.updateBalanceDeposit(100f);
        bank.updateBalanceWithdrawal(0f);
        assertThat(bank.getBankBalance()).isEqualTo(100f);
    }

    @Test
    void minusWithdrawalThrowsException() {
        bank.updateBalanceDeposit(100f);
        assertThrows(RuntimeException.class, () -> bank.updateBalanceWithdrawal(-100f));
    }


}
