package com.main.app.Login;

import com.main.app.Bank;
import com.main.app.accounts.AccountBase;
import com.main.app.accounts.AccountManager;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;

public class PasswordService {

    public static boolean authenticateUserPassword(String accountPasswordHash, String userPassword) {
        if (accountPasswordHash != null) {
            return verifyPassword(accountPasswordHash, userPassword.toCharArray());
        }
        System.out.println("Password Has Not Been Set");
        return false;
    }

    public static void setPasswordHashForAccount(int accountNumber, String password) {
        PasswordService passwordService = new PasswordService();
        for (AccountBase account: AccountManager.getBankAccounts()) {
            if (account.getAccountNumber() == accountNumber) {
                account.setAccountPasswordHash(passwordService.hashPassword(password));
            }
        }
    }

    private String hashPassword(String password) {
        Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
        return argon2.hash(10, 65536, 1, password.toCharArray());
    }

    private static boolean verifyPassword(String hashedPassword, char[] inputPassword) {
        Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
        return argon2.verify(hashedPassword, inputPassword);
    }

}

