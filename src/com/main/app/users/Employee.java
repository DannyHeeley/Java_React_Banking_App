package com.main.app.users;

import com.main.app.core.FactoryBase;

public class Employee extends Person {
    private FactoryBase.UserType userType;
    private int employeeId;
    public Employee(
            FactoryBase.UserType userType, PersonalInformation personalInformation
    ) {
        super(personalInformation);
        this.userType = userType;
        this.employeeId = -1;
    }
    public FactoryBase.UserType getPosition() {
        return userType;
    }
    public int getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
