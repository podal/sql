package sql;

import impl.SConnctionImpl;
import impl.exception.SConnectionNotStartedException;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Utility class providing simplified static access to database operations.
 *
 * <p>The {@code SQL} class manages a shared database connection and offers
 * convenience methods for executing queries, updates, and schema operations.</p>
 *
 * <p>Query results can either be returned as {@link SRow} objects or mapped
 * directly to custom Java classes.</p>
 */
public class SQL {

    private static SConnection connection;

    private static SConnection getConnection() {
        if (connection == null) {
            throw new SConnectionNotStartedException("""
                    You have to create a connection to a database.
                    
                    use:
                      SQL.connect(...)
                    
                    Example:
                      SQL.connect("jdbc:sqlite:my.db")
                    """);
        }
        return connection;
    }

    /**
     * Opens a new database connection using the specified credentials.
     *
     * @param url the database connection URL
     * @param user the database username
     * @param pass the database password
     * @return an active {@link SConnection}
     */
    public static SConnection connect(String url, String user, String pass) {
        try {
            if (connection != null) {
                connection.close();
            }
            return connection = new SConnctionImpl(DriverManager.getConnection(url, user, pass), () -> connection = null);
        } catch (SQLException e) {
            throw new SException(e);
        }
    }

    /**
     * Opens a new database connection using the specified connection URL.
     *
     * @param url the database connection URL
     * @return an active {@link SConnection}
     */
    public static SConnection connect(String url) {
        try {
            if (connection != null) {
                connection.close();
            }
            return connection = new SConnctionImpl(DriverManager.getConnection(url), () -> connection = null);
        } catch (SQLException e) {
            throw new SException(e);
        }
    }

    /**
     * Closes the currently active shared connection, if one exists.
     */
    public static void close() {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    /**
     * Executes a query and returns all matching rows.
     *
     * @param sql the SQL query to execute
     * @param args optional query parameters
     * @return a list of resulting rows
     */
    public static List<SRow> list(String sql, Object... args) {
        return getConnection().list(sql, args);
    }

    /**
     * Executes a query and maps all resulting rows to instances of
     * the specified class.
     *
     * @param <R> the result type
     * @param clazz the target class used for row mapping
     * @param sql the SQL query to execute
     * @param args optional query parameters
     * @return a list of mapped result objects
     */
    public static <R> List<R> list(Class<R> clazz, String sql, Object... args) {
        return getConnection().list(clazz, sql, args);
    }

    /**
     * Executes a query and returns the first matching row, if present.
     *
     * @param sql the SQL query to execute
     * @param args optional query parameters
     * @return an {@link Optional} containing the first row,
     *         or empty if no row exists
     */
    public static Optional<SRow> singel(String sql, Object... args) {
        return getConnection().single(sql, args).stream().findFirst();
    }

    /**
     * Executes a query and maps the first matching row to an instance
     * of the specified class.
     *
     * @param <R> the result type
     * @param clazz the target class used for row mapping
     * @param sql the SQL query to execute
     * @param args optional query parameters
     * @return an {@link Optional} containing the mapped object,
     *         or empty if no row exists
     */
    public static <R> Optional<R> singel(Class<R> clazz, String sql, Object... args) {
        return getConnection().single(clazz, sql, args).stream().findFirst();
    }

    /**
     * Executes a SQL statement intended for schema creation or modification.
     *
     * @param sql the SQL statement to execute
     * @return {@code true} if execution succeeded, otherwise {@code false}
     */
    public static boolean create(String sql) {
        return getConnection().create(sql);
    }

    /**
     * Executes an update statement such as INSERT, UPDATE, or DELETE.
     *
     * @param sql the SQL statement to execute
     * @param args optional query parameters
     * @return the number of affected rows
     */
    public static int update(String sql, Object... args) {
        return getConnection().update(sql, args);
    }
}
