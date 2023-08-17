package com.app.core;

import com.app.database.CustomerDAO;
import com.app.database.EmployeeDAO;
import com.app.database.PersonDAO;
import com.app.core.users.Customer;
import com.app.core.users.Employee;
import com.app.core.users.PersonalInformation;

import java.time.LocalDate;

import static com.app.core.FactoryBase.UserType.CUSTOMER;

public class EntityFactory extends FactoryBase {
    private EntityFactory() {

    }
    public static Customer newCustomer(
            String userName, String firstName,
            String lastName, LocalDate dateOfBirth,
            String email
    ) {
        PersonDAO personDAO = new PersonDAO();
        PersonalInformation pi = new PersonalInformation(firstName, lastName, dateOfBirth, email);
        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = new Customer(CUSTOMER, pi, userName);
        // Save to db here
        customer.setPersonId(personDAO.saveNew(pi.getFirstName(), pi.getLastName(), pi.getDateOfBirth(), pi.getEmail()));
        customer.setCustomerId(customerDAO.saveNew(CUSTOMER, customer));
        return customer;
    }

    public static Employee newEmployee(
            UserType userType,
            String firstName, String lastName,
            LocalDate dateOfBirth, String email
    ) {
        PersonDAO personDAO = new PersonDAO();
        PersonalInformation pi = new PersonalInformation(firstName, lastName, dateOfBirth, email);
        EmployeeDAO employeeDAO = new EmployeeDAO();
        Employee employee = new Employee(userType, pi);
        // Save to db here
        employee.setPersonId(personDAO.saveNew(pi.getFirstName(), pi.getLastName(), pi.getDateOfBirth(), pi.getEmail()));
        employee.setEmployeeId(employeeDAO.saveNew(userType, employee));
        return employee;
    }

}
