package com.app.services;

import com.app.core.accounts.AccountBase;
import com.app.database.AccountDAO;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;

import java.io.Serializable;
import java.util.*;

public class EndpointManager implements Serializable {
    public static final String ERROR_MESSAGE = "An error occurred while processing the request.";
    public static EndpointManager instance;
    private EndpointManager() {}
    public static EndpointManager getInstance() {
        if (instance == null) {
            instance = new EndpointManager();
        } return  instance;
    }

    public static void main(String[] args) {

        Javalin app = Javalin.create(config ->
                config.plugins.enableCors(cors ->
                        cors.add(it -> {
                            it.allowHost("http://localhost:3000/", "http://localhost:7070/");
                            it.allowCredentials = true;
                            it.exposeHeader("x-server");
                        })
                )
        ).start(7070);

// ---- When creating a new account
        app.post("/signup", ctx -> {
            try {
                EndpointService.SignupRequest signupRequest = ctx.bodyAsClass(EndpointService.SignupRequest.class);
                AccountBase account = EndpointService.signUpNewAccount(signupRequest);
                if (account == null) {
                    System.out.println("Account is null at signup");
                    ctx.status(500).json(Map.of("message", "Account is null"));
                    return;
                }
                String userName = signupRequest.getUserName();
                ctx.sessionAttribute("userName", userName);
                ctx.status(201).json(Map.of("message", "Signup successful!"));
            } catch (Exception e){
                e.printStackTrace();
                ctx.status(500).json(Map.of("message", "Failed to create an account"));
            }
        });

// ---- When logging in
        app.post("/login", ctx -> {
            try {
                System.out.println("A /login post request received");
                System.out.println("/login: Session ID: " + ctx.req().getSession().getId());
                // Get post request parameters
                EndpointService.LoginRequest loginRequest = ctx.bodyAsClass(EndpointService.LoginRequest.class);
                System.out.println("/login: Received userName: " + loginRequest.getUserName());
                System.out.println("/login: Received password: " + loginRequest.getPassword());
                String userName = loginRequest.getUserName();
                String password = loginRequest.getPassword();
                AccountBase account = EndpointService.getAccountFromDatabaseByUserName(ctx, userName);
                System.out.println("/login: Account: " + account);
                // Authenticate password
                if (PasswordService.authenticateUserPassword(account, password)) {
                    ctx.sessionAttribute("userName", userName);
                    ctx.status(200).json(Map.of("message", "Login successful!"));
                } else {
                    System.out.println("Invalid credentials");
                    ctx.status(401).json(Map.of("message", "Invalid Credentials"));
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).json(Map.of("message", ERROR_MESSAGE));
            }
        });

// ---- When depositing
        app.post("/deposit", ctx -> {
            try {
                System.out.println("A /deposit get request received");
                System.out.println("/transfers: Session ID: " + ctx.req().getSession().getId());

                EndpointService.TransactionRequest transactionRequest = ctx.bodyAsClass(EndpointService.TransactionRequest.class);
                Float amount = transactionRequest.getAmount();
                if (amount <= 0) {
                    ctx.status(400).json(Map.of("message", "Invalid deposit amount."));
                    return;
                }

                AccountBase account = EndpointService.getExistingAccount(ctx);
                if (account == null) return;

                account.deposit(amount);

                ctx.status(200).json(Map.of(
                        "message", "Deposit successful.", "balance", account.getAccountBalance()
                ));

            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).json(Map.of("message", ERROR_MESSAGE));
            }
        });

// ---- When withdrawing
        app.post("/withdraw", ctx -> {
            try {
                System.out.println("A /withdraw get request received");
                System.out.println("/transfers: Session ID: " + ctx.req().getSession().getId());

                EndpointService.TransactionRequest transactionRequest = ctx.bodyAsClass(EndpointService.TransactionRequest.class);
                Float amount = transactionRequest.getAmount();
                if (amount <= 0) {
                    ctx.status(400).json(Map.of("message", "Invalid withdrawal amount."));
                    return;
                }

                AccountBase account = EndpointService.getExistingAccount(ctx);
                if (account == null) return;


                if (account.getAccountBalance() < amount) {
                    ctx.status(400).json(Map.of("message", "Insufficient funds."));
                    return;
                }
                account.withdraw(amount);
                ctx.status(200).json(Map.of(
                        "message", "Withdrawal successful.", "balance", account.getAccountBalance()
                ));

            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).json(Map.of("message", ERROR_MESSAGE));
            }
        });

