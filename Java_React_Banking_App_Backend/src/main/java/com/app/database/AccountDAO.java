package com.app.database;

import com.app.core.accounts.AccountBase;
import com.app.core.accounts.AdultAccount;
import com.app.core.accounts.StudentAccount;
import com.app.core.users.Customer;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class AccountDAO extends BaseDAO {

    public int saveNew(Customer customer, AccountBase account) {
        String sql = "INSERT INTO accounts(" +
                "AccountNumber, AccountType, CurrentBalance, DateCreated, " +
                "PasswordHash, DateLastUpdated, TimeLastUpdated, CustomerID, PersonID) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int sqlGeneratedAccountId = -1;
        int affectedRows = -1;
        try (Connection connection = dbConnPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setInt(1, account.getAccountNumber());
            preparedStatement.setString(2, account.getAccountType().toString());
            preparedStatement.setFloat(3, account.getAccountBalance());
            java.sql.Date sqlDateCreated = java.sql.Date.valueOf(LocalDate.now());
            preparedStatement.setDate(4, sqlDateCreated);
            preparedStatement.setString(5, account.getAccountPasswordHash());
            java.sql.Date sqlDateLastUpdated = java.sql.Date.valueOf(LocalDate.now());
            preparedStatement.setDate(6, sqlDateLastUpdated);
            java.sql.Time sqlTimeLastUpdated = java.sql.Time.valueOf(LocalTime.now());
            preparedStatement.setTime(7, sqlTimeLastUpdated);
            preparedStatement.setInt(8, customer.getCustomerId());
            preparedStatement.setInt(9, customer.getPersonId());
            affectedRows = dbConnPool.handleUpdate(preparedStatement);
            sqlGeneratedAccountId = handlePreparedStatement(preparedStatement, affectedRows);
        } catch (SQLException e) {
            System.out.println("Error in account DAO: saveNew");
            System.out.println(e.getMessage());
        }
        System.out.printf("Success - %d - rows affected.\n",affectedRows);
        return sqlGeneratedAccountId;
    }
    public AccountBase getAccountByAccountId(int accountId) {
        String sql = "SELECT a.*, c.*, p.* FROM accounts a JOIN customers c ON a.CustomerID = c.CustomerID " +
                "JOIN persons p ON c.PersonID = p.PersonID WHERE a.AccountID = ?";
        AccountBase account = null;
        try (
                Connection connection = dbConnPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, accountId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String accountType = resultSet.getString("AccountType");
                    String passwordHash = resultSet.getString("PasswordHash");
                    float balance = resultSet.getFloat("CurrentBalance");
                    String userName = resultSet.getString("UserName");

                    switch(accountType) {
                        case "STUDENT":
                            account = new StudentAccount(userName, balance, passwordHash);
                            break;
                        case "ADULT":
                            account = new AdultAccount(userName, balance, passwordHash);
                            break;
                        default:
                            throw new SQLException("Account not found or type unknown for ID: " + accountId);
                    }

                    account.setCustomerId(resultSet.getInt("CustomerID"));
                    account.setPersonId(resultSet.getInt("PersonID"));
                    account.setAccountNumber(resultSet.getInt("AccountNumber"));
                    account.setAccountId(resultSet.getInt("AccountID"));
                    account.setDateCreated(resultSet.getDate("DateCreated").toLocalDate());
                    account.setAccountUpdated(resultSet.getDate("DateLastUpdated").toLocalDate());
                    account.setTimeLastUpdated(resultSet.getTime("TimeLastUpdated").toLocalTime());
                }
            }
        } catch(SQLException e) {
            System.out.println("Error in account DAO: getAccountByAccountId");
            e.printStackTrace();
        }
        return account;
    }
    public AccountBase getAccountByAccountNumber(int accountNumber) {
        String sql = "SELECT a.*, c.*, p.* FROM accounts a JOIN customers c ON a.CustomerID = c.CustomerID " +
                "JOIN persons p ON c.PersonID = p.PersonID WHERE a.AccountNumber = ?";
        AccountBase account = null;
        try (Connection connection = dbConnPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, accountNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String accountType = resultSet.getString("AccountType");
                    String passwordHash = resultSet.getString("PasswordHash");
                    float balance = resultSet.getFloat("CurrentBalance");
                    String userName = resultSet.getString("UserName");
                    if (Objects.equals(accountType, "STUDENT")) {
                        account = new StudentAccount(userName, balance, passwordHash);
                    }
                    if (Objects.equals(accountType, "ADULT")) {
                        account = new AdultAccount(userName, balance, passwordHash);
                    }
                    if (account == null) {
                        throw new SQLException("Account not found for Number: " + account);
                    }
                    account.setCustomerId(resultSet.getInt("CustomerID"));
                    account.setPersonId(resultSet.getInt("PersonID"));
                    account.setAccountNumber(resultSet.getInt("AccountNumber"));
                    account.setAccountId(resultSet.getInt("AccountID"));
                    account.setDateCreated(resultSet.getDate("DateCreated").toLocalDate());
                    account.setAccountUpdated(resultSet.getDate("DateLastUpdated").toLocalDate());
                    account.setTimeLastUpdated(resultSet.getTime("TimeLastUpdated").toLocalTime());
                }
            }
        } catch(SQLException e){
            System.out.println("Error in account DAO: getAccountByAccountNumber");
            System.out.println(e.getMessage());
        }
        return account;
    }
    public float getAccountBalance(AccountBase account) {
        String sql = "SELECT CurrentBalance FROM accounts WHERE AccountID = ?";
        float balance = 0;
        try (Connection connection = dbConnPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, account.getAccountId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    balance = resultSet.getFloat("CurrentBalance");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in account DAO: getAccountBalance");
            System.out.println(e.getMessage());
        }
        return balance;
    }
    public void updateAccountBalance(AccountBase account, float currentBalance) {
        String sql = "UPDATE accounts SET CurrentBalance = ?, dateLastUpdated = ?, timeLastUpdated = ? WHERE AccountID = ?";
        int affectedRows = -1;
        try (Connection connection = dbConnPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setFloat(1, currentBalance);
            java.sql.Date sqlUpdateDate = java.sql.Date.valueOf(LocalDate.now());
            preparedStatement.setDate(2, sqlUpdateDate);
            java.sql.Time sqlUpdateTime = java.sql.Time.valueOf(LocalTime.now());
            preparedStatement.setTime(3, sqlUpdateTime);
            preparedStatement.setInt(4, account.getAccountId());
            affectedRows = dbConnPool.handleUpdate(preparedStatement);
        } catch (SQLException e) {
            System.out.println("Error in account DAO: updateAccountBalance");
            System.out.println(e.getMessage());
            return;
        }
        if (affectedRows <= 0) {
            System.out.println("No rows affected in updateAccountBalance. Possible issue with AccountID or the data provided.");
            return;
        }
        System.out.printf("Success - %d - rows affected.\n", affectedRows);
    }
    public int getAccountIdForCustomer(Customer customer) {
        String sql = "SELECT AccountID FROM accounts WHERE CustomerID = ?";
        int accountId = -1;
        try (Connection connection = dbConnPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, customer.getCustomerId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    accountId = resultSet.getInt("AccountID");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in account DAO: getAccountIdForCustomerId");
            System.out.println(e.getMessage());
        }
        return accountId;
    }
}