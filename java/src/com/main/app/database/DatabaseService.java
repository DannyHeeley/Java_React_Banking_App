package com.main.app.database;

import com.main.app.accounts.AccountBase;
import com.main.app.accounts.AccountType;
import com.main.app.accounts.Person;
import com.main.app.transactions.TransactionType;
import org.apache.tomcat.jni.Local;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

//Should not be an interface but an abstract or concrete class

public interface DatabaseService {

    static int addPersonEntryToDatabase(String firstName, String lastName, LocalDate dateOfBirth , String email) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "INSERT INTO persons(FirstName, LastName, DateOfBirth, Email) VALUES(?, ?, ?, ?)";
        int sqlGeneratedPersonId = -1;
        try (PreparedStatement preparedStatement = databaseConnection.getDatabaseConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            java.sql.Date sqlDateOfBirth = java.sql.Date.valueOf(dateOfBirth);
            preparedStatement.setDate(3, sqlDateOfBirth);
            preparedStatement.setString(4, email);
            int affectedRows = databaseConnection.handleUpdate(preparedStatement);
            sqlGeneratedPersonId = getIdFromDatabase(preparedStatement, affectedRows);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return sqlGeneratedPersonId;
    }

    static int addAccountEntryToDatabase(AccountBase account, int accountNumber, AccountType accountType, Float currentBalance, LocalDate dateCreated, String passwordHash) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "INSERT INTO accounts(AccountNumber, AccountType, CurrentBalance, DateCreated, PasswordHash, LastUpdated, PersonId) VALUES(?, ?, ?, ?, ?, ?, ?)";
        int sqlGeneratedAccountId = -1;
        try (PreparedStatement preparedStatement = databaseConnection.getDatabaseConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, accountNumber);
            preparedStatement.setString(2, accountType.toString());
            preparedStatement.setFloat(3, currentBalance);
            java.sql.Date sqlDateCreated = java.sql.Date.valueOf(dateCreated);
            preparedStatement.setDate(4, sqlDateCreated);
            preparedStatement.setString(5, passwordHash);
            java.sql.Date sqlDateLastUpdated = java.sql.Date.valueOf(LocalDate.now());
            preparedStatement.setDate(6, sqlDateLastUpdated);
            java.sql.Time sqlTimeLastUpdated = java.sql.Time.valueOf(LocalTime.now());
            preparedStatement.setTime(7, sqlTimeLastUpdated);
            preparedStatement.setInt(8, account.getPersonId());
            int affectedRows = databaseConnection.handleUpdate(preparedStatement);
            sqlGeneratedAccountId = getIdFromDatabase(preparedStatement, affectedRows);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return sqlGeneratedAccountId;
    }

    static int addTransactionEntryToDatabase(AccountBase account, TransactionType transactionType, Float amount) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "INSERT INTO transactions(TransactionType, Amount, TransactionDate, TransactionTime, AccountID) VALUES(?, ?, ?, ?, ?)";
        int sqlGeneratedTransactionId = -1;
        try (PreparedStatement preparedStatement = databaseConnection.getDatabaseConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, transactionType.toString());
            preparedStatement.setFloat(2, amount);
            java.sql.Date dateOfTransaction = java.sql.Date.valueOf(LocalDate.now());
            preparedStatement.setDate(3, dateOfTransaction);
            java.sql.Time timeOfTransaction = java.sql.Time.valueOf(LocalTime.now());
            preparedStatement.setTime(4, timeOfTransaction);
            preparedStatement.setInt(5, account.getAccountId());
            int affectedRows = databaseConnection.handleUpdate(preparedStatement);
            sqlGeneratedTransactionId = getIdFromDatabase(preparedStatement, affectedRows);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return sqlGeneratedTransactionId;
    }

    static void updateAccountBalanceInDatabase(AccountBase account, Float currentBalance) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "UPDATE accounts SET CurrentBalance = ?, dateLastUpdated = ?, timeLastUpdated = ? WHERE AccountID = ?";
        try (PreparedStatement preparedStatement = databaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            preparedStatement.setFloat(1, currentBalance);
            java.sql.Date sqlUpdateDate = java.sql.Date.valueOf(LocalDate.now());
            preparedStatement.setDate(2, sqlUpdateDate);
            java.sql.Time sqlUpdateTime = java.sql.Time.valueOf(LocalTime.now());
            preparedStatement.setTime(3, sqlUpdateTime);
            preparedStatement.setInt(4, account.getAccountId());
            databaseConnection.handleUpdate(preparedStatement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static int getIdFromDatabase(PreparedStatement preparedStatement, int affectedRows) throws SQLException {
        if (affectedRows == 0) {
            throw new SQLException("Creating account failed, no rows affected.");
        }
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating account failed, no ID obtained.");
            }
        }
    }

//    static ResultSet getAllAccountsFromDatabase() {
//        DatabaseConnection databaseConnection = new DatabaseConnection();
//        String sql = "SELECT * FROM accounts";
//        try (PreparedStatement preparedStatement = databaseConnection.getDatabaseConnection().prepareStatement(sql)) {
//             return databaseConnection.handleQuery(preparedStatement);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return null;
//    }
}
