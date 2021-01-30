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
    private void cleanup(Connection connection, PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    public int userExists(String username_or_email) {
        Connection connection = null;
        PreparedStatement userExistsStatement = null;

        try {
            connection = DatabaseConnectionPool.getConnection();
            userExistsStatement = connection.prepareStatement(Queries.userExists);

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
        finally {
            cleanup(connection, userExistsStatement);
        }
        return -1;
    }

    public UserInfo getUserInfo(int user_id) {
        Connection connection = null;
        PreparedStatement getUserInfoStatement = null;

        try {
            connection = DatabaseConnectionPool.getConnection();
            getUserInfoStatement = connection.prepareStatement(Queries.getUserInfo);

            getUserInfoStatement.setInt(1, user_id);
            ResultSet results = getUserInfoStatement.executeQuery();
            if (results.first()) {
                String username = results.getString("username");
                String display_name = results.getString("display_name");
                String email = results.getString("email");
                long creation_time = results.getLong("creation_time");
                return new UserInfo(username, display_name, email, creation_time);
            }
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
        finally {
            cleanup(connection, getUserInfoStatement);
        }
        return null;
    }

    public int createUser(String username, String display_name, byte[] password_hash, byte[] password_salt, String email, long creation_time) {
        Connection connection = null;
        PreparedStatement createUserStatement = null;

        try {
            connection = DatabaseConnectionPool.getConnection();
            createUserStatement = connection.prepareStatement(Queries.createUser, Statement.RETURN_GENERATED_KEYS);

            createUserStatement.setString(1, username);
            createUserStatement.setString(2, display_name);
            createUserStatement.setBytes(3, password_hash);
            createUserStatement.setBytes(4, password_salt);
            createUserStatement.setString(5, email);
            createUserStatement.setLong(6, creation_time);
            createUserStatement.executeUpdate();
            ResultSet generated_keys = createUserStatement.getGeneratedKeys();
            if (generated_keys.first()) {
                return generated_keys.getInt("user_id");
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            cleanup(connection, createUserStatement);
        }
        return -1;
    }

    public HashedPassword getHashedPassword(int user_id) {
        Connection connection = null;
        PreparedStatement getHashedPasswordStatement = null;

        try {
            connection = DatabaseConnectionPool.getConnection();
            getHashedPasswordStatement = connection.prepareStatement(Queries.getHashedPassword);

            getHashedPasswordStatement.setInt(1, user_id);
            ResultSet results = getHashedPasswordStatement.executeQuery();
            if (results.first()) {
                return new HashedPassword(results.getBytes("password_salt"), results.getBytes("password_hash"));
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            cleanup(connection, getHashedPasswordStatement);
        }
        return null;
    }

    public int validateUser(String username_or_email, byte[] password_hash) {
        Connection connection = null;
        PreparedStatement validateUserStatement = null;

        try {
            connection = DatabaseConnectionPool.getConnection();
            validateUserStatement = connection.prepareStatement(Queries.validateUser);

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
        finally {
            cleanup(connection, validateUserStatement);
        }
        return -1;
    }

    public int createMedia(String image_url, long creation_time) {
        Connection connection = null;
        PreparedStatement createMediaStatement = null;

        try {
            connection = DatabaseConnectionPool.getConnection();
            createMediaStatement = connection.prepareStatement(Queries.createMedia, Statement.RETURN_GENERATED_KEYS);

            createMediaStatement.setString(1, image_url);
            createMediaStatement.setLong(2, creation_time);
            createMediaStatement.executeUpdate();
            ResultSet results = createMediaStatement.getGeneratedKeys();
            if (results.first()) {
                return results.getInt("media_id");
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            cleanup(connection, createMediaStatement);
        }
        return -1;
    }

    public int createCard(int user_id, int media_id, String title, String caption, long creation_time) {
        Connection connection = null;
        PreparedStatement createCardStatement = null;

        try {
            connection = DatabaseConnectionPool.getConnection();
            createCardStatement = connection.prepareStatement(Queries.createCard, Statement.RETURN_GENERATED_KEYS);

            createCardStatement.setInt(1, user_id);
            createCardStatement.setInt(2, media_id);
            createCardStatement.setString(3, title);
            createCardStatement.setString(4, caption);
            createCardStatement.setLong(5, creation_time);
            createCardStatement.executeUpdate();
            ResultSet results = createCardStatement.getGeneratedKeys();
            if (results.first()) {
                return results.getInt("card_id");
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            cleanup(connection, createCardStatement);
        }
        return -1;
    }

    public Card getCard(int card_id) {
        Connection connection = null;
        PreparedStatement getCardStatement = null;

        try {
            connection = DatabaseConnectionPool.getConnection();
            getCardStatement = connection.prepareStatement(Queries.getCard);

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
                long creation_time = results.getLong("creation_time");
                String[] tags;
                if (results.getString("tags").equals("")) {
                    tags = new String[0];
                }
                else {
                    tags = results.getString("tags").split(",");
                }
                return new Card(card_id, user_id, media_url, title, caption, likes, tags, creation_time);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            cleanup(connection, getCardStatement);
        }
        return null;
    }

    public Card[] getCards(String tagged_with, Integer top, String title_contains, String caption_contains) {
        Connection connection = null;
        PreparedStatement getCardsStatement = null;

        try {
            connection = DatabaseConnectionPool.getConnection();
            getCardsStatement = connection.prepareStatement(Queries.getCards);

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
                long creation_time = results.getLong("creation_time");
                String[] tags;
                if (results.getString("tags").equals("")) {
                    tags = new String[0];
                }
                else {
                    tags = results.getString("tags").split(",");
                }
                cards.add(new Card(card_id, user_id, media_url, title, caption, likes, tags, creation_time));
            }
            Card[] cardsArray = new Card[cards.size()];
            cards.toArray(cardsArray);
            return cardsArray;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            cleanup(connection, getCardsStatement);
        }
        return new Card[0];
    }

    private int getTagId(String content) {
        Connection connection = null;
        PreparedStatement getTagIdStatement = null;

        try {
            connection = DatabaseConnectionPool.getConnection();
            getTagIdStatement = connection.prepareStatement(Queries.getTagId);

            getTagIdStatement.setString(1, content);
            ResultSet results = getTagIdStatement.executeQuery();
            if (results.first()) {
                return results.getInt("tag_id");
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            cleanup(connection, getTagIdStatement);
        }
        return -1;
    }

    public int createTagOrFindExisting(String content, long creation_time) {
        Connection connection = null;
        PreparedStatement createTagStatement = null;

        try {
            connection = DatabaseConnectionPool.getConnection();
            createTagStatement = connection.prepareStatement(Queries.createTag, Statement.RETURN_GENERATED_KEYS);

            createTagStatement.setString(1, content);
            createTagStatement.setLong(2, creation_time);
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
        finally {
            cleanup(connection, createTagStatement);
        }
        return -1;
    }

    public int tagCard(int card_id, int tag_id) {
        Connection connection = null;
        PreparedStatement tagCardStatement = null;

        try {
            connection = DatabaseConnectionPool.getConnection();
            tagCardStatement = connection.prepareStatement(Queries.tagCard, Statement.RETURN_GENERATED_KEYS);

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
        finally {
            cleanup(connection, tagCardStatement);
        }
        return -1;
    }

    public int likeCard(int card_id, int user_id, long creation_time) {
        Connection connection = null;
        PreparedStatement likeCardStatement = null;

        try {
            connection = DatabaseConnectionPool.getConnection();
            likeCardStatement = connection.prepareStatement(Queries.likeCard, Statement.RETURN_GENERATED_KEYS);

            likeCardStatement.setInt(1, card_id);
            likeCardStatement.setInt(2, user_id);
            likeCardStatement.setLong(3, creation_time);
            likeCardStatement.executeUpdate();
            ResultSet results = likeCardStatement.getGeneratedKeys();
            if (results.first()) {
                return results.getInt("like_id");
            }
        }
        catch (java.sql.SQLIntegrityConstraintViolationException exception) {
            // Ignore duplicate likes.
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            cleanup(connection, likeCardStatement);
        }
        return -1;
    }

    public Tag[] getTags(String content_contains, Integer top) {
        Connection connection = null;
        PreparedStatement getTagsStatement = null;
        try {
            connection = DatabaseConnectionPool.getConnection();
            getTagsStatement = connection.prepareStatement(Queries.getTags);

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
                long creation_time = results.getLong("creation_time");
                tags.add(new Tag(tag_id, content, cards_tagged, creation_time));
            }
            Tag[] tags_array = new Tag[tags.size()];
            tags.toArray(tags_array);
            return tags_array;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            cleanup(connection, getTagsStatement);
        }
        return new Tag[0];
    }

    public boolean getCardLiked(int user_id, int card_id) {
        Connection connection = null;
        PreparedStatement getCardLikedStatement = null;
        try {
            connection = DatabaseConnectionPool.getConnection();
            getCardLikedStatement = connection.prepareStatement(Queries.getCardLiked);

            getCardLikedStatement.setInt(1, user_id);
            getCardLikedStatement.setInt(2, card_id);

            ResultSet results = getCardLikedStatement.executeQuery();

            return results.first();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            cleanup(connection, getCardLikedStatement);
        }

        return false;
    }

    public int signUpEmail(String email_address, String first_name, String last_name, long signup_time) {
        Connection connection = null;
        PreparedStatement signUpEmailStatement = null;

        try {
            connection = DatabaseConnectionPool.getConnection();
            signUpEmailStatement = connection.prepareStatement(Queries.signUpEmail, Statement.RETURN_GENERATED_KEYS);

            signUpEmailStatement.setString(1, email_address);
            signUpEmailStatement.setString(2, first_name);
            signUpEmailStatement.setString(3, last_name);
            signUpEmailStatement.setLong(4, signup_time);
            signUpEmailStatement.executeUpdate();
            ResultSet results = signUpEmailStatement.getGeneratedKeys();
            if (results.first()) {
                return results.getInt("email_signup_id");
            }
        }
        catch (java.sql.SQLIntegrityConstraintViolationException exception) {
            // Ignore duplicate emails.
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            cleanup(connection, signUpEmailStatement);
        }
        return -1;
    }
}