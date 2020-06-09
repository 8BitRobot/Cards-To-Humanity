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
            dataSource.setUsername("root");
            dataSource.setPassword("none");
            dataSource.setMinIdle(5);
            dataSource.setMaxIdle(10);
            dataSource.setMaxOpenPreparedStatements(100);
        }
        else {
            try {
                URI jdbUri = new URI(JAWSDB_MARIA_URL);

                String username = jdbUri.getUserInfo().split(":")[0];
                String password = jdbUri.getUserInfo().split(":")[1];
                String port = String.valueOf(jdbUri.getPort());
                String jdbUrl = "jdbc:mariadb://" + jdbUri.getHost() + ":" + port + jdbUri.getPath();

                dataSource.setUrl(jdbUrl);
                dataSource.setUsername(username);
                dataSource.setPassword(password);
                dataSource.setMinIdle(5);
                dataSource.setMaxIdle(10);
                dataSource.setMaxOpenPreparedStatements(100);
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
