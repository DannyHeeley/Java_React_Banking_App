package com.main.app.entities;

import com.main.app.accounts.PersonalInformation;
import com.main.app.database.DatabaseService;


public class Employee extends Person {
    private EntityType entityType;
    private final int employeeId;

    public Employee(
            EntityType entityType, PersonalInformation personalInformation
    ) {
        super(
                personalInformation
        );
        this.entityType = entityType;
        this.employeeId = DatabaseService.addEmployeeEntryToDatabase(
                entityType,
                this
        );
    }

    public EntityType getPosition() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public int getEmployeeId() {
        return employeeId;
    }
}
