package com.main.app.database;

import com.main.app.core.FactoryBase;
import com.main.app.accounts.*;
import com.main.app.users.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class AccountDAO extends BaseDAO {

    public int saveNew(
            Customer customer, int accountNumber, FactoryBase.AccountType accountType,
            Float currentBalance, LocalDate dateCreated, String passwordHash
    ) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "INSERT INTO accounts(" +
                "AccountNumber, AccountType, CurrentBalance, DateCreated, " +
                "PasswordHash, DateLastUpdated, TimeLastUpdated, CustomerID, PersonID) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int sqlGeneratedAccountId = -1;
        int affectedRows = -1;
        try (PreparedStatement preparedStatement = databaseConnection.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
            preparedStatement.setInt(9, customer.getPersonId());
            affectedRows = databaseConnection.handleUpdate(preparedStatement);
            sqlGeneratedAccountId = getIdFromDatabase(preparedStatement, affectedRows);
        } catch (SQLException e) {
            System.out.println("Error in account DAO: saveNew");
            System.out.println(e.getMessage());
        }
        System.out.printf("Success - %d - rows affected.\n",affectedRows);
        return sqlGeneratedAccountId;
    }

    public AccountBase getAccount(int accountId) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "SELECT a.*, c.*, p.* FROM accounts a JOIN customers c ON a.CustomerID = c.CustomerID " +
                "JOIN persons p ON c.PersonID = p.PersonID WHERE a.AccountID = ?";
        AccountBase account = null;
        try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, accountId);
            ResultSet resultSet = databaseConnection.handleQuery(preparedStatement);
            if (resultSet.next()) {
                String accountType = resultSet.getString("AccountType");
                String passwordHash = resultSet.getString("PasswordHash");
                Float balance = resultSet.getFloat("CurrentBalance");
                String userName = resultSet.getString("UserName");
                if ("STUDENT".equals(accountType)) {
                    account = new StudentAccount(userName, balance, passwordHash);
                }
                if ("ADULT".equals(accountType)) {
                    account = new AdultAccount(userName, balance, passwordHash);
                }
                assert account != null;
                account.setCustomerId(resultSet.getInt("CustomerID"));
                account.setPersonId(resultSet.getInt("CustomerID"));
                account.setAccountNumber(resultSet.getInt("AccountNumber"));
                account.setAccountId(resultSet.getInt("AccountID"));
            }
        } catch(SQLException e){
            System.out.println("Error in account DAO: getAccount");
            System.out.println(e.getMessage());
        }
        return account;
    }

    public void updateAccountBalance(AccountBase account, Float currentBalance) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "UPDATE accounts SET CurrentBalance = ?, dateLastUpdated = ?, timeLastUpdated = ? WHERE AccountID = ?";
        int affectedRows = -1;
        try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setFloat(1, currentBalance);
            java.sql.Date sqlUpdateDate = java.sql.Date.valueOf(LocalDate.now());
            preparedStatement.setDate(2, sqlUpdateDate);
            java.sql.Time sqlUpdateTime = java.sql.Time.valueOf(LocalTime.now());
            preparedStatement.setTime(3, sqlUpdateTime);
            preparedStatement.setInt(4, account.getAccountId());
            affectedRows = databaseConnection.handleUpdate(preparedStatement);
        } catch (SQLException e) {
            System.out.println("Error in account DAO: updateAccountBalance");
            System.out.println(e.getMessage());
        }
        System.out.printf("Success - %d - rows affected.\n",affectedRows);
    }

    public int getAccountIdForCustomerId(int customerId) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "SELECT AccountID FROM accounts WHERE CustomerID = ?";
        int accountId = -1;
        try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = databaseConnection.handleQuery(preparedStatement);
            if (resultSet.next()) {
                accountId = resultSet.getInt("AccountId");
            }
        } catch(SQLException e){
            System.out.println("Error in account DAO: getAccountIdForCustomerId");
            System.out.println(e.getMessage());
        }
        return accountId;
    }
}
