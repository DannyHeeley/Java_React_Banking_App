package com.app.services;

import com.app.core.accounts.AccountBase;
import com.app.core.accounts.AccountFactory;
import com.app.core.FactoryBase;
import com.app.database.AccountDAO;
import com.app.database.CustomerDAO;
import com.app.database.TransactionDAO;
import com.app.core.transactions.TransactionType;
import com.app.core.users.Customer;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static com.app.core.FactoryBase.AccountType.ADULT;
import static com.app.core.FactoryBase.AccountType.STUDENT;

public class EndpointService {

    private EndpointService() {

    }
    static List<Map<String, Object>> getAccountTransactions(AccountBase account) {

        // Create a Map to hold the transactions
        TransactionDAO transactionDAO = new TransactionDAO();
        List<Map<String, Object>> transactionsList = transactionDAO.getTransactions(account);

        // Process the data into a format for the front end
        List<Map<String, Object>> transformedTransactions = new ArrayList<>();
        for (Map<String, Object> transaction : transactionsList) {
            Map<String, Object> transformedTransaction = new HashMap<>();
            transformedTransaction.put("name", transaction.get("transactionType"));
            transformedTransaction.put("value", transaction.get("amount"));
            transformedTransactions.add(transformedTransaction);
        }
        return transformedTransactions;
    }
    static AccountBase getAccountFromDatabaseByUserName(Context ctx, String userName) throws SQLException {
        AccountDAO accountDAO = new AccountDAO();
        CustomerDAO customerDAO = new CustomerDAO();
        // Create customer and account objects
        int customerId = customerDAO.getCustomerIdForUserName(userName);
        if (customerId == -1) {
            ctx.status(400).json(Map.of("message", "Account not found for userName: " + userName));
        }
        Customer customer = customerDAO.getCustomer(customerId);
        if (customer == null) {
            ctx.status(400).json(Map.of("message", "Customer not found for CustomerID: " + customerId));
        }
        int accountId = accountDAO.getAccountIdForCustomer(customer);
        if (accountId == -1) {
            ctx.status(400).json(Map.of("message", "AccountID not found"));
            throw new RuntimeException("AccountID not found for the customerID: " + customerId);
        }
        AccountBase account = accountDAO.getAccountByAccountId(accountId);
        if (account == null) {
            ctx.status(400).json(Map.of("message", "Account not found"));
            throw new SQLException("Account not found for AccountID: " + accountId);
        }
        return account;
    }
    @Nullable
    static AccountBase getExistingAccount(Context ctx) throws SQLException {
        String userName = ctx.sessionAttribute("userName");

        if (userName == null) {
            System.out.println("userName is null");
            ctx.status(401).json(Map.of("message", "User not logged in or session expired."));
            return null;
        }

        AccountBase account = getAccountFromDatabaseByUserName(ctx, userName);
        System.out.println("Account: " + account);
        if (account == null) {
            System.out.println("Account is null");
            ctx.status(401).json(Map.of("message", "User not logged in or session expired."));
            return null;
        }
        return account;
    }
    @NotNull
    static Map<String, Object> buildAccountDetailsMap(AccountBase account) {
        Map<String, Object> accountDetails = new HashMap<>();
        accountDetails.put("currentBalance", account.getAccountBalance());
        accountDetails.put("userName", account.getUserName());
        accountDetails.put("accountId", account.getAccountId());
        accountDetails.put("customerId", account.getCustomerId());
        accountDetails.put("accountNumber", account.getAccountNumber());
        accountDetails.put("accountType", account.getAccountType());
        accountDetails.put("dateCreated", account.getDateAccountCreated());
        accountDetails.put("dateUpdated", account.getAccountUpdated());
        accountDetails.put("timeUpdated", account.getTimeLastUpdated());
        return accountDetails;
    }
    @Nullable
    static AccountBase signUpNewAccount(SignupRequest signupRequest) {
        System.out.println("Received userName: " + signupRequest.getUserName());
        System.out.println("Received firstName: " + signupRequest.getFirstName());
        System.out.println("Received lastName: " + signupRequest.getLastName());
        System.out.println("Received dateOfBirth: " + signupRequest.getDateOfBirth());
        System.out.println("Received email: " + signupRequest.getEmail());
        System.out.println("Received password: " + signupRequest.getPassword());
        System.out.println("Received initialDeposit: " + signupRequest.getInitialDeposit());
        System.out.println("Received accountType: " + signupRequest.getAccountType());

        String userName = signupRequest.getUserName();
        String firstName = signupRequest.getFirstName();
        String lastName = signupRequest.getLastName();
        LocalDate dateOfBirth = LocalDate.parse(Objects.requireNonNull(signupRequest.getDateOfBirth()));
        String email = signupRequest.getEmail();
        String password = signupRequest.getPassword();
        FactoryBase.AccountType accountType = FactoryBase.AccountType.valueOf(signupRequest.getAccountType());
        Float initialDeposit = Float.parseFloat(Objects.requireNonNull(signupRequest.getInitialDeposit()));

        AccountBase account = null;
        if (Objects.equals(accountType, ADULT)) {
            account = AccountFactory.newAdultAccount(
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
                    userName,
                    password,
                    initialDeposit,
                    firstName,
                    lastName,
                    dateOfBirth,
                    email
            );
        }
        return account;
    }
    public static class TransactionRequest {
        private Float amount;
        private TransactionType transactionType;
        public Float getAmount() {
            return amount;
        }
        public void setAmount(Float amount) {
            this.amount = amount;
        }
        public TransactionType getTransactionType() {
            return transactionType;
        }
        public void setTransactionType(TransactionType transactionType) {
            this.transactionType = transactionType;
        }
    }
    public static class TransferRequest{
        private Float transferAmount;
        private int accountNumber;
        private String transactionType;
        public void setTransactionType(String transactionType) {
            this.transactionType = transactionType;
        }
        public void setAccountNumber(int accountNumber) {
            this.accountNumber = accountNumber;
        }
        public void setTransferAmount(Float transferAmount) {
            this.transferAmount = transferAmount;
        }
        public String getTransactionType() {
            return transactionType;
        }
        public int getAccountNumber() {
            return accountNumber;
        }
        public float getTransferAmount() {
            return transferAmount;
        }
    }
    public static class SignupRequest {
        private String userName;
        private String firstName;
        private String lastName;
        private String dateOfBirth;
        private String email;
        private String password;
        private String accountType;
        private String initialDeposit;

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public void setInitialDeposit(String initialDeposit) {
            this.initialDeposit = initialDeposit;
        }

        public String getUserName() {
            return userName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public String getAccountType() {
            return accountType;
        }

        public String getInitialDeposit() {
            return initialDeposit;
        }
    }
    public static class LoginRequest {
        private String userName;
        private String password;
        public String getUserName() {
            return userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }
}
