package com.main.app.accounts;

import com.main.app.database.DatabaseConnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import static java.time.LocalTime.now;

public class AccountManager {

    //    responsible for managing various operations related to accounts,
    //    such as updating account information, handling transactions,
    //    and managing account state.


    // Implement DB for accounts

    private static ArrayList<AccountBase> bankAccounts = new ArrayList<>();

    public static AccountBase getAccount(String userName) {
        return bankAccounts.stream()
                .filter(account -> userNameMatchesAccount(userName, account))
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("Account does not exist");
                    return null;
                });
    }

    public static boolean accountExists(String userName) {
        return bankAccounts.stream().anyMatch(account -> Objects.equals(account.getUserName(), userName));
    }

    public static ArrayList<AccountBase> getBankAccounts() {
        return bankAccounts;
    }

    public static void printAccounts() {
        for (AccountBase account : getBankAccounts()) {
            System.out.print("Account: " + account.getUserName());
            System.out.println(", Created: " + account.getDateAccountCreated());
        }
    }

    public static void clearBankAccountList() {
        bankAccounts = new ArrayList<>();
    }

    private static boolean userNameMatchesAccount(String userName, AccountBase account) {
        return Objects.equals(account.getUserName(), userName);
    }

    public static void updateDatabaseForAccount(int accountNumber, AccountType accountType, Float currentBalance, LocalDate dateCreated) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        String sql = "INSERT INTO accounts(AccountNumber, AccountType, CurrentBalance, DateCreated) VALUES(?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = databaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, accountNumber);
            preparedStatement.setString(2, accountType.toString());
            preparedStatement.setFloat(3, currentBalance);
            java.sql.Date sqlDateCreated = java.sql.Date.valueOf(dateCreated);
            preparedStatement.setDate(4, sqlDateCreated);
            databaseConnection.executeUpdate(preparedStatement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
