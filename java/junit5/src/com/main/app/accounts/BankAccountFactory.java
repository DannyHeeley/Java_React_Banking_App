package com.main.app.accounts;

import com.main.app.Bank;
import com.main.app.Login.PasswordService;

public class BankAccountFactory {


    public static AdultAccount createNewAdultAccount(String userName, String password, Float initialDeposit) {
        if (AccountManager.accountExists(userName)) {
            System.out.println("Account already exists");
            return null;
        }
        AdultAccount adultAccount = new AdultAccount(userName, initialDeposit);
        updateBank(initialDeposit, adultAccount);
        PasswordService.setPasswordHashForAccount(adultAccount.getAccountNumber(), password);
        return adultAccount;
    }

    public static StudentAccount createNewStudentAccount(String userName, String password, Float initialDeposit) {
        if (AccountManager.accountExists(userName)) {
            System.out.println("Account already exists");
            return null;
        }
        StudentAccount studentAccount = new StudentAccount(userName, initialDeposit);
        updateBank(initialDeposit, studentAccount);
        PasswordService.setPasswordHashForAccount(studentAccount.getAccountNumber(), password);
        return studentAccount;
    }

    private static void updateBank(Float initialDeposit, AccountBase account) {
        Bank.getInstance().getBankAccounts().add(account);
        Bank.getInstance().updateBalanceDeposit(initialDeposit);
    }


}
