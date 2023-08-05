package com.main.test;

import com.main.app.accounts.AdultAccount;
import com.main.app.accounts.PersonalInformation;
import com.main.app.entities.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.main.app.accounts.AccountType.ADULT;
import static org.assertj.core.api.Assertions.assertThat;

class CustomerTest {
    PersonalInformation personalInformation;
    Customer customer;
    AdultAccount account1;
    AdultAccount account2;

    @BeforeEach
    void setUp() {
        personalInformation = new PersonalInformation(
                "Best", "Customer", LocalDate.of(1960, 4, 28), "bestcustomer@gmail.com"
        );
        customer = new Customer(ADULT, personalInformation);
        account1 = new AdultAccount(customer, "BestCustomer", 123456f, "gigachad123!");
    }

    @AfterEach
    void tearDown() {
        personalInformation = null;
        customer = null;
        account1 = null;
        account2 = null;
    }

    @Test
    void addsAccountToCustomerAccounts() {
        customer.addAccount(account1);
        assertThat(customer.getAccounts()).containsOnlyOnce(account1);
    }
    @Test
    void addsMultipleAccountsToCustomerAccounts() {
        account2 = new AdultAccount(customer, "BestCustomerSavings", 999999f, "gigachad123!");
        customer.addAccount(account1);
        customer.addAccount(account2);
        assertThat(customer.getAccounts()).contains(account1, account2);
    }
}