package storage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Queries {
    public final String userExists;

    public final String createUser;

    public final String getHashedPassword;

    public final String validateUser;

    public final String createMedia;

    public final String getMedia;

    public final String createCard;

    public final String getCard;

    public final String getCards;

    public final String createTag;

    public final String getTagId;

    public final String tagCard;

    public final String likeCard;

    private ClassLoader classLoader;

    private String readResourceFile(String fileName) {
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
    }

    public Queries() {
        classLoader = Thread.currentThread().getContextClassLoader();
        userExists = readResourceFile("sql/userexistsquery.sql");
        createUser = readResourceFile("sql/createuserquery.sql");
        getHashedPassword = readResourceFile("sql/gethashedpasswordquery.sql");
        validateUser = readResourceFile("sql/validateuserquery.sql");
        createMedia = readResourceFile("sql/createmediaquery.sql");
        getMedia = readResourceFile("sql/getmediaquery.sql");
        createCard = readResourceFile("sql/createcardquery.sql");
        getCard = readResourceFile("sql/getcardquery.sql");
        getCards = readResourceFile("sql/getcardsquery.sql");
        createTag = readResourceFile("sql/createtagquery.sql");
        getTagId = readResourceFile("sql/gettagidquery.sql");
        tagCard = readResourceFile("sql/tagcardquery.sql");
        likeCard = readResourceFile("sql/likecardquery.sql");
    }
}
