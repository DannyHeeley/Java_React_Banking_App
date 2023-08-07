package com.main.app.wiring;

import com.main.app.accounts.AccountBase;
import com.main.app.entities.Employee;
import com.main.app.entities.EntityType;
import com.main.app.entities.Person;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerDAO extends BaseDAO {

    public int saveCustomer(EntityType customerType, Person person) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "INSERT INTO customers(CustomerType, PersonID) VALUES(?, ?)";
        int sqlGeneratedCustomerId = -1;
        int affectedRows = -1;
        try (PreparedStatement preparedStatement = databaseConnection.getDatabaseConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, customerType.toString());
            preparedStatement.setInt(2, person.getPersonId());
            affectedRows = databaseConnection.handleUpdate(preparedStatement);
            sqlGeneratedCustomerId = getIdFromDatabase(preparedStatement, affectedRows);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.printf("Success - %d - rows affected.\n",affectedRows);
        return sqlGeneratedCustomerId;
    }

    public int getCustomerIdForAccount(AccountBase account) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "SELECT * FROM accounts WHERE AccountID = ?";
        int customerId = -1;
        try (PreparedStatement preparedStatement = databaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            preparedStatement.setFloat(1, account.getAccountId());
            ResultSet resultSet = databaseConnection.handleQuery(preparedStatement);
            if (resultSet.next()) {
                customerId = resultSet.getInt("CustomerID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customerId;
    }
}
