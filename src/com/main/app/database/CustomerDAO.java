package com.main.app.database;

import com.main.app.core.FactoryBase;
import com.main.app.users.PersonalInformation;
import com.main.app.users.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class CustomerDAO extends BaseDAO {

    public int saveNew(FactoryBase.UserType customerType, Customer customer) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "INSERT INTO customers(CustomerType, PersonID, UserName) VALUES(?, ?, ?)";
        int sqlGeneratedCustomerId = -1;
        int affectedRows = -1;
        try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, customerType.toString());
            preparedStatement.setInt(2, customer.getPersonId());
            preparedStatement.setString(3, customer.getUserName());
            affectedRows = databaseConnection.handleUpdate(preparedStatement);
            sqlGeneratedCustomerId = getIdFromDatabase(preparedStatement, affectedRows);
        } catch (SQLException e) {
            System.out.println("Error in customer DAO: saveNew");
            System.out.println(e.getMessage());
        }
        System.out.printf("Success - %d - rows affected.\n",affectedRows);
        return sqlGeneratedCustomerId;
    }

    public Customer getCustomer(int customerId) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "SELECT c.*, p.* FROM customers c JOIN persons p ON c.PersonID = p.PersonID WHERE c.CustomerID = ?";
        Customer customer = null;
        try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = databaseConnection.handleQuery(preparedStatement);
            if (resultSet.next()) {
                String customerType = resultSet.getString("CustomerType");
                String userName = resultSet.getString("UserName");
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                LocalDate dateOfBirth = resultSet.getDate("DateOfBirth").toLocalDate();
                String email = resultSet.getString("Email");
                int personId = resultSet.getInt("PersonID");
                PersonalInformation pi = new PersonalInformation(firstName, lastName, dateOfBirth, email);
                customer = new Customer(FactoryBase.UserType.valueOf(customerType), pi, userName);
                customer.setPersonId(personId);
                customer.setCustomerId(customerId);
            }
        } catch(SQLException e){
            System.out.println("Error in customer DAO: getCustomer");
            System.out.println(e.getMessage());
        }
        return customer;
    }

    public int getCustomerIdForUserName(String userName) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "SELECT CustomerID FROM customers WHERE UserName = ?";
        int customerId = -1;
        try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, userName);
            ResultSet resultSet = databaseConnection.handleQuery(preparedStatement);
            if (resultSet.next()) {
                customerId = resultSet.getInt("CustomerID");
            }
        } catch (SQLException e) {
            System.out.println("Error in customer DAO: getCustomerIdForUserName");
            System.out.println(e.getMessage());
        }
        return customerId;
    }
}
