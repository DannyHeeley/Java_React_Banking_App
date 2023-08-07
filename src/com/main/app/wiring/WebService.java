package com.main.app.wiring;

import com.main.app.FactoryBase;
import com.main.app.accounts.*;
import com.main.app.entities.Customer;
import com.main.app.entities.EntityFactory;
import io.javalin.Javalin;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Objects;

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
            Customer customer = EntityFactory.newCustomer(userName, firstName, lastName, dateOfBirth, email);
            AccountBase account = AccountFactory.newAccount(
                    FactoryBase.AccountType.valueOf(ctx.formParam("accountType")),
                    userName,
                    ctx.formParam("password"),
                    Float.parseFloat(Objects.requireNonNull(ctx.formParam("initialDeposit"))),
                    firstName,
                    lastName,
                    dateOfBirth,
                    ctx.formParam("email")
            );
            // Redirect to the profile page
            ctx.redirect("/profile?accountId=" + account.getAccountId() + "&userName=" + customer.getUserName());
        });

        // ----When logging in
        app.post("/login", ctx -> {
            AccountDAO accountDAO = new AccountDAO();
            CustomerDAO customerDAO = new CustomerDAO();
            // Get post request parameters
            String userName = ctx.formParam("userName");
            String password = ctx.formParam("password");
            // Create customer and account objects
            // Fetch data from the database
            int customerId = customerDAO.getCustomerIdForUserName(userName);
            Customer customer = customerDAO.getCustomer(customerId);
            int accountId = accountDAO.getAccountIdForCustomerId(customerId);
            AccountBase account = accountDAO.getAccount(accountId);
            // Authenticate password
            PasswordService.authenticateUserPassword(account, password);
            // Redirect to profile page
            ctx.redirect("/profile?accountId=" + account.getAccountId() + "&userName=" + customer.getUserName());
        });

        // ----Updates HTML page with account details
        app.get("/profile", ctx -> {
            int accountId = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("accountId")));
            String userName = ctx.queryParam("userName");
            AccountDAO accountDAO = new AccountDAO();
            AccountBase account = accountDAO.getAccount(accountId);
            // Read the profile.html as a string
            InputStream profileStream = WebService.class.getClassLoader().getResourceAsStream("public/profile.html");
            if (profileStream == null) {
                throw new FileNotFoundException("Could not find 'profile.html'");
            }
            String profilePageContent = new String(profileStream.readAllBytes(), StandardCharsets.UTF_8);

            // Replace the placeholders with actual data
            profilePageContent = profilePageContent.replace(
                            "{{currentBalance}}", String.valueOf(account.getAccountBalance()))
                    .replace("{{userName}}", String.valueOf(userName)
                            .replace("{{accountId}}", String.valueOf(account.getAccountId()))
                            .replace("{{customerId}}", String.valueOf(account.getCustomerId()))
                            .replace("{{accountNumber}}", String.valueOf(account.getAccountNumber()))
                            .replace("{{accountType}}", String.valueOf(account.getAccountType()))
                            .replace("{{dateCreated}}", String.valueOf(account.getDateAccountCreated()))
                            .replace("{{lastUpdated}}", String.valueOf(account.getAccountUpdated())));
            ctx.html(profilePageContent);
        });

    }
}
