package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {
    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());

    private static Connection connection = null;
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "girolamo";
    private DBConnection() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                LOGGER.info("Connessione al database stabilita con successo!");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore di connessione al database", e);
        }
        return connection;
    }
}
