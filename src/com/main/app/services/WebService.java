package com.main.app.services;

import com.main.app.core.FactoryBase;
import com.main.app.accounts.*;
import com.main.app.database.AccountDAO;
import com.main.app.database.CustomerDAO;
import com.main.app.users.Customer;

import io.javalin.Javalin;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

import static com.main.app.core.FactoryBase.AccountType.ADULT;
import static com.main.app.core.FactoryBase.AccountType.STUDENT;

public class WebService {

    public static WebService instance;
    private WebService() {}

    public static WebService getInstance() {
        if (instance == null) {
            instance = new WebService();
        } return  instance;
    }

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.addStaticFiles(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/public";
            });
            config.enableCorsForAllOrigins();
            config.enforceSsl = true;
            config.asyncRequestTimeout = 10_000L;
        }).start(7070);

        // ----When creating a new account
        app.post("/signup", ctx -> {
            String userName = ctx.formParam("userName");
            String firstName = ctx.formParam("firstName");
            String lastName = ctx.formParam("lastName");
            LocalDate dateOfBirth = LocalDate.parse(Objects.requireNonNull(ctx.formParam("dateOfBirth")));
            String email = ctx.formParam("email");
            String password = ctx.formParam("password");
            FactoryBase.AccountType accountType = FactoryBase.AccountType.valueOf(ctx.formParam("accountType"));
            Float initialDeposit = Float.parseFloat(Objects.requireNonNull(ctx.formParam("initialDeposit")));
            AccountBase account = null;
            if (Objects.equals(accountType, ADULT)) {
                account = AccountFactory.newAdultAccount(
                        accountType,
                        userName,
                        password,
                        initialDeposit,
                        firstName,
                        lastName,
                        dateOfBirth,
                        email
                );
            }
            if (Objects.equals(accountType, STUDENT)) {
                account = AccountFactory.newStudentAccount(
                        accountType,
                        userName,
                        password,
                        initialDeposit,
                        firstName,
                        lastName,
                        dateOfBirth,
                        email
                );
            }
            // Redirect to the profile page
            assert account != null;
            ctx.redirect("/profile?accountId=" + account.getAccountId() + "&userName=" + userName);
        });

        // ----When logging in
        app.post("/login", ctx -> {
            try {
                AccountDAO accountDAO = new AccountDAO();
                CustomerDAO customerDAO = new CustomerDAO();
                // Get post request parameters
                String userName = ctx.formParam("userName");
                String password = ctx.formParam("password");
                // Authenticate password

                // Create customer and account objects
                // Fetch data from the database
                int customerId = customerDAO.getCustomerIdForUserName(userName);
                if (customerId == -1) {
                    ctx.status(400).result("Account not found");
                    throw new SQLException("Account not found for userName: " + userName);
                }
                Customer customer = customerDAO.getCustomer(customerId);
                if (customer == null) {
                    ctx.status(400).result("Customer not found");
                    throw new SQLException("Customer not found for CustomerID: " + customerId);
                }
                int accountId = accountDAO.getAccountIdForCustomerId(customerId);
                if (accountId == -1) {
                    ctx.status(400).result("AccountID not found");
                    throw new RuntimeException("AccountID not found for the customerID: " + customerId);
                }
                AccountBase account = accountDAO.getAccount(accountId);
                if (account == null) {
                    ctx.status(400).result("Account not found");
                    throw new SQLException("Account not found for AccountID: " + accountId);
                }
                if (PasswordService.authenticateUserPassword(account, password)) {
                    // Redirect to profile page
                    ctx.redirect("/profile?accountId=" + accountId + "&userName=" + userName);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).result("An error occurred while processing the request.");
            }
        });

        // ----Updates HTML page with account details
        app.get("/profile", ctx -> {
            try {
                int accountId = Integer.parseInt(ctx.queryParam("accountId"));
                String userName = ctx.queryParam("userName");
                AccountDAO accountDAO = new AccountDAO();
                AccountBase account = accountDAO.getAccount(accountId);
                if (account == null) {
                    throw new RuntimeException("Account not found for ID: " + accountId);
                }
                // Read the profile.html as a string
                InputStream profileStream = WebService.class.getClassLoader().getResourceAsStream("public/profile.html");
                if (profileStream == null) {
                    throw new FileNotFoundException("Could not find 'profile.html'");
                }
                String profilePageContent = new String(profileStream.readAllBytes(), StandardCharsets.UTF_8);
                // Replace the placeholders with actual data
                profilePageContent = profilePageContent
                        .replace("{{currentBalance}}", String.valueOf(account.getAccountBalance()))
                        .replace("{{userName}}", String.valueOf(userName))
                        .replace("{{accountId}}", String.valueOf(account.getAccountId()))
                        .replace("{{customerId}}", String.valueOf(account.getCustomerId()))
                        .replace("{{accountNumber}}", String.valueOf(account.getAccountNumber()))
                        .replace("{{accountType}}", String.valueOf(account.getAccountType()))
                        .replace("{{dateCreated}}", String.valueOf(account.getDateAccountCreated()))
                        .replace("{{dateUpdated}}", String.valueOf(account.getAccountUpdated()))
                        .replace("{{timeUpdated}}", String.valueOf(account.getTimeLastUpdated()));
                ctx.html(profilePageContent);
            } catch (Exception e) {
                // log the error for debugging
                e.printStackTrace();
                ctx.status(500).result("An error occurred while processing the request.");
            }
        });

    }
}
