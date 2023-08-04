package com.main.app.database;

import java.sql.*;

public class DatabaseConnection {
    Connection conn = null;
    Statement statement = null;
    ResultSet resultsSet = null;
    private static DatabaseConnection instance;

    private DatabaseConnection() {

    }
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
            return instance;
    }

    // Currently vulnerable to SQL injection due to using concatenated strings to for SQL commands
    public int handleUpdate(PreparedStatement preparedStatement) throws SQLException {
        try {
            int rowCount = preparedStatement.executeUpdate();
            System.out.printf("Success - %d - rows affected.\n",rowCount);
            return rowCount;
        } catch(SQLException err) {
            System.out.println("An error has occurred.");
            System.out.println("See full details below.");
            err.printStackTrace();
            throw err;  // Rethrow the exception so it can be handled by the calling method
        }
    }
    public ResultSet handleQuery(PreparedStatement preparedStatement) {
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
        // Releases resources in reverse-order of
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
    }
    private void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException sqlEx) {
            }
            conn = null;
        }
    }
}
