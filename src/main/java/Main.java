import io.javalin.Javalin;
import org.eclipse.jetty.server.session.*;
import storage.DatabaseStorage;

public class Main {

    // Two functions for persisting sessions to the MariaDB database so that they can be shared.
    // See https://javalin.io/tutorials/jetty-session-handling.
    private static SessionHandler sqlSessionHandler(String driver, String url) {
        SessionHandler sessionHandler = new SessionHandler();
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

        final String connectionURLFinal = connectionURL;

        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/static");
            config.sessionHandler(() -> sqlSessionHandler("org.mariadb.jdbc.Driver", connectionURLFinal));
        }).start(port);

        storage.DatabaseStorage databaseStorage = new DatabaseStorage(connectionURLFinal);

        endpoints.Home home_endpoint = new endpoints.Home();
        endpoints.LoginUser login_user_endpoint = new endpoints.LoginUser(databaseStorage);
        endpoints.CreateUser create_user_endpoint = new endpoints.CreateUser(databaseStorage);
        app.get("/", home_endpoint);
        app.post("/login_user", login_user_endpoint);
        app.post("/create_user", create_user_endpoint);
    }
}
