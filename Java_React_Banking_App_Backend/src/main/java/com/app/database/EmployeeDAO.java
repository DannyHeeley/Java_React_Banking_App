package com.app.database;

import com.app.core.FactoryBase;
import com.app.core.users.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class EmployeeDAO extends BaseDAO {
    public int saveNew(FactoryBase.UserType position, Employee employee) {
        String sql = "INSERT INTO employees(Position, PersonID) VALUES(?, ?)";
        int sqlGeneratedEmployeeId = -1;
        int affectedRows = -1;
        try (Connection connection = dbConnPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, position.toString());
            preparedStatement.setInt(2, employee.getPersonId());
            affectedRows = dbConnPool.handleUpdate(preparedStatement);
            sqlGeneratedEmployeeId = handlePreparedStatement(preparedStatement, affectedRows);
        } catch (SQLException e) {
            System.out.println("Error in Employee DAO: saveNew");
            System.out.println(e.getMessage());
        }
        System.out.printf("Success - %d - rows affected.\n",affectedRows);
        return sqlGeneratedEmployeeId;
    }
}
