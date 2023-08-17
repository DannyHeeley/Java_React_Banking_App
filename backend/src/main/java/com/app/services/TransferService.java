package com.app.services;

import com.app.core.accounts.AccountBase;
import com.app.database.AccountDAO;
import com.app.core.transactions.TransactionType;

public class TransferService {

    private TransferService() {
    }
    public static void transferBetweenAccounts(AccountBase sendingAccount, AccountBase receivingAccount, Float amount) {
        AccountDAO accountDAO = new AccountDAO();
        float sendingAccountBalance = accountDAO.getAccountBalance(sendingAccount);
        float receivingAccountBalance = accountDAO.getAccountBalance(receivingAccount);

        if (amount > sendingAccountBalance) {
            System.out.println("Balance too low to transfer the specified amount");
        } else {
            float sendingAccountNewBalance = sendingAccountBalance - amount;
            float receivingAccountNewBalance = receivingAccountBalance + amount;
            try {
                // Update balance in sending account object
                sendingAccount.setAccountBalance(sendingAccountNewBalance);
                // Update sending account balance in database
                accountDAO.updateAccountBalance(sendingAccount, sendingAccountNewBalance);
                System.out.println("Sending account expected new balance: " + sendingAccountNewBalance);
                System.out.println("Sending account actual new balance: " + accountDAO.getAccountBalance(sendingAccount));

                // Update balance in receiving account object
                receivingAccount.setAccountBalance(receivingAccountNewBalance);
                // Update receiving account balance in database
                accountDAO.updateAccountBalance(receivingAccount, receivingAccountNewBalance);
                System.out.println("Receiving account expected new balance: " + receivingAccountNewBalance);
                System.out.println("Receiving account actual new balance: " + accountDAO.getAccountBalance(receivingAccount));

                // Saves a new transaction to account and database for both accounts
                sendingAccount.getTransactions().addTransaction(TransactionType.TRANSFER_OUT, amount, sendingAccount);
                receivingAccount.getTransactions().addTransaction(TransactionType.TRANSFER_IN, amount, receivingAccount);
            } catch (Exception e) {
                System.out.println("Transfer failed: " + e);
            }
            System.out.println("Transfer succesful for amount: " + amount);
        }
    }
}