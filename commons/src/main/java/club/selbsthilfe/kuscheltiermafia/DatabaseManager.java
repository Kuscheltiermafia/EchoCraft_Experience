package club.selbsthilfe.kuscheltiermafia;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {

    private final HikariDataSource dataSource;

    public DatabaseManager(String host, String port, String database, String username, String password) {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:postgresql://%s:%s/%s", host, port, database));
        config.setUsername(username);
        config.setPassword(password);

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(1);
        config.setIdleTimeout(300000);
        config.setConnectionTimeout(10000);

        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
