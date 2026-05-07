package sql;

import java.util.List;
import java.util.Optional;

public interface SConnection extends AutoCloseable {
    @Override
    void close();

    List<List<Object>> list(String sql, Object... args);

    <R> List<R> list(Class<R> clazz, String sql, Object... args);

    Optional<List<Object>> single(String sql, Object... args);

    <R> Optional<R> single(Class<R> clazz, String sql, Object... args);

    boolean create(String sql);

    int update(String sql, Object ... args);
}
