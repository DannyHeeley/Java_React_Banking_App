package com.main.app.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class PersonDAO extends BaseDAO {
    public int saveNew(String firstName, String lastName, LocalDate dateOfBirth, String email) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "INSERT INTO persons(FirstName, LastName, DateOfBirth, Email) VALUES(?, ?, ?, ?)";
        int sqlGeneratedPersonId = -1;
        int affectedRows = -1;
        try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            java.sql.Date sqlDateOfBirth = java.sql.Date.valueOf(dateOfBirth);
            preparedStatement.setDate(3, sqlDateOfBirth);
            preparedStatement.setString(4, email);
            affectedRows = databaseConnection.handleUpdate(preparedStatement);
            sqlGeneratedPersonId = getIdFromDatabase(preparedStatement, affectedRows);
        } catch (SQLException e) {
            System.out.println("Error in Person DAO: saveNew");
            System.out.println(e.getMessage());
        }
        System.out.printf("Success - %d - rows affected.\n",affectedRows);
        return sqlGeneratedPersonId;
    }
}
