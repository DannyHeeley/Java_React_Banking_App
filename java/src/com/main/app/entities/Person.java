package com.main.app.entities;

import com.main.app.database.DatabaseService;

import java.time.LocalDate;

public class Person implements DatabaseService {
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
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.personId = DatabaseService.addPersonEntryToDatabase(firstName, lastName, dateOfBirth, email);
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

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }
}
