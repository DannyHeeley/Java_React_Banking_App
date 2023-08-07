package com.main.app.entities;

import com.main.app.accounts.PersonalInformation;

public class Employee extends Person {
    private EntityType entityType;
    private int employeeId;
    private int personId;

    public Employee(
            EntityType entityType, PersonalInformation personalInformation
    ) {
        super(
                personalInformation
        );
        this.entityType = entityType;
        this.employeeId = -1;
        this.personId = -1;
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
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setPersonid(int newPersonId) {
        this.personId = personId;
    }
}
