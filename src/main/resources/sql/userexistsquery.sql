SELECT user_id FROM users
    WHERE
        username = ?
        OR
        email = ?
