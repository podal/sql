package sql;

import impl.SConnctionImpl;
import impl.SException;
import impl.exception.SConnectionNotStartedException;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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

    public static void close() {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    public static List<List<Object>> list(String sql, Object... args) {
        return getConnection().list(sql, args);
    }

    public static <R> List<R> list(Class<R> clazz, String sql, Object... args) {
        return getConnection().list(clazz, sql, args);
    }

    public static Optional<List<Object>> singel(String sql, Object... args) {
        return getConnection().singel(sql, args).stream().findFirst();
    }

    public static <R> Optional<R> singel(Class<R> clazz, String sql, Object... args) {
        return getConnection().singel(clazz, sql, args).stream().findFirst();
    }

    public static boolean create(String sql) {
        return getConnection().create(sql);
    }

    public static int update(String sql, Object... args) {
        return getConnection().update(sql, args);
    }
}
