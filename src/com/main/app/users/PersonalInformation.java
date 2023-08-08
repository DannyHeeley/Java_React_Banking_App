package com.main.app.users;

import java.time.LocalDate;

public class PersonalInformation {
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    private final String email;
    public PersonalInformation(String firstName, String lastName, LocalDate dateOfBirth, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
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
}
