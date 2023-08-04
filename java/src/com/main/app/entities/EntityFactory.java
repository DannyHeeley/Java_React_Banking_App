package com.main.app.entities;

import com.main.app.FactoryInterface;

import java.time.LocalDate;
import java.util.Objects;

import static com.main.app.entities.EntityType.*;

public class EntityFactory implements FactoryInterface {

    private EntityFactory() {

    }

    public static Employee createEntityForType(
            EntityType entityType,
            String firstName, String lastName,
            LocalDate dateOfBirth, String email
    ) {
        Employee entity = null;
        try {
            entity = handleCreateEntityByTypes(entityType,
                    firstName,
                    lastName,
                    dateOfBirth,
                    email
            );
        } catch (AccountCreationException e) {
            System.out.println("Error Creating Acccount: " + e.getMessage());
        }
        return entity;
    }

    private static Employee handleCreateEntityByTypes(
            EntityType entityType, String firstName,
            String lastName, LocalDate dateOfBirth,
            String email
    )
            throws AccountCreationException {
        if (Objects.equals(entityType, EMPLOYEE)) {
            return employee(EMPLOYEE, firstName, lastName, dateOfBirth, email);
        } else if (Objects.equals(entityType, ADMINISTRATOR)) {
            return employee(ADMINISTRATOR, firstName, lastName, dateOfBirth, email);
        } else if (Objects.equals(entityType, CUSTOMER)) {
            return employee(CUSTOMER, firstName, lastName, dateOfBirth, email);
        }
        throw new AccountCreationException("Account type must be valid");
    }

    private static Employee employee(
            EntityType entityType, String firstName,
            String lastName, LocalDate dateOfBirth, String email
    ) {
        return new Employee(
                entityType,
                firstName,
                lastName,
                dateOfBirth,
                email
        );
    }
}
