package storage;

public class Queries {
    public final static String userExists = "SELECT user_id FROM users WHERE username = ? OR email = ?";

    public final static String createUser = "INSERT INTO users (username, display_name, password_hash, email) VALUES (?, ?, ?, ?)";
}
