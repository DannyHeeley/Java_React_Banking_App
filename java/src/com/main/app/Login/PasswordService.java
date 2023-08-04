package com.main.app.Login;

import com.main.app.accounts.AccountBase;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;

import java.util.concurrent.RejectedExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordService {

    static Pattern passwordPattern = Pattern.compile("^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$");

    public static boolean authenticateUserPassword(AccountBase account, String passwordAttempt) {
        if (account.getAccountPasswordHash() == null) {
            throw new NullPointerException("Password Has Not Been Found");
        } else {
            enforcePasswordRules(passwordAttempt);
            return verifyPassword(account.getAccountPasswordHash(), passwordAttempt.toCharArray());
        }
    }

    public static String hashPassword(String password) {
        enforcePasswordRules(password);
        Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
        return argon2.hash(10, 65536, 1, password.toCharArray());
    }

    private static boolean verifyPassword(String hashedPassword, char[] inputPassword) {
        Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
        return argon2.verify(hashedPassword, inputPassword);
    }
    public static void setPasswordHashForAccount(AccountBase bankAccountToUpdate, String newAccountPassword) {
        bankAccountToUpdate.setAccountPasswordHash(hashPassword(newAccountPassword));
    }

    private static void enforcePasswordRules(String password) {
        //Minimum of 8 characters
        //At least one uppercase letter
        //At least one number
        //At least one special character
        if (password == null || password.equals("")) {
            throw new RejectedExecutionException("Password cannot be null or empty string");
        }
        Matcher matchPass = passwordPattern.matcher(password);
        if (!matchPass.matches()) {
            throw new RejectedExecutionException("Password must be in the following format: Minimum of 8 characters. At least one uppercase letter. At lease one number. At least one special character");
        }
    }
}

