package bkpembawastress;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Db {
    private static final String URL = "jdbc:mysql://localhost:3306/db_bkpembawastress?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";

    private Db() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
