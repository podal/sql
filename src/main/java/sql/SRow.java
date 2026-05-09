package sql;

import java.util.List;

public interface SRow extends List<Object> {
    List<String> columns();

    Object get(String column);

    <T> T get(String column, Class<T> type);
}
