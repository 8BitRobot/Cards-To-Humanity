package storage;

public class Queries {
    public final static String userExists = "SELECT user_id FROM users WHERE username = ? OR email = ?";

    public final static String createUser = "INSERT INTO users (username, display_name, password_hash, password_salt, email) VALUES (?, ?, ?, ?, ?)";

    public final static String validateUser = "SELECT * FROM users WHERE (username = ? OR email = ?) AND password_hash = ?";

    public final static String createMedia = "INSERT INTO media (media_mime_type, media_content) VALUES (?, ?)";

    public final static String getMedia = "SELECT media_mime_type, media_content FROM media WHERE media_id = ?";

    public final static String createCard = "INSERT INTO cards (user_id, media_id, title, caption) VALUES (?, ?, ?, ?)";

    public final static String getCard = "SELECT user_id, media_id, title, caption, likes = (SELECT COUNT(*) FROM likes WHERE card_id = ?), tags = (SELECT GROUP_CONCAT(tags.content SEPARATOR ',') FROM tags INNER JOIN taggings ON tags.tag_id = taggings.tag_id WHERE taggings.card_id = ?) FROM cards WHERE card_id = ?";
}
