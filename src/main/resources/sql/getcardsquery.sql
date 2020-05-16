-- Create a temporary table that stores the card IDs of cards that match the search parameters.
WITH found_cards_ids AS (SELECT cards.card_id FROM cards
                            INNER JOIN taggings
                                ON cards.card_id = taggings.card_id
                            INNER JOIN tags
                                ON taggings.tag_id = tags.tag_id
                            WHERE
                                tags.content = ?                     -- Find cards tagged with a specific tag.
                                OR
                                cards.title LIKE ?                   -- Find cards with titles containing a specific string. Set NULL to not use this part of the query.
                                OR
                                cards.caption LIKE ?                 -- Find cards with captions containing a specific string. Set NULL to not use this part of the query.
                            GROUP BY cards.card_id)                  -- Deduplicate.
SELECT
    cards.card_id AS card_id,
    cards.user_id AS user_id,
    media_id,
    title,
    caption,
    (
        SELECT COUNT(*) FROM likes
        WHERE
            likes.card_id = cards.card_id
    )
    AS likes,
    (
        SELECT GROUP_CONCAT(tags.content SEPARATOR ',')
        FROM tags
            INNER JOIN taggings
                ON tags.tag_id = taggings.tag_id
        WHERE taggings.card_id = cards.card_id
    )
    AS tags
FROM cards
    INNER JOIN found_cards_ids
        ON cards.card_id = found_cards_ids.card_id
    ORDER BY likes DESC
    LIMIT ?;