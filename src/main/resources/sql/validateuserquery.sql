SELECT * FROM users
    WHERE
        (username = ? OR email = ?)
        AND
        password_hash = ?
