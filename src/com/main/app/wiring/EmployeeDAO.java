package com.main.app.wiring;

import com.main.app.FactoryBase;
import com.main.app.entities.Person;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class EmployeeDAO extends BaseDAO {
    public int saveNew(FactoryBase.EntityType position, Person person) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "INSERT INTO employees(Position, PersonID) VALUES(?, ?)";
        int sqlGeneratedEmployeeId = -1;
        int affectedRows = -1;
        try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, position.toString());
            preparedStatement.setInt(2, person.getPersonId());
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
