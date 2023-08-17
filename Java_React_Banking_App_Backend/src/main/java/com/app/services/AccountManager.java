package com.app.services;

import com.app.core.accounts.AccountBase;
import com.app.core.Bank;
import com.app.database.AccountDAO;
import com.app.core.transactions.TransactionType;
import com.app.core.users.Customer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class AccountManager {
    private static AccountManager instance;
    private AccountManager() {

    }
    public static AccountManager getInstance() {
        if (instance == null) {
            instance = new AccountManager();
        } return instance;
    }
    public AccountBase getAccount(int accountId) {
        return Bank.getInstance().getBankAccounts().stream()
                .filter(account -> Objects.equals(account.getAccountId(), accountId))
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("Account does not exist");
                    return null;
                });
    }
    public void addAccount(Customer customer, AccountBase account, float initialDeposit) {
        AccountManager.getInstance().handleNegativeArgument(TransactionType.DEPOSIT, initialDeposit);
        int generatedAccountNumber = generateAccountNumber(customer);
        AccountDAO accountDAO = new AccountDAO();
        account.setAccountNumber(generatedAccountNumber);
        account.setCustomerId(customer.getCustomerId());
        account.setPersonId(customer.getPersonId());
        account.setAccountId(accountDAO.saveNew(customer, account));
        customer.addBankAccountToAccounts(account);
        accountDAO.updateAccountBalance(account, initialDeposit);
        account.getTransactions().addTransaction(TransactionType.DEPOSIT, initialDeposit, account);
        Bank.getInstance().getBankAccounts().add(account);
        Bank.getInstance().updateBankDeposit(initialDeposit);
    }
    public void addToAccountBalance(AccountBase account, float amount) {
        AccountManager.getInstance().handleNegativeArgument(TransactionType.DEPOSIT, amount);
        if (amount > 0) {
            AccountDAO accountDAO = new AccountDAO();
            float accBalance = account.getAccountBalance();
            accBalance += amount;
            account.setAccountBalance(accBalance);
            accountDAO.updateAccountBalance(account, accBalance);
            account.getTransactions().addTransaction(TransactionType.DEPOSIT, amount, account);
            account.setAccountUpdated(LocalDate.now());
            Bank.getInstance().updateBankDeposit(amount);
        }
    }
    public void subtractFromAccountBalance(AccountBase account, float amount) {
        AccountManager.getInstance().handleNegativeArgument(TransactionType.WITHDRAWAL, amount);
        if (amount > 0) {
            AccountDAO accountDAO = new AccountDAO();
        float accBalance = account.getAccountBalance();
        accBalance -= amount;
        account.setAccountBalance(accBalance);
        accountDAO.updateAccountBalance(account, accBalance);
        account.getTransactions().addTransaction(TransactionType.WITHDRAWAL, amount, account);
        account.setAccountUpdated(LocalDate.now());
        Bank.getInstance().updateBankWithdrawal(amount);

        }
    }
    public boolean accountExists(String userName) {
        return Bank.getInstance().getBankAccounts().stream().anyMatch(existingAccount -> Objects.equals(existingAccount.getUserName(), userName));
        // TODO: Check in the database directly if the list might not have the most recent data
    }
    public void clearBankAccountList() {
        Bank.getInstance().setBankAccounts(new ArrayList<>());
    }
    public int generateAccountNumber(Customer customer) {
        String customerId = String.valueOf(customer.getCustomerId());
        String personId = String.valueOf(customer.getPersonId());
        return Integer.parseInt(customerId + personId);
    }
    public void handleNegativeArgument(TransactionType transactionType, float amount) {
        if (amount < 0) {
            if (transactionType == TransactionType.DEPOSIT) {
                throw new IllegalArgumentException("Deposit amount must be a positive number");
            }
            if (transactionType == TransactionType.WITHDRAWAL) {
                throw new IllegalArgumentException("Withdrawal amount must be a positive number");
            }
        }
    }

}
