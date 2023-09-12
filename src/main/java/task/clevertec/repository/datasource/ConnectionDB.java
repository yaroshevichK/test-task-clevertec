package task.clevertec.repository.datasource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import task.clevertec.util.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static task.clevertec.repository.datasource.Queries.GET_CURRENT_DB;
import static task.clevertec.util.Constants.DB;
import static task.clevertec.util.Constants.INIT;
import static task.clevertec.util.Constants.MSG_WRONG_CONN;
import static task.clevertec.util.Constants.MSG_WRONG_INIT;
import static task.clevertec.util.Constants.MSG_WRONG_READ;
import static task.clevertec.util.Constants.OUT;
import static task.clevertec.util.Constants.PASSWORD;
import static task.clevertec.util.Constants.POOL_SIZE;
import static task.clevertec.util.Constants.POOL_STATEMENTS;
import static task.clevertec.util.Constants.PROP_DB;
import static task.clevertec.util.Constants.PROP_TABLES;
import static task.clevertec.util.Constants.URL;
import static task.clevertec.util.Constants.USER;

public class ConnectionDB {
    private static ConnectionDB instance = getInstance();
    private static ComboPooledDataSource poolConn;

    private ConnectionDB() {
        poolConn = new ComboPooledDataSource();

        poolConn.setJdbcUrl(getProperty(DB, URL));
        poolConn.setUser(getProperty(DB, USER));
        poolConn.setPassword(getProperty(DB, PASSWORD));

        poolConn.setMaxPoolSize(getProperty(DB, POOL_SIZE));
        poolConn.setMaxStatements(getProperty(DB, POOL_STATEMENTS));
    }

    private static <T> T getProperty(String root, String key) {
        return Configuration.getProperty(root, key);
    }

    public static ConnectionDB getInstance() {
        if (instance == null) {
            instance = new ConnectionDB();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            return poolConn.getConnection();
        } catch (SQLException e) {
            OUT.println(MSG_WRONG_CONN);
            return null;
        }
    }

    public static Connection getInitConnection() {
        try {
            return DriverManager
                    .getConnection(getProperty(INIT, URL),
                            getProperty(INIT, USER),
                            getProperty(INIT, PASSWORD));

        } catch (SQLException e) {
            OUT.println(MSG_WRONG_INIT);
            return null;
        }
    }

    public boolean initDatabase() {
        try (Connection connection = getInitConnection()) {
            if (connection == null) {
                OUT.println(MSG_WRONG_INIT);
                return false;
            }
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(GET_CURRENT_DB);

            if (result != null && result.next()) {
                return true;
            }

            statement = connection.createStatement();
            String pathStr = getProperty(INIT, PROP_DB);
            Path path = Path.of(pathStr);
            statement.executeUpdate(Files.readString(path));

        } catch (SQLException e) {
            OUT.println(MSG_WRONG_INIT);
            return false;
        } catch (IOException e) {
            OUT.println(MSG_WRONG_READ);
            return false;
        }

        try (Connection currConnection = getConnection()) {
            if (currConnection == null) {
                OUT.println(MSG_WRONG_INIT);
                return false;
            }
            Statement statement = currConnection.createStatement();
            String pathStr = getProperty(INIT, PROP_TABLES);
            Path path = Path.of(pathStr);
            try {
                statement.executeUpdate(Files.readString(path));
            } catch (IOException e) {
                OUT.println(MSG_WRONG_READ);
                return false;
            }
        } catch (SQLException e) {
            OUT.println(MSG_WRONG_CONN);
            return false;
        }

        return true;
    }
}
