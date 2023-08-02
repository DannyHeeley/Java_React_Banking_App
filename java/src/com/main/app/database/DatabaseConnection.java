package com.main.app.database;

import java.sql.*;

public class DatabaseConnection {
    Connection conn = null;
    Statement statement = null;
    ResultSet resultsSet = null;

    public void executeUpdate(PreparedStatement preparedStatement) {
        try {
            int rowcount = preparedStatement.executeUpdate();
            System.out.println();
            System.out.printf("Success - %d - rows affected.\n",rowcount);
        } catch(Exception err) {
            System.out.println("An error has occurred.");
            System.out.println("See full details below.");
            err.printStackTrace();
        }
    }

    public ResultSet executeQuery(PreparedStatement preparedStatement) {
        ResultSet resultSet = null;
        try {
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } finally {
            releaseResources();
        }
        return resultSet;
    }
    public Connection getDatabaseConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/bankdb";
            String user = "root";
            String password = "xxxx";
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return conn;
    }
    private void releaseResources() {
        // closes connections in reverse-order of
        // their creation if they are no-longer needed
        if (resultsSet != null) {
            try {
                resultsSet.close();
            } catch (SQLException sqlEx) {
            }
            resultsSet = null;
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException sqlEx) {
            }
            statement = null;
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException sqlEx) {
            }
            conn = null;
        }
    }
}
