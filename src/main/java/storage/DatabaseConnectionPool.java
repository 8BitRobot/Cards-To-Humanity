package storage;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;

class DatabaseConnectionPool {

    private static int MAX_ALLOWED_DB_CONNECTIONS = 10;
    private static ComboPooledDataSource dataSource = new ComboPooledDataSource();

    static {
        try {
            // See https://devcenter.heroku.com/articles/jawsdb-maria#using-jawsdb-maria-with-java
            String JAWSDB_MARIA_URL = System.getenv("JAWSDB_MARIA_URL");

            if (JAWSDB_MARIA_URL == null || JAWSDB_MARIA_URL.equals("")) {
                dataSource.setDriverClass("org.mariadb.jdbc.Driver");
                dataSource.setJdbcUrl("jdbc:mariadb://localhost:3306/carecards?socketTimeout=500");
                dataSource.setUser("root");
                dataSource.setPassword("none");
                dataSource.setInitialPoolSize(5);
                dataSource.setMinPoolSize(3);
                dataSource.setMaxPoolSize(MAX_ALLOWED_DB_CONNECTIONS - 2);
                dataSource.setAcquireIncrement(2);
                dataSource.setAcquireRetryAttempts(10);
                dataSource.setAutoCommitOnClose(true);
                dataSource.setAutomaticTestTable("connection_pooling_test_table");
                dataSource.setIdleConnectionTestPeriod(60);
                dataSource.setTestConnectionOnCheckout(true); // TODO: Remove this if at all possible because it impedes performance.
                dataSource.setMaxConnectionAge(60 * 5);
                dataSource.setMaxStatementsPerConnection(50);
            }
            else {
                try {
                    URI jdbUri = new URI(JAWSDB_MARIA_URL);

                    String username = jdbUri.getUserInfo().split(":")[0];
                    String password = jdbUri.getUserInfo().split(":")[1];
                    String port = String.valueOf(jdbUri.getPort());
                    String jdbUrl = "jdbc:mariadb://" + jdbUri.getHost() + ":" + port + jdbUri.getPath();

                    dataSource.setDriverClass("org.mariadb.jdbc.Driver");
                    dataSource.setJdbcUrl(jdbUrl);
                    dataSource.setUser(username);
                    dataSource.setPassword(password);
                    dataSource.setInitialPoolSize(5);
                    dataSource.setMinPoolSize(3);
                    dataSource.setMaxPoolSize(MAX_ALLOWED_DB_CONNECTIONS - 2);
                    dataSource.setAcquireIncrement(2);
                    dataSource.setAcquireRetryAttempts(10);
                    dataSource.setAutoCommitOnClose(true);
                    dataSource.setAutomaticTestTable("connection_pooling_test_table");
                    dataSource.setIdleConnectionTestPeriod(60);
                    dataSource.setTestConnectionOnCheckout(true); // TODO: Remove this if at all possible because it impedes performance.
                    dataSource.setMaxConnectionAge(60 * 5);
                    dataSource.setMaxStatementsPerConnection(50);
                } catch (URISyntaxException exception) {
                    exception.printStackTrace();
                    System.out.println("JAWSDB_MARIA_URL variable contains a malformed URL.");
                }
            }
        }
        catch (PropertyVetoException exception) {
            exception.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        return connection;
    }

    private DatabaseConnectionPool() {
    }
}
