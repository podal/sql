package sql;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/**
 * Represents an active database connection used for executing SQL queries
 * and updates.
 *
 * <p>An {@code SConnection} can retrieve query results as {@link SRow}
 * instances or automatically map rows to custom Java objects.</p>
 *
 * <p>This interface extends {@link AutoCloseable}, allowing it to be used
 * in try-with-resources statements.</p>
 */
public interface SConnection extends AutoCloseable {

    /**
     * Returns the currently active database connection.
     *
     * @return the active {@link Connection}
     * @throws IllegalStateException if no connection is currently active
     */
    Connection getConnection();
    /**
     * Closes this connection and releases any associated resources.
     */
    @Override
    void close();

    /**
     * Executes a query and returns all resulting rows.
     *
     * @param sql the SQL query to execute
     * @param args optional query parameters
     * @return a list containing all matching rows
     */
    List<SRow> list(String sql, Object... args);

    /**
     * Executes a query and maps all resulting rows to instances of the
     * specified class.
     *
     * @param <R> the result type
     * @param clazz the target class used for row mapping
     * @param sql the SQL query to execute
     * @param args optional query parameters
     * @return a list of mapped result objects
     */
    <R> List<R> list(Class<R> clazz, String sql, Object... args);

    /**
     * Executes a query and returns the first matching row, if present.
     *
     * @param sql the SQL query to execute
     * @param args optional query parameters
     * @return an {@link Optional} containing the first row, or empty if no row exists
     */
    Optional<SRow> single(String sql, Object... args);

    /**
     * Executes a query and maps the first matching row to an instance of
     * the specified class.
     *
     * @param <R> the result type
     * @param clazz the target class used for row mapping
     * @param sql the SQL query to execute
     * @param args optional query parameters
     * @return an {@link Optional} containing the mapped object,
     *         or empty if no row exists
     */
    <R> Optional<R> single(Class<R> clazz, String sql, Object... args);

    /**
     * Executes a SQL statement intended for schema creation or modification.
     *
     * @param sql the SQL statement to execute
     * @return {@code true} if execution succeeded, otherwise {@code false}
     */
    boolean create(String sql);

    /**
     * Executes an update statement such as INSERT, UPDATE, or DELETE.
     *
     * @param sql the SQL statement to execute
     * @param args optional query parameters
     * @return the number of affected rows
     */
    int update(String sql, Object ... args);
}
