package impl;

import sql.SConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SConnctionImpl implements SConnection {
    private final Connection connection;
    private final AutoCloseable closeable;

    public SConnctionImpl(Connection connection, AutoCloseable closeable) throws SQLException {
        this.connection = connection;
        this.closeable = closeable;
        this.connection.setAutoCommit(true);
    }

    @Override
    public void close() {
        try {
            try {
                closeable.close();
            } catch (Exception ignore) {
            }
            connection.close();
        } catch (SQLException e) {
            throw new SException(e);
        }
    }

    @Override
    public List<List<Object>> list(String s, Object... objs) {
        return SList._list(connection, s, objs, row -> {
            var nr = row.getMetaData().getColumnCount();
            var rowData = new ArrayList<>(nr);
            for (int i = 0; i < nr; i++) {
                rowData.add(row.getObject(i + 1));
            }
            return rowData;
        });
    }

    @Override
    public <R> List<R> list(Class<R> clazz, String s, Object... objs) {
        //noinspection rawtypes,unchecked
        return SList._list(connection, s, objs, new Wrapper(clazz));
    }

    @Override
    public Optional<List<Object>> singel(String sql, Object... args) {
        return list(sql, args).stream().findFirst();
    }

    @Override
    public <R> Optional<R> singel(Class<R> clazz, String sql, Object... args) {
        return list(clazz, sql, args).stream().findFirst();
    }

    @Override
    public boolean create(String sql) {
        try (var s = connection.prepareStatement(sql)) {
            return s.execute();
        } catch (SQLException e) {
            throw new SException(e);
        }
    }

    public int update(String sql, Object... args) {
        try (var s = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                s.setObject(i + 1, args[i]);
            }
            return s.executeUpdate();
        } catch (SQLException e) {
            throw new SException(e);
        }
    }
}
