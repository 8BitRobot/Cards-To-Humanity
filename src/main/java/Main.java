import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.eclipse.jetty.server.session.*;
import storage.DatabaseStorage;

import java.net.URI;
import java.net.URISyntaxException;

public class Main {

    // Two functions for persisting sessions to the MariaDB database so that they can be shared.
    // See https://javalin.io/tutorials/jetty-session-handling.
    private static SessionHandler sqlSessionHandler(String driver, String url) {
        final SessionHandler sessionHandler = new SessionHandler();
        sessionHandler.getSessionCookieConfig().setHttpOnly(true);
        sessionHandler.getSessionCookieConfig().setSecure(true);
        sessionHandler.getSessionCookieConfig().setComment("__SAME_SITE_STRICT__");
        SessionCache sessionCache = new DefaultSessionCache(sessionHandler);
        sessionCache.setSessionDataStore(
            jdbcDataStoreFactory(driver, url).getSessionDataStore(sessionHandler)
        );
        sessionHandler.setSessionCache(sessionCache);
        sessionHandler.setHttpOnly(true);
        return sessionHandler;
    }

    private static JDBCSessionDataStoreFactory jdbcDataStoreFactory(String driver, String url) {
        DatabaseAdaptor databaseAdaptor = new DatabaseAdaptor();
        databaseAdaptor.setDriverInfo(driver, url);
        JDBCSessionDataStoreFactory jdbcSessionDataStoreFactory = new JDBCSessionDataStoreFactory();
        jdbcSessionDataStoreFactory.setDatabaseAdaptor(databaseAdaptor);
        return jdbcSessionDataStoreFactory;
    }

    public static void main(String[] args) {
        Integer port = Integer.parseInt(System.getProperty("server.port", "4567")); // Passed on the java command line by Heroku in the Procfile. Defaults to 4567 if not passed.
        String connectionURL = System.getenv("JAWSDB_MARIA_URL");
        if (connectionURL == null || connectionURL.equals("")) {
            connectionURL = "jdbc:mariadb://localhost:3306/carecards?user=root&password=none";
        }
        else {
            URI jdbUri = null;
            try {
                jdbUri = new URI(connectionURL);
            }
            catch (URISyntaxException exception) {
                exception.printStackTrace();
                System.out.println("Could not interpret JawsDB Maria URI.");
                System.exit(-1);
            }

            String username = jdbUri.getUserInfo().split(":")[0];
            String password = jdbUri.getUserInfo().split(":")[1];
            String db_port = String.valueOf(jdbUri.getPort());
            connectionURL = "jdbc:mariadb://" + jdbUri.getHost() + ":" + db_port + jdbUri.getPath() + "?user=" + username + "&password=" + password;
        }
        final String connectionURLFinal = connectionURL;

        Javalin app = Javalin.create(config -> {
            //config.addStaticFiles("/static");
            config.addStaticFiles("src/main/resources/static", Location.EXTERNAL); // TODO: Switch this line back to the previous commented line when not using live-reload for dev work.
            config.sessionHandler(() -> sqlSessionHandler("org.mariadb.jdbc.Driver", connectionURLFinal));
            config.dynamicGzip = false; // This prevents a 500 error when crawled by Facebook due to issues with how Javalin and Jetty cooperate when compressing data.
        }).start(port);

        /*
        app.before(ctx -> {
            System.out.println("~~~~~~~~~~~~~~~~~~~~~");
            Enumeration<String> headerNames = ctx.req.getHeaderNames();

            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                System.out.println(headerName + ": " + ctx.req.getHeader(headerName));
            }

            InputStream inputStream = ctx.req.getInputStream();
            byte[] raw_request_body_bytes = inputStream.readAllBytes();
            String raw_request_body = new String(raw_request_body_bytes, StandardCharsets.UTF_8);
            System.out.println(raw_request_body);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~");
        });
        */

        storage.DatabaseStorage databaseStorage = new DatabaseStorage();

        Gson gson = new Gson();

        endpoints.Home home_endpoint = new endpoints.Home();
        endpoints.UserExists user_exists_endpoint = new endpoints.UserExists(gson, databaseStorage);
        endpoints.LoginUser login_user_endpoint = new endpoints.LoginUser(databaseStorage);
        endpoints.CreateUser create_user_endpoint = new endpoints.CreateUser(databaseStorage);
        endpoints.LogoutUser logout_user_endpoint = new endpoints.LogoutUser();
        endpoints.UploadMedia upload_media_endpoint = new endpoints.UploadMedia(databaseStorage);
        endpoints.GetMedia get_media_endpoint = new endpoints.GetMedia(databaseStorage);
        endpoints.CreateCard create_card_endpoint = new endpoints.CreateCard(databaseStorage);
        endpoints.GetCard get_card_endpoint = new endpoints.GetCard(gson, databaseStorage);
        endpoints.GetCards get_cards_endpoint = new endpoints.GetCards(gson, databaseStorage);
        endpoints.CreateOrFindTag create_or_find_tag_endpoint = new endpoints.CreateOrFindTag(databaseStorage);
        app.get("/", home_endpoint);
        app.get("/user_exists", user_exists_endpoint);
        app.post("/login_user", login_user_endpoint);
        app.post("/create_user", create_user_endpoint);
        app.post("/logout_user", logout_user_endpoint);
        app.post("/upload_media", upload_media_endpoint);
        app.get("/get_media", get_media_endpoint);
        app.post("/create_card", create_card_endpoint);
        app.get("/get_card", get_card_endpoint);
        app.get("/get_cards", get_cards_endpoint);
        app.post("/create_or_find_tag", create_or_find_tag_endpoint);
    }
}
