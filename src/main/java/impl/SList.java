package impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SList {
    public interface ToObjectFunction<O> {
        O toObject(ResultSet r) throws SQLException;
    }

    public static <R> List<R> _list(Connection connection, String s, Object[] objs, ToObjectFunction<R> f) {
        try (var st = connection.prepareStatement(s)) {
            for (int i = 0; i < objs.length; i++) {
                st.setObject(i + 1, objs[i]);
            }
            try (var row = st.executeQuery()) {
                /*for (int i = 0; i < row.getMetaData().getColumnCount(); i++) {
                    IO.println(row.getMetaData().getColumnName(i + 1));
                }*/
                var list = new ArrayList<R>();
                while (row.next()) {
                    list.add(f.toObject(row));
                }
                return list;
            }
        } catch (SQLException e) {
            throw new SException(e);
        }
    }
}
