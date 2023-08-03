package com.main.app.database;

import com.main.app.accounts.AccountType;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public interface DatabaseService {
    public static void updateDatabaseForAccount(int accountNumber, AccountType accountType, Float currentBalance, LocalDate dateCreated, String passwordHash) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        String sql = "INSERT INTO accounts(AccountNumber, AccountType, CurrentBalance, DateCreated, PasswordHash) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = databaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, accountNumber);
            preparedStatement.setString(2, accountType.toString());
            preparedStatement.setFloat(3, currentBalance);
            java.sql.Date sqlDateCreated = java.sql.Date.valueOf(dateCreated);
            preparedStatement.setDate(4, sqlDateCreated);
            preparedStatement.setString(5, passwordHash);
            databaseConnection.executeUpdate(preparedStatement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void updateDatabaseForPerson(String firstName, String lastName, LocalDate dateOfBirth , String email) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        String sql = "INSERT INTO persons(FirstName, LastName, DateOfBirth, Email) VALUES(?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = databaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            java.sql.Date sqlDateOfBirth = java.sql.Date.valueOf(dateOfBirth);
            preparedStatement.setDate(3, sqlDateOfBirth);
            preparedStatement.setString(4, email);
            databaseConnection.executeUpdate(preparedStatement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
