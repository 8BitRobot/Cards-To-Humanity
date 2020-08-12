SELECT user_id,
       title,
       media.media_url AS media_url,
       caption,
       creation_time,
       (
           SELECT COUNT(*) FROM likes
               WHERE
                   card_id = ?
       )
       AS likes,
       COALESCE(
           (
                SELECT GROUP_CONCAT(tags.content SEPARATOR ',')
                FROM tags
                    INNER JOIN taggings
                        ON tags.tag_id = taggings.tag_id
                    WHERE
                        taggings.card_id = ?
           )
           , ''
       )
       AS tags
FROM cards
    INNER JOIN media
        ON cards.media_id = media.media_id
    WHERE
          card_id = ?
