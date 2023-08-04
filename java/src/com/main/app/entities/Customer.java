package com.main.app.entities;

import java.time.LocalDate;

public class Customer extends Person {

    protected Customer(String firstName, String lastName, LocalDate dateOfBirth, String email) {
        super(firstName, lastName, dateOfBirth, email);
    }

}
