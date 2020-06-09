package storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

class DatabaseConnection {
    private Connection connection;

    private PreparedStatement userExistsStatement;

    private PreparedStatement createUserStatement;

    private PreparedStatement validateUserStatement;

    private PreparedStatement createMediaStatement;

    private PreparedStatement createCardStatement;

    private PreparedStatement getCardStatement;

    private PreparedStatement createTagStatement;

    private PreparedStatement getTagIdStatement;

    private PreparedStatement getHashedPasswordStatement;

    private PreparedStatement tagCardStatement;

    private PreparedStatement likeCardStatement;

    private PreparedStatement getCardsStatement;

    private PreparedStatement getTagsStatement;

    public DatabaseConnection(Connection connection) throws SQLException {
        this.connection = connection;
        Queries queries = new Queries();
        userExistsStatement = connection.prepareStatement(queries.userExists);
        createUserStatement = connection.prepareStatement(queries.createUser, Statement.RETURN_GENERATED_KEYS);
        validateUserStatement = connection.prepareStatement(queries.validateUser);
        createMediaStatement = connection.prepareStatement(queries.createMedia, Statement.RETURN_GENERATED_KEYS);
        createCardStatement = connection.prepareStatement(queries.createCard, Statement.RETURN_GENERATED_KEYS);
        getCardStatement = connection.prepareStatement(queries.getCard);
        createTagStatement = connection.prepareStatement(queries.createTag, Statement.RETURN_GENERATED_KEYS);
        getTagIdStatement = connection.prepareStatement(queries.getTagId);
        getHashedPasswordStatement = connection.prepareStatement(queries.getHashedPassword);
        tagCardStatement = connection.prepareStatement(queries.tagCard, Statement.RETURN_GENERATED_KEYS);
        likeCardStatement = connection.prepareStatement(queries.likeCard, Statement.RETURN_GENERATED_KEYS);
        getCardsStatement = connection.prepareStatement(queries.getCards);
        getTagsStatement = connection.prepareStatement(queries.getTags);
    }

    public PreparedStatement getUserExistsStatement() {
        return userExistsStatement;
    }

    public PreparedStatement getCreateUserStatement() {
        return createUserStatement;
    }

    public PreparedStatement getValidateUserStatement() {
        return validateUserStatement;
    }

    public PreparedStatement getCreateMediaStatement() {
        return createMediaStatement;
    }

    public PreparedStatement getCreateCardStatement() {
        return createCardStatement;
    }

    public PreparedStatement getGetCardStatement() {
        return getCardStatement;
    }

    public PreparedStatement getCreateTagStatement() {
        return createTagStatement;
    }

    public PreparedStatement getGetTagIdStatement() {
        return getTagIdStatement;
    }

    public PreparedStatement getGetHashedPasswordStatement() {
        return getHashedPasswordStatement;
    }

    public PreparedStatement getTagCardStatement() {
        return tagCardStatement;
    }

    public PreparedStatement getLikeCardStatement() {
        return likeCardStatement;
    }

    public PreparedStatement getGetCardsStatement() {
        return getCardsStatement;
    }

    public PreparedStatement getGetTagsStatement() {
        return getTagsStatement;
    }
}
