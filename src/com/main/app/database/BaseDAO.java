package com.main.app.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseDAO {
    public int getIdFromDatabase(PreparedStatement preparedStatement, int affectedRows) throws SQLException {
        if (affectedRows == 0) {
            throw new SQLException("Creating account failed, no rows affected.");
        }
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating account failed, no ID obtained.");
            }
        }
    }
}
