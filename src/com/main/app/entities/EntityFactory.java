package com.main.app.entities;

import com.main.app.FactoryBase;
import com.main.app.accounts.PersonalInformation;
import com.main.app.wiring.CustomerDAO;
import com.main.app.wiring.EmployeeDAO;

import java.time.LocalDate;

import static com.main.app.FactoryBase.EntityType.CUSTOMER;

public class EntityFactory extends FactoryBase {
    private EntityFactory() {

    }
    public static Customer newCustomer(
            String userName, String firstName,
            String lastName, LocalDate dateOfBirth,
            String email
    ) {
        PersonalInformation personalInformation = new PersonalInformation(firstName, lastName, dateOfBirth, email);
            Person person = createNewPerson(personalInformation);
            return customer(personalInformation, person, userName);
    }

    public static Employee newEmployee(
            EntityType entityType,
            String firstName, String lastName,
            LocalDate dateOfBirth, String email
    ) {
        PersonalInformation personalInformation = new PersonalInformation(firstName, lastName, dateOfBirth, email);
            Person person = createNewPerson(personalInformation);
            return employee(entityType, personalInformation, person);
    }

    private static Employee employee(
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

    public static Customer customer(PersonalInformation pi, Person person, String userName) {
        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = new Customer(CUSTOMER, pi, userName);
        customer.setCustomerId(customerDAO.saveNew(CUSTOMER, customer, person));
        customer.setPersonId(person.getPersonId());
        return customer;
    }
}
