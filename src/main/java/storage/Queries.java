package storage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Queries {
    public static final String userExists;

    public static final String createUser;

    public static final String getHashedPassword;

    public static final String validateUser;

    public static final String createMedia;

    public static final String createCard;

    public static final String getCard;

    public static final String getCards;

    public static final String createTag;

    public static final String getTagId;

    public static final String tagCard;

    public static final String likeCard;

    public static final String getTags;

    public static final String getUserInfo;

    public static final String getCardLiked;

    private static ClassLoader classLoader;

    private static String readResourceFile(String fileName) {
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
    }

    static {
        classLoader = Thread.currentThread().getContextClassLoader();
        userExists = readResourceFile("sql/userexistsquery.sql");
        createUser = readResourceFile("sql/createuserquery.sql");
        getHashedPassword = readResourceFile("sql/gethashedpasswordquery.sql");
        validateUser = readResourceFile("sql/validateuserquery.sql");
        createMedia = readResourceFile("sql/createmediaquery.sql");
        createCard = readResourceFile("sql/createcardquery.sql");
        getCard = readResourceFile("sql/getcardquery.sql");
        getCards = readResourceFile("sql/getcardsquery.sql");
        createTag = readResourceFile("sql/createtagquery.sql");
        getTagId = readResourceFile("sql/gettagidquery.sql");
        tagCard = readResourceFile("sql/tagcardquery.sql");
        likeCard = readResourceFile("sql/likecardquery.sql");
        getTags = readResourceFile("sql/gettagsquery.sql");
        getUserInfo = readResourceFile("sql/getuserinfoquery.sql");
        getCardLiked = readResourceFile("sql/getcardlikedquery.sql");
    }
}
