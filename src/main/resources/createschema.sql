-- This SQL file is in the MariaDB dialect. It creates the schema for the "carecards" database, which is the persistent layer of the carecards application.
-- Concepts:
--    Users -- A user is one person who uses the site. One special user with the username "anonymous" is used for people who don't register for an account.
--    Cards -- A card is one virtual care card for a front-line worker. Users create cards with a title, caption, and some kind of image (usually of a hand-written card). The images are stored as media in the media table.
--    Media -- Each card has one piece of media associated with it. Media can be any image file of a supported type.
--    Tags -- Tags are short phrases used to group cards. For example, you could tag your card "firefighters" or "nurses" to target those groups. Cards can have multiple tags attached to them. Anyone can create a new tag.a
--    Taggings -- Pairings between a card and a tag. Stored in a separate table.
--    Likes -- When a user clicks the "like" button on a card, a like is created in the likes table. Likes persist even if the originating user is later deleted.

-- Destroy the old database (leave commented out for safety).
DROP DATABASE carecards;

-- Create a new, empty database with Unicode encoding (this allows emojis and non-English characters to be stored directly).
CREATE DATABASE carecards
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Connect to the newly-created database.
USE carecards;

-- Create a table to store all users of the system.
CREATE TABLE users(user_id INT AUTO_INCREMENT PRIMARY KEY,     -- Identifying key for the user.
                   username VARCHAR(100) NOT NULL,             -- Username for the user (used to log in). Like "xxdogman69xx".
                   display_name VARCHAR(255) NOT NULL,         -- Name that is displayed on the user's posts. Like "Karl Marx".
                   password_hash VARCHAR(100) NOT NULL,        -- Hashed password for the user. Used for authentication.
                   email VARCHAR(100) NOT NULL,                -- The user's email address.
                   UNIQUE INDEX unique_index(username, email)  -- Usernames and emails cannot be shared (since they are used to log in).
);

-- Create the special anonymous user that is associated with all cards/tags/likes from non-registered users.
INSERT IGNORE INTO users (username, display_name, password_hash, email) VALUES ("anonymous", "Anonymous", "", "");

-- Create a table to store all uploaded media. Might move this to Amazon S3 later on.
CREATE TABLE media(media_id INT AUTO_INCREMENT PRIMARY KEY,
                   media_type INT,
                   media_content LONGBLOB
);

-- Create a table to store all cards in the system.
CREATE TABLE cards(card_id INT AUTO_INCREMENT PRIMARY KEY,     -- Identifying key for the card.
                   user_id INT,                                -- The id of the user who created this card.
                       CONSTRAINT foreign_key_user_id          -- Reference to the user who created this card.
                       FOREIGN KEY (user_id)
                           REFERENCES users(user_id)
                           ON DELETE SET NULL,                 -- If the user who created this card is deleted, then set the user_id column here to NULL.
                   media_id INT,                               -- The id of the media (photo, video, etc.) attached to this card.
                       CONSTRAINT foreign_key_media_id         -- Reference to the media attached to this card.
                       FOREIGN KEY (media_id)
                           REFERENCES media(media_id)
                           ON DELETE SET NULL,                 -- If the media attached to this card is deleted, then set the media_id column here to NULL.
                   title TEXT,                                 -- Title of the card.
                   caption TEXT                                -- Written caption/description for the card.
);

-- Create a table to store all tags for cards (tags are short phrases that are used to group cards, such as "wholesome" or "firefighters").
CREATE TABLE tags(tag_id INT AUTO_INCREMENT PRIMARY KEY,
                  content VARCHAR(50)
);

-- Create a table that associates tags to cards. A card may have multiple tags.
CREATE TABLE taggings(tagging_id INT AUTO_INCREMENT PRIMARY KEY,
                      card_id INT,
                          CONSTRAINT foreign_key_card_id
                          FOREIGN KEY (card_id)
                              REFERENCES cards(card_id)
                              ON DELETE CASCADE,
                      tag_id INT,
                          CONSTRAINT foreign_key_tag_id
                          FOREIGN KEY (tag_id)
                              REFERENCES tags(tag_id)
                              ON DELETE CASCADE
);

-- Create a table that stores who "liked" a card.
CREATE TABLE likes(like_id INT AUTO_INCREMENT PRIMARY KEY,
                   card_id INT,
                       CONSTRAINT foreign_key_card_id
                       FOREIGN KEY (card_id)
                           REFERENCES cards(card_id)
                           ON DELETE CASCADE,
                   user_id INT,
                       CONSTRAINT foreign_key_user_id
                       FOREIGN KEY (user_id)
                           REFERENCES users(user_id)
                           ON DELETE SET NULL
);
