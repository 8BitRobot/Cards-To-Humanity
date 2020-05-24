SELECT password_hash, password_salt FROM users
    WHERE
        user_id = ?
