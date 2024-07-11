package net.worldmc.merovingian.managers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.worldmc.merovingian.Merovingian;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {
    private HikariDataSource dataSource;
    private final Merovingian plugin;

    public DatabaseManager(Merovingian plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        FileConfiguration config = plugin.getConfig();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getString("database.jdbc-url", "jdbc:mysql://localhost:3306/database"));
        hikariConfig.setUsername(config.getString("database.username", "username"));
        hikariConfig.setPassword(config.getString("database.password", "password"));

        dataSource = new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}