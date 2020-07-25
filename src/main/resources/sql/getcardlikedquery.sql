SELECT like_id FROM likes
    WHERE
        user_id = ?
        AND
        card_id = ?