package com.main.app.wiring;

import com.main.app.accounts.AccountBase;
import com.main.app.entities.Customer;
import com.main.app.transactions.TransactionType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

public class TransactionDAO extends BaseDAO {
    public int saveNew(int accountId, TransactionType transactionType, Float amount) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "INSERT INTO transactions(TransactionType, Amount, TransactionDate, TransactionTime, AccountID) VALUES(?, ?, ?, ?, ?)";
        int sqlGeneratedTransactionId = -1;
        int affectedRows = -1;
        try (PreparedStatement preparedStatement = databaseConnection.getDatabaseConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, transactionType.toString());
            preparedStatement.setFloat(2, amount);
            java.sql.Date dateOfTransaction = java.sql.Date.valueOf(LocalDate.now());
            preparedStatement.setDate(3, dateOfTransaction);
            java.sql.Time timeOfTransaction = java.sql.Time.valueOf(LocalTime.now());
            preparedStatement.setTime(4, timeOfTransaction);
            preparedStatement.setInt(5, accountId);
            affectedRows = databaseConnection.handleUpdate(preparedStatement);
            sqlGeneratedTransactionId = getIdFromDatabase(preparedStatement, affectedRows);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.printf("Success - %d - rows affected.\n",affectedRows);
        return sqlGeneratedTransactionId;
    }
}
