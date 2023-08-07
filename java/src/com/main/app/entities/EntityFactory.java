package com.main.app.entities;

import com.main.app.FactoryBase;
import com.main.app.accounts.PersonalInformation;
import com.main.app.wiring.EmployeeDAO;

import java.time.LocalDate;

public class EntityFactory extends FactoryBase {

    private EntityFactory() {

    }

    public static Employee createEntityForType(
            EntityType entityType,
            String firstName, String lastName,
            LocalDate dateOfBirth, String email
    ) {
        PersonalInformation personalInformation = new PersonalInformation(firstName, lastName, dateOfBirth, email);
        Person person = createNewPerson(personalInformation);
        return createNewEmployee(entityType, personalInformation, person);
    }

    private static Employee createNewEmployee(
            EntityType entityType, PersonalInformation personalInformation, Person person
    ) {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        Employee employee = new Employee(entityType, personalInformation);
        employee.setPersonid(person.getPersonId());
        employeeDAO.saveNew(
                entityType,
                person
        );
        return employee;
    }
}
