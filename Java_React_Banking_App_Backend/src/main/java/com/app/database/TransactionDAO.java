package com.app.database;

import com.app.core.accounts.AccountBase;
import com.app.core.transactions.TransactionType;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class TransactionDAO extends BaseDAO {
    public int saveNew(AccountBase account, TransactionType transactionType, float amount) {
        String sql = "INSERT INTO transactions(TransactionType, Amount, TransactionDate, TransactionTime, AccountID) VALUES(?, ?, ?, ?, ?)";
        int sqlGeneratedTransactionId = -1;
        int affectedRows = -1;
        try (Connection connection = dbConnPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, transactionType.toString());
            preparedStatement.setFloat(2, amount);
            java.sql.Date dateOfTransaction = java.sql.Date.valueOf(LocalDate.now());
            preparedStatement.setDate(3, dateOfTransaction);
            java.sql.Time timeOfTransaction = java.sql.Time.valueOf(LocalTime.now());
            preparedStatement.setTime(4, timeOfTransaction);
            preparedStatement.setInt(5, account.getAccountId());
            affectedRows = dbConnPool.handleUpdate(preparedStatement);
            sqlGeneratedTransactionId = handlePreparedStatement(preparedStatement, affectedRows);
        } catch (SQLException e) {
            System.out.println("Error in Transaction DAO: saveNew");
            System.out.println(e.getMessage());
        }
        System.out.printf("Success - %d - rows affected.\n",affectedRows);
        return sqlGeneratedTransactionId;
    }
    public List<Map<String, Object>> getTransactions(AccountBase account) {
        String sql = "SELECT * FROM transactions WHERE AccountID = ?";
        List<Map<String, Object>> transactionsList = new ArrayList<>();
        try (Connection connection = dbConnPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, account.getAccountId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int transactionId = resultSet.getInt("TransactionID");
                    int accountId = resultSet.getInt("AccountID");
                    int employeeId = resultSet.getInt("EmployeeID");
                    String transactionType = resultSet.getString("TransactionType");
                    int amount = resultSet.getInt("Amount");
                    LocalDate transactionDate = resultSet.getDate("TransactionDate").toLocalDate();
                    LocalTime transactionTime = resultSet.getTime("TransactionTime").toLocalTime();
                    Map<String, Object> transactionMap = new HashMap<>();
                    transactionMap.put("transactionId", transactionId);
                    transactionMap.put("accountId", accountId);
                    transactionMap.put("employeeId", employeeId);
                    transactionMap.put("transactionType", transactionType);
                    transactionMap.put("amount", amount);
                    transactionMap.put("transactionDate", transactionDate);
                    transactionMap.put("transactionTime", transactionTime);
                    // Add the map to the list
                    transactionsList.add(transactionMap);
                }
            }
        } catch(SQLException e){
            System.out.println("Error in account DAO: getAccount");
            System.out.println(e.getMessage());
        }
        return transactionsList;
    }
}
