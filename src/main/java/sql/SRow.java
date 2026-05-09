package sql;

import java.util.List;

/**
 * Represents a single row returned from a database query.
 *
 * <p>An {@code SRow} behaves like a list of column values while also
 * providing access by column name.</p>
 */
public interface SRow extends List<Object> {
    /**
     * Returns the column names in this row.
     *
     * @return a list of column names
     */
    List<String> columns();

    /**
     * Returns the value of the specified column.
     *
     * @param column the column name
     * @return the column value, or {@code null} if the value is SQL NULL
     */
    Object get(String column);

    /**
     * Returns the value of the specified column cast to the given type.
     *
     * @param <T> the expected type
     * @param column the column name
     * @param type the expected class type
     * @return the column value converted or cast to the requested type
     */
    <T> T get(String column, Class<T> type);
}
