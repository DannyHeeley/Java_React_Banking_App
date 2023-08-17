package com.app.database;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.*;

public class DbConnPool {
    private static final HikariDataSource dataSource;
    static {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/bankdb");
        dataSource.setUsername("root");
        dataSource.setPassword("xxxx");
        dataSource.setMaximumPoolSize(10);
        dataSource.setLeakDetectionThreshold(10000);
    }
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    public int handleUpdate(PreparedStatement preparedStatement) throws SQLException {
        try {
            return preparedStatement.executeUpdate();
        } catch(SQLException err) {
            System.out.println("Error in DatabaseConnection: handleUpdate");
            System.out.println("See full details below.");
            err.printStackTrace();
            throw err;
        }
    }
}
