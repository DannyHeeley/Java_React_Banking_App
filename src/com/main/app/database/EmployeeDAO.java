package com.main.app.database;

import com.main.app.core.FactoryBase;
import com.main.app.users.Employee;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class EmployeeDAO extends BaseDAO {
    public int saveNew(FactoryBase.UserType position, Employee employee) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "INSERT INTO employees(Position, PersonID) VALUES(?, ?)";
        int sqlGeneratedEmployeeId = -1;
        int affectedRows = -1;
        try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, position.toString());
            preparedStatement.setInt(2, employee.getPersonId());
            affectedRows = databaseConnection.handleUpdate(preparedStatement);
            sqlGeneratedEmployeeId = getIdFromDatabase(preparedStatement, affectedRows);
        } catch (SQLException e) {
            System.out.println("Error in Employee DAO: saveNew");
            System.out.println(e.getMessage());
        }
        System.out.printf("Success - %d - rows affected.\n",affectedRows);
        return sqlGeneratedEmployeeId;
    }
}
