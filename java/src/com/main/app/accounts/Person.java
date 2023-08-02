package com.main.app.accounts;

import com.main.app.database.DatabaseConnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Person {
    private int personId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;

    public Person(
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String email
    ) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        updateDatabaseForPerson(firstName, lastName, dateOfBirth, email);
    }

    public static void updateDatabaseForPerson(String firstName, String lastName, LocalDate dateOfBirth , String email) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        String sql = "INSERT INTO persons(FirstName, LastName, DateOfBirth, Email) VALUES(?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = databaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            java.sql.Date sqlDateOfBirth = java.sql.Date.valueOf(dateOfBirth);
            preparedStatement.setDate(3, sqlDateOfBirth);
            preparedStatement.setString(4, email);
            databaseConnection.executeUpdate(preparedStatement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPersonId() {
        return personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

}
