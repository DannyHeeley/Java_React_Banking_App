package com.app.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class PersonDAO extends BaseDAO {
    public int saveNew(String firstName, String lastName, LocalDate dateOfBirth, String email) {
        String sql = "INSERT INTO persons(FirstName, LastName, DateOfBirth, Email) VALUES(?, ?, ?, ?)";
        int sqlGeneratedPersonId = -1;
        int affectedRows = -1;
        try (Connection connection = dbConnPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            java.sql.Date sqlDateOfBirth = java.sql.Date.valueOf(dateOfBirth);
            preparedStatement.setDate(3, sqlDateOfBirth);
            preparedStatement.setString(4, email);
            affectedRows = dbConnPool.handleUpdate(preparedStatement);
            sqlGeneratedPersonId = handlePreparedStatement(preparedStatement, affectedRows);
        } catch (SQLException e) {
            System.out.println("Error in Person DAO: saveNew");
            System.out.println(e.getMessage());
        }
        System.out.printf("Success - %d - rows affected.\n",affectedRows);
        return sqlGeneratedPersonId;
    }
}
