package com.app.core.users;

import java.time.LocalDate;

public class Person {
    private int personId;
    private String firstName;
    private String lastName;
    private final LocalDate dateOfBirth;
    private String email;

    public Person(
            PersonalInformation pi
    ) {
        this.firstName = pi.getFirstName();
        this.lastName = pi.getLastName();
        this.dateOfBirth = pi.getDateOfBirth();
        this.email = pi.getEmail();
        this.personId = -1;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
