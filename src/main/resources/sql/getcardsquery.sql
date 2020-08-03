-- Create a temporary table that stores the card IDs of cards that match the search parameters.
WITH found_cards_ids AS (SELECT cards.card_id FROM cards
                            LEFT JOIN taggings
                                ON cards.card_id = taggings.card_id
                            LEFT JOIN tags
                                ON taggings.tag_id = tags.tag_id
                            WHERE
                                COALESCE(tags.content, '') LIKE ?                  -- Find cards tagged with a specific tag. Parameter #1.
                                AND
                                COALESCE(cards.title, '') LIKE ?                   -- Find cards with titles containing a specific string. Set NULL to not use this part of the query. Parameter #2.
                                AND
                                COALESCE(cards.caption, '') LIKE ?                 -- Find cards with captions containing a specific string. Set NULL to not use this part of the query. Parameter #3
                            GROUP BY cards.card_id)                  -- Deduplicate.
SELECT
    cards.card_id AS card_id,
    cards.user_id AS user_id,
    media.media_url AS media_url,
    cards.creation_time AS creation_time,
    title,
    caption,
    (
        SELECT COUNT(*) FROM likes
        WHERE
            likes.card_id = cards.card_id
    )
    AS likes,
    COALESCE(
        (
            SELECT GROUP_CONCAT(tags.content SEPARATOR ',')
            FROM tags
                     INNER JOIN taggings
                                ON tags.tag_id = taggings.tag_id
            WHERE taggings.card_id = cards.card_id
        )
    , '')
    AS tags
FROM cards
    INNER JOIN found_cards_ids
        ON cards.card_id = found_cards_ids.card_id
    INNER JOIN media
        ON cards.media_id = media.media_id
    ORDER BY creation_time DESC
    LIMIT ? -- Specifies how many of the most-liked cards to retrieve. Parameter #4.