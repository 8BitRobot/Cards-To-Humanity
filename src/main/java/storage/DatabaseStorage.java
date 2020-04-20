package storage;

/** @file DatabaseStorage.java
 * Code that connects the rest of the application to the database. Persistent storage layer of the server-side code.
 */

import java.sql.*;
import com.google.gson.Gson;

/**
 * JDBC-based class that connects the application to the database layer. Uses JDBC to talk to MariaDB/MySQL.
 */
public class DatabaseStorage {
    /**
     * A reusable, shared Google Gson object for processing JSON.
     */
    private Gson gson;

    /**
     * The database connection.
     */
    private Connection connection;

    /**
     * Parameterized SQL query to check if a user with a given username/email exists.
     */
    private PreparedStatement userExistsStatement;

    /**
     * Parameterized SQL query to create a new user.
     */
    private PreparedStatement createUserStatement;

    /**
     * Create a new DatabaseStorage object and connect to the DB.
     */
    public DatabaseStorage() {
        try {
            gson = new Gson();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carecards", "root", "none");
            userExistsStatement = connection.prepareStatement(Queries.userExists);
            createUserStatement = connection.prepareStatement(Queries.createUser, Statement.RETURN_GENERATED_KEYS);
        }
        catch (SQLException exception) {
            exception.printStackTrace();
            connection = null;
        }
    }

    public synchronized boolean userExists(String username_or_email) {
        try {
            userExistsStatement.setString(1, username_or_email);
            ResultSet results = userExistsStatement.executeQuery();
            return results.first();
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public synchronized int createUser(String username, String display_name, String password_hash, String email) {
        try {
            createUserStatement.setString(1, username);
            createUserStatement.setString(2, display_name);
            createUserStatement.setString(3, password_hash);
            createUserStatement.setString(4, email);
            createUserStatement.executeUpdate();
            ResultSet generated_keys = createUserStatement.getGeneratedKeys();
            if (generated_keys.first()) {
                return generated_keys.getInt(1);
            }
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
        return -1;
    }
}
