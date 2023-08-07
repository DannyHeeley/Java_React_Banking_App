package com.main.app.wiring;

import com.main.app.accounts.AccountBase;
import com.main.app.accounts.AccountType;
import com.main.app.entities.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

public class AccountDAO extends BaseDAO {

    public int saveNew(
            Customer customer, int accountNumber, AccountType accountType, Float currentBalance, LocalDate dateCreated, String passwordHash
    ) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "INSERT INTO accounts(AccountNumber, AccountType, CurrentBalance, DateCreated, PasswordHash, DateLastUpdated, TimeLastUpdated, CustomerID) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        int sqlGeneratedAccountId = -1;
        int affectedRows = -1;
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
            preparedStatement.setInt(8, customer.getCustomerId());
            affectedRows = databaseConnection.handleUpdate(preparedStatement);
            sqlGeneratedAccountId = getIdFromDatabase(preparedStatement, affectedRows);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.printf("Success - %d - rows affected.\n",affectedRows);
        return sqlGeneratedAccountId;
    }

    public static void updateAccountBalanceInDatabase(AccountBase account, Float currentBalance) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "UPDATE accounts SET CurrentBalance = ?, dateLastUpdated = ?, timeLastUpdated = ? WHERE AccountID = ?";
        int affectedRows = -1;
        try (PreparedStatement preparedStatement = databaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            preparedStatement.setFloat(1, currentBalance);
            java.sql.Date sqlUpdateDate = java.sql.Date.valueOf(LocalDate.now());
            preparedStatement.setDate(2, sqlUpdateDate);
            java.sql.Time sqlUpdateTime = java.sql.Time.valueOf(LocalTime.now());
            preparedStatement.setTime(3, sqlUpdateTime);
            preparedStatement.setInt(4, account.getAccountId());
            affectedRows = databaseConnection.handleUpdate(preparedStatement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.printf("Success - %d - rows affected.\n",affectedRows);
    }

    public static ResultSet getAllAccountsFromDatabase() {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "SELECT * FROM accounts";
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = databaseConnection.getDatabaseConnection().prepareStatement(sql)) {
             resultSet = databaseConnection.handleQuery(preparedStatement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return resultSet;
    }

    public int getAccountIdForCustomer(Customer customer) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        int customerID = customer.getCustomerId();
        String sql = "SELECT AccountID FROM customers WHERE CustomerID = ?";
        int accountId = -1;
        try (PreparedStatement preparedStatement = databaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, customerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                accountId = resultSet.getInt("AccountID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return accountId;
    }
}
