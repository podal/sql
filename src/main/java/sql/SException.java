package sql;

import java.sql.SQLException;

/**
 * Base runtime exception for the SQL library.
 * <p>
 * This exception is used as the root of all unchecked exceptions
 * thrown by the library. It wraps SQL-related errors and provides
 * a consistent exception type for users of the API.
 */
public class SException extends RuntimeException {
    /**
     * Creates an empty {@code SException}.
     */
    public SException() {
        super();
    }

    /**
     * Creates an {@code SException} that wraps an underlying {@link SQLException}.
     *
     * @param e the underlying SQL exception that caused this error
     */
    public SException(SQLException e) {
        super(e);
    }

    /**
     * Creates an {@code SException} with a custom error message.
     *
     * @param m the detail message explaining the exception
     */
    public SException(String m) {
        super(m);
    }
}
