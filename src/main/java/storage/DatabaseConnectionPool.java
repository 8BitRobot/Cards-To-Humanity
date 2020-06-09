package storage;

import org.apache.commons.dbcp2.BasicDataSource;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

class DatabaseConnectionPool {

    private static BasicDataSource dataSource = new BasicDataSource();

    static {
        // See https://devcenter.heroku.com/articles/jawsdb-maria#using-jawsdb-maria-with-java
        String JAWSDB_MARIA_URL = System.getenv("JAWSDB_MARIA_URL");

        if (JAWSDB_MARIA_URL == null || JAWSDB_MARIA_URL.equals("")) {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/carecards");
            dataSource.setValidationQuery("SELECT * FROM users WHERE user_id = 1");
            dataSource.setValidationQueryTimeout(3);
            dataSource.setTimeBetweenEvictionRunsMillis(5000);
            dataSource.setMaxConnLifetimeMillis(60 * 1000);
            dataSource.setUsername("root");
            dataSource.setPassword("none");
            dataSource.setMinIdle(5);
            dataSource.setMaxIdle(10);
        }
        else {
            try {
                URI jdbUri = new URI(JAWSDB_MARIA_URL);

                String username = jdbUri.getUserInfo().split(":")[0];
                String password = jdbUri.getUserInfo().split(":")[1];
                String port = String.valueOf(jdbUri.getPort());
                String jdbUrl = "jdbc:mariadb://" + jdbUri.getHost() + ":" + port + jdbUri.getPath();

                dataSource.setUrl(jdbUrl);
                dataSource.setValidationQuery("SELECT * FROM users WHERE user_id = 1");
                dataSource.setValidationQueryTimeout(3);
                dataSource.setTimeBetweenEvictionRunsMillis(5000);
                dataSource.setMaxConnLifetimeMillis(60 * 1000);
                dataSource.setUsername(username);
                dataSource.setPassword(password);
                dataSource.setMinIdle(5);
                dataSource.setMaxIdle(10);
            }
            catch (URISyntaxException exception) {
                exception.printStackTrace();
                System.out.println("JAWSDB_MARIA_URL variable contains a malformed URL.");
            }
        }
    }

    public static DatabaseConnection getDatabaseConnection() throws SQLException {
        return new DatabaseConnection(dataSource.getConnection());
    }

    private DatabaseConnectionPool() {
    }
}
