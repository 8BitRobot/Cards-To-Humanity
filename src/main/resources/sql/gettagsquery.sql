-- Create a temporary table that stores the tag IDs of tags that match the search parameters.
WITH found_tags_ids AS (
    SELECT tag_id FROM tags
        WHERE
            content LIKE ? -- Filter tags by their content. Parameter #1
)
SELECT
    tags.tag_id AS tag_id,
    content,
    (
        SELECT COUNT(*) FROM taggings
        WHERE
            taggings.tag_id = tags.tag_id
    )
    AS cards_tagged
FROM tags
    INNER JOIN found_tags_ids
        ON tags.tag_id = found_tags_ids.tag_id
    ORDER BY cards_tagged DESC
    LIMIT ? -- How many tags to retrieve. Parameter #2