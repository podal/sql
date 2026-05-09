package impl;

import sql.SException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SList {
    public interface ToObjectFunction<O> {
        O toObject(ResultSet r) throws SQLException;

        default void columns(HashMap<String, Integer> columns) {
        }
    }

    public static <R> List<R> _list(Connection connection, String s, Object[] objs, ToObjectFunction<R> f) {
        try (var st = connection.prepareStatement(s)) {
            for (int i = 0; i < objs.length; i++) {
                st.setObject(i + 1, objs[i]);
            }
            try (var row = st.executeQuery()) {
                var columns = new HashMap<String, Integer>();
                for (int i = 0; i < row.getMetaData().getColumnCount(); i++) {
                    columns.put(row.getMetaData().getColumnName(i + 1).toLowerCase(), i + 1);
                }
                f.columns(columns);
                if (!row.next()) {
                    return List.of();
                }
                var list = new ArrayList<R>();
                do {
                    list.add(f.toObject(row));
                } while (row.next());
                return list;
            }
        } catch (SQLException e) {
            throw new SException(e);
        }
    }
}
