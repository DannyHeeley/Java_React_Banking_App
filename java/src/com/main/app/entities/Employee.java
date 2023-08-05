package com.main.app.entities;

import com.main.app.accounts.PersonalInformation;
import com.main.app.database.DatabaseService;

import java.time.LocalDate;


public class Employee extends Person {
    private EntityType position;
    private final int employeeId;

    public Employee(
            EntityType position, PersonalInformation personalInformation
    ) {
        super(
                personalInformation
        );
        this.position = position;
        this.employeeId = DatabaseService.addEmployeeEntryToDatabase(
                position,
                this
        );
    }

    public EntityType getPosition() {
        return position;
    }

    public void setPosition(EntityType position) {
        this.position = position;
    }

    public int getEmployeeId() {
        return employeeId;
    }
}
