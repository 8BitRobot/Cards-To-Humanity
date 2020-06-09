package storage;

/** @file DatabaseStorage.java
 * Code that connects the rest of the application to the database. Persistent storage layer of the server-side code.
 */

import security.HashedPassword;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC-based class that connects the application to the database layer. Uses JDBC to talk to MariaDB/MySQL.
 */
public class DatabaseStorage {
    public int userExists(String username_or_email) {
        try {
            DatabaseConnection connection = DatabaseConnectionPool.getDatabaseConnection();
            PreparedStatement userExistsStatement = connection.getUserExistsStatement();

            userExistsStatement.setString(1, username_or_email);
            userExistsStatement.setString(2, username_or_email);
            ResultSet results = userExistsStatement.executeQuery();
            if (results.first()) {
                return results.getInt("user_id");
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public int createUser(String username, String display_name, byte[] password_hash, byte[] password_salt, String email) {
        try {
            DatabaseConnection connection = DatabaseConnectionPool.getDatabaseConnection();
            PreparedStatement createUserStatement = connection.getCreateUserStatement();

            createUserStatement.setString(1, username);
            createUserStatement.setString(2, display_name);
            createUserStatement.setBytes(3, password_hash);
            createUserStatement.setBytes(4, password_salt);
            createUserStatement.setString(5, email);
            createUserStatement.executeUpdate();
            ResultSet generated_keys = createUserStatement.getGeneratedKeys();
            if (generated_keys.first()) {
                return generated_keys.getInt("user_id");
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public HashedPassword getHashedPassword(int user_id) {
        try {
            DatabaseConnection connection = DatabaseConnectionPool.getDatabaseConnection();
            PreparedStatement getHashedPasswordStatement = connection.getGetHashedPasswordStatement();

            getHashedPasswordStatement.setInt(1, user_id);
            ResultSet results = getHashedPasswordStatement.executeQuery();
            if (results.first()) {
                return new HashedPassword(results.getBytes("password_salt"), results.getBytes("password_hash"));
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public int validateUser(String username_or_email, byte[] password_hash) {
        try {
            DatabaseConnection connection = DatabaseConnectionPool.getDatabaseConnection();
            PreparedStatement validateUserStatement = connection.getValidateUserStatement();

            validateUserStatement.setString(1, username_or_email);
            validateUserStatement.setString(2, username_or_email);
            validateUserStatement.setBytes(3, password_hash);
            ResultSet results = validateUserStatement.executeQuery();
            if (results.first()) {
                return results.getInt("user_id");
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public int createMedia(String image_url) {
        try {
            DatabaseConnection connection = DatabaseConnectionPool.getDatabaseConnection();
            PreparedStatement createMediaStatement = connection.getCreateMediaStatement();

            createMediaStatement.setString(1, image_url);
            createMediaStatement.executeUpdate();
            ResultSet results = createMediaStatement.getGeneratedKeys();
            if (results.first()) {
                return results.getInt("media_id");
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public int createCard(int user_id, int media_id, String title, String caption) {
        try {
            DatabaseConnection connection = DatabaseConnectionPool.getDatabaseConnection();
            PreparedStatement createCardStatement = connection.getCreateCardStatement();

            createCardStatement.setInt(1, user_id);
            createCardStatement.setInt(2, media_id);
            createCardStatement.setString(3, title);
            createCardStatement.setString(4, caption);
            createCardStatement.executeUpdate();
            ResultSet results = createCardStatement.getGeneratedKeys();
            if (results.first()) {
                return results.getInt("card_id");
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public Card getCard(int card_id) {
        try {
            DatabaseConnection connection = DatabaseConnectionPool.getDatabaseConnection();
            PreparedStatement getCardStatement = connection.getGetCardStatement();

            getCardStatement.setInt(1, card_id);
            getCardStatement.setInt(2, card_id);
            getCardStatement.setInt(3, card_id);
            ResultSet results = getCardStatement.executeQuery();
            if (results.first()) {
                int user_id = results.getInt("user_id");
                String media_url = results.getString("media_url");
                String title = results.getString("title");
                String caption = results.getString("caption");
                int likes = results.getInt("likes");
                String[] tags;
                if (results.getString("tags").equals("")) {
                    tags = new String[0];
                }
                else {
                    tags = results.getString("tags").split(",");
                }
                return new Card(card_id, user_id, media_url, title, caption, likes, tags);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Card[] getCards(String tagged_with, Integer top, String title_contains, String caption_contains) {
        try {
            DatabaseConnection connection = DatabaseConnectionPool.getDatabaseConnection();
            PreparedStatement getCardsStatement = connection.getGetCardsStatement();

            if (tagged_with == null) {
                getCardsStatement.setString(1, "%");
            }
            else {
                getCardsStatement.setString(1, "%" + tagged_with + "%");
            }
            if (title_contains == null) {
                getCardsStatement.setString(2, "%");
            }
            else {
                getCardsStatement.setString(2, "%" + title_contains + "%");
            }
            if (caption_contains == null) {
                getCardsStatement.setString(3, "%");
            }
            else {
                getCardsStatement.setString(3, "%" + caption_contains + "%");
            }
            if (top == null || top > 100) {
                top = 100;
            }
            getCardsStatement.setInt(4, top);
            ResultSet results = getCardsStatement.executeQuery();
            List<Card> cards = new ArrayList<>();
            while (results.next()) {
                int card_id = results.getInt("card_id");
                int user_id = results.getInt("user_id");
                String media_url = results.getString("media_url");
                String title = results.getString("title");
                String caption = results.getString("caption");
                int likes = results.getInt("likes");
                String[] tags;
                if (results.getString("tags").equals("")) {
                    tags = new String[0];
                }
                else {
                    tags = results.getString("tags").split(",");
                }
                cards.add(new Card(card_id, user_id, media_url, title, caption, likes, tags));
            }
            Card[] cardsArray = new Card[cards.size()];
            cards.toArray(cardsArray);
            return cardsArray;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return new Card[0];
    }

    private int getTagId(String content) {
        try {
            DatabaseConnection connection = DatabaseConnectionPool.getDatabaseConnection();
            PreparedStatement getTagIdStatement = connection.getGetTagIdStatement();

            getTagIdStatement.setString(1, content);
            ResultSet results = getTagIdStatement.executeQuery();
            if (results.first()) {
                return results.getInt("tag_id");
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public int createTagOrFindExisting(String content) {
        try {
            DatabaseConnection connection = DatabaseConnectionPool.getDatabaseConnection();
            PreparedStatement createTagStatement = connection.getCreateTagStatement();

            createTagStatement.setString(1, content);
            createTagStatement.executeUpdate();
            ResultSet results = createTagStatement.getGeneratedKeys();
            if (results == null) {
                return getTagId(content);
            }
            if (results.first()) {
                return results.getInt("tag_id");
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public int tagCard(int card_id, int tag_id) {
        try {
            DatabaseConnection connection = DatabaseConnectionPool.getDatabaseConnection();
            PreparedStatement tagCardStatement = connection.getTagCardStatement();

            tagCardStatement.setInt(1, card_id);
            tagCardStatement.setInt(2, tag_id);
            tagCardStatement.executeUpdate();
            ResultSet results = tagCardStatement.getGeneratedKeys();
            if (results.first()) {
                return results.getInt("tagging_id");
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public int likeCard(int card_id, int user_id) {
        try {
            DatabaseConnection connection = DatabaseConnectionPool.getDatabaseConnection();
            PreparedStatement likeCardStatement = connection.getLikeCardStatement();

            likeCardStatement.setInt(1, card_id);
            likeCardStatement.setInt(2, user_id);
            likeCardStatement.executeUpdate();
            ResultSet results = likeCardStatement.getGeneratedKeys();
            if (results.first()) {
                return results.getInt("like_id");
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public Tag[] getTags(String content_contains, Integer top) {
        try {
            DatabaseConnection connection = DatabaseConnectionPool.getDatabaseConnection();
            PreparedStatement getTagsStatement = connection.getGetTagsStatement();

            getTagsStatement.setString(1, "%" + content_contains + "%");
            if (top == null || top > 100) {
                top = 100;
            }
            getTagsStatement.setInt(2, top);
            ResultSet results = getTagsStatement.executeQuery();
            List<Tag> tags = new ArrayList<>();
            while (results.next()) {
                int tag_id = results.getInt("tag_id");
                String content = results.getString("content");
                int cards_tagged = results.getInt("cards_tagged");
                tags.add(new Tag(tag_id, content, cards_tagged));
            }
            Tag[] tags_array = new Tag[tags.size()];
            tags.toArray(tags_array);
            return tags_array;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return new Tag[0];
    }
}