// ---- When transfer is accessed
        app.post("/transfer", ctx -> {
            try {
                System.out.println("A /transfer get request received");
                System.out.println("/transfer: Session ID: " + ctx.req().getSession().getId());
                AccountDAO accountDAO = new AccountDAO();
                EndpointService.TransferRequest transferRequest = ctx.bodyAsClass(EndpointService.TransferRequest.class);

                Float transferAmount = transferRequest.getTransferAmount();

                AccountBase sendingAccount = EndpointService.getExistingAccount(ctx);
                if (sendingAccount == null) return;

                AccountBase receivingAccount = accountDAO.getAccountByAccountNumber(transferRequest.getAccountNumber());
                if (receivingAccount == null) return;

                TransferService.transferBetweenAccounts(sendingAccount, receivingAccount, transferAmount);

                // Fetch transactions from database
                List<Map<String, Object>> transformedTransactions = EndpointService.getAccountTransactions(sendingAccount);
                if (transformedTransactions == null) return;

                // Remove all transactions that do not have a transaction type of "TRANSFER_IN" or "TRANSFER_OUT"
                List<Map<String, Object>> transfers = transformedTransactions.stream()
                        .filter(transfer -> "TRANSFER_IN".equals(transfer.get("name"))
                                && "TRANSFER_OUT".equals(transfer.get("name"))
                                && ((Number) transfer.get("value")).floatValue() > 0
                        ).toList();

                // Respond with the new balance in JSON format
                ctx.json(Map.of(
                        "message", "Transfer successful.","balance", accountDAO.getAccountBalance(sendingAccount)
                ));
            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).json(Map.of("message", ERROR_MESSAGE));
            }
        });

// ---- When /profile is accessed
        app.get("/profile", ctx -> {
            try {
                System.out.println("A /profile get request received");
                System.out.println("/profile: Session ID: " + ctx.req().getSession().getId());

                // Get account
                AccountBase account = EndpointService.getExistingAccount(ctx);
                if (account == null) return;

                // Create a Map to hold the account details
                Map<String, Object> accountDetails = EndpointService.buildAccountDetailsMap(account);

                // Respond with the account details in JSON format
                ctx.json(accountDetails);
            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).json(Map.of("message", ERROR_MESSAGE));
            }
        });

// ---- When transactions are accessed
        app.get("/transactions", ctx -> {
            try {
                System.out.println("A /transactions get request received");
                System.out.println("/transactions: Session ID: " + ctx.req().getSession().getId());

                AccountBase account = EndpointService.getExistingAccount(ctx);
                if (account == null) return;

                // Fetch transactions from database
                List<Map<String, Object>> transactions = EndpointService.getAccountTransactions(account);
                if (transactions == null) return;

                // Respond with the transactions in JSON format
                ctx.json(Map.of("transactions", transactions, "currentBalance", account.getAccountBalance()));
            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).json(Map.of("message", ERROR_MESSAGE));
            }
        });

// ---- When dashboard is accessed
        app.get("/dashboard", ctx -> {
            try {
                System.out.println("A /dashboard get request received");
                System.out.println("/dashboard: Session ID: " + ctx.req().getSession().getId());

                AccountBase account = EndpointService.getExistingAccount(ctx);
                if (account == null) return;

                // Fetch transactions from database
                List<Map<String, Object>> transformedTransactions = EndpointService.getAccountTransactions(account);
                if (transformedTransactions == null) return;

                // Remove all transactions that do not have a transaction type of "DEPOSIT" or "WITHDRAWAL"
                List<Map<String, Object>> depositWithdrawalList = transformedTransactions.stream()
                        .filter(depositWithdrawal ->
                                !"TRANSFER_IN".equals(depositWithdrawal.get("name")) ||
                                        !"TRANSFER_OUT".equals(depositWithdrawal.get("name")) &&
                                        ((Number) depositWithdrawal.get("value")).floatValue() > 0).toList();

                ctx.json(Map.of("depositWithdrawalList", depositWithdrawalList, "currentBalance", account.getAccountBalance()));
            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).json(Map.of("message", ERROR_MESSAGE));
            }
        });

// ---- When transferList is accessed
        app.get("/transferList", ctx -> {
            try {
                System.out.println("A /transfers get request received");
                System.out.println("/transfers: Session ID: " + ctx.req().getSession().getId());

                AccountBase account = EndpointService.getExistingAccount(ctx);
                if (account == null) return;

                // Fetch transactions from database
                List<Map<String, Object>> transformedTransactions = EndpointService.getAccountTransactions(account);
                if (transformedTransactions == null) return;

                // Remove all transactions that do not have a transaction type of "TRANSFER_IN" or "TRANSFER_OUT"
                List<Map<String, Object>> transfers = transformedTransactions.stream()
                        .filter(transfer -> "TRANSFER_IN".equals(transfer.get("name"))
                                || "TRANSFER_OUT".equals(transfer.get("name"))
                                && ((Number) transfer.get("value")).floatValue() > 0
                        ).toList();

                // Respond with the transactions in JSON format
                ctx.json(Map.of("transfers", transfers, "currentBalance", account.getAccountBalance()));
            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).json(Map.of("message", ERROR_MESSAGE));
            }
        });

    }
}
