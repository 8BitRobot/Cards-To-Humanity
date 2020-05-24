SELECT user_id,
       media_id,
       title,
       caption,
       (
           SELECT COUNT(*) FROM likes
               WHERE
                   card_id = ?
       )
       AS likes,
       (
           SELECT GROUP_CONCAT(tags.content SEPARATOR ',') FROM tags
               INNER JOIN taggings
                   ON tags.tag_id = taggings.tag_id
               WHERE taggings.card_id = ?
       )
       AS tags
FROM cards
    WHERE card_id = ?
