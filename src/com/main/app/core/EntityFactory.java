package com.main.app.core;

import com.main.app.database.CustomerDAO;
import com.main.app.database.EmployeeDAO;
import com.main.app.database.PersonDAO;
import com.main.app.users.Customer;
import com.main.app.users.Employee;
import com.main.app.users.PersonalInformation;

import java.time.LocalDate;

import static com.main.app.core.FactoryBase.UserType.CUSTOMER;

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
