package com.main.app.database;

import com.main.app.accounts.AccountBase;
import com.main.app.accounts.AccountType;
import com.main.app.accounts.Person;
import com.main.app.transactions.TransactionType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

public interface DatabaseService {

    static int updateDatabaseForAccount(int accountNumber, AccountType accountType, Float currentBalance, LocalDate dateCreated, String passwordHash) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        String sql = "INSERT INTO accounts(AccountNumber, AccountType, CurrentBalance, DateCreated, PasswordHash, LastUpdated, PersonId) VALUES(?, ?, ?, ?, ?, ?, ?)";
        int SqlGeneratedAccountId = -1;
        try (PreparedStatement preparedStatement = databaseConnection.getDatabaseConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, accountNumber);
            preparedStatement.setString(2, accountType.toString());
            preparedStatement.setFloat(3, currentBalance);
            java.sql.Date sqlDateCreated = java.sql.Date.valueOf(dateCreated);
            preparedStatement.setDate(4, sqlDateCreated);
            preparedStatement.setString(5, passwordHash);
            java.sql.Date sqlLastUpdated = java.sql.Date.valueOf(dateCreated);
            preparedStatement.setDate(6, sqlLastUpdated);
            preparedStatement.setInt(7, Person.getPersonId());
            databaseConnection.executeUpdate(preparedStatement);
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    SqlGeneratedAccountId = generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Creating account failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return SqlGeneratedAccountId;
    }

    static int updateDatabaseForPerson(String firstName, String lastName, LocalDate dateOfBirth , String email) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        String sql = "INSERT INTO persons(FirstName, LastName, DateOfBirth, Email) VALUES(?, ?, ?, ?)";
        int SqlGeneratedPersonId = -1;
        try (PreparedStatement preparedStatement = databaseConnection.getDatabaseConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            java.sql.Date sqlDateOfBirth = java.sql.Date.valueOf(dateOfBirth);
            preparedStatement.setDate(3, sqlDateOfBirth);
            preparedStatement.setString(4, email);
            int affectedRows = databaseConnection.executeUpdate(preparedStatement);
            if (affectedRows == 0) {
                throw new SQLException("Creating account failed, no rows affected.");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    SqlGeneratedPersonId = generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Creating account failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
            return SqlGeneratedPersonId;
    }

    static int updateDatabaseForTransaction(TransactionType transactionType, Float amount, LocalDate transactionDate, LocalTime transactionTime) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        String sql = "INSERT INTO transactions(TransactionType, Amount, TransactionDate, TransactionTime, AccountID) VALUES(?, ?, ?, ?, ?)";
        int SqlGeneratedTransactionId = -1;
        try (PreparedStatement preparedStatement = databaseConnection.getDatabaseConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, transactionType.toString());
            preparedStatement.setFloat(2, amount);
            java.sql.Date dateOfTransaction = java.sql.Date.valueOf(transactionDate);
            preparedStatement.setDate(3, dateOfTransaction);
            java.sql.Time timeOfTransaction = java.sql.Time.valueOf(transactionTime);
            preparedStatement.setTime(4, timeOfTransaction);
            preparedStatement.setInt(5, AccountBase.getAccountId());
            databaseConnection.executeUpdate(preparedStatement);
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    SqlGeneratedTransactionId = generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Creating account failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return SqlGeneratedTransactionId;
    }

//    static ResultSet getAllAccountsFromDatabase() {
//        DatabaseConnection databaseConnection = new DatabaseConnection();
//        String sql = "SELECT * FROM accounts";
//        try (PreparedStatement preparedStatement = databaseConnection.getDatabaseConnection().prepareStatement(sql)) {
//             return databaseConnection.executeQuery(preparedStatement);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return null;
//    }
}
