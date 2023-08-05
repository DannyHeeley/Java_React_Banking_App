package com.main.app.entities;

import com.main.app.FactoryBase;
import com.main.app.accounts.PersonalInformation;

import java.time.LocalDate;
import java.util.Objects;

import static com.main.app.entities.EntityType.*;

public class EntityFactory extends FactoryBase {

    private EntityFactory() {

    }

    public static Employee createEntityForType(
            EntityType entityType,
            String firstName, String lastName,
            LocalDate dateOfBirth, String email
    ) {
        Employee entity = null;
        PersonalInformation personalInformation =
                new PersonalInformation(firstName, lastName, dateOfBirth, email);
        try {
            entity = handleCreateEntityByTypes(entityType, personalInformation);
        } catch (AccountCreationException e) {
            System.out.println("Error Creating Acccount: " + e.getMessage());
        }
        return entity;
    }

    private static Employee handleCreateEntityByTypes(
            EntityType entityType, PersonalInformation personalInformation
    )
            throws AccountCreationException {
        if (Objects.equals(entityType, EMPLOYEE)) {
            return employee(EMPLOYEE, personalInformation);
        } else if (Objects.equals(entityType, ADMINISTRATOR)) {
            return employee(ADMINISTRATOR, personalInformation);
        } else if (Objects.equals(entityType, CUSTOMER)) {
            return employee(CUSTOMER, personalInformation);
        }
        throw new AccountCreationException("Account type must be valid");
    }

    private static Employee employee(
            EntityType entityType, PersonalInformation personalInformation
    ) {
        return new Employee(
                entityType,
                personalInformation
        );
    }
}
