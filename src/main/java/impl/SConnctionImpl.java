package impl;

import sql.SConnection;
import sql.SException;
import sql.SRow;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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

    private record RowInfo(int count, Map<String, Integer> columnMap) {
    }

    @Override
    public List<SRow> list(String s, Object... objs) {
        var _info = new AtomicReference<RowInfo>();
        return SList._list(connection, s, objs, row -> {
            var info = _info.get();
            if (info == null) {
                ResultSetMetaData meta = row.getMetaData();
                var count = meta.getColumnCount();
                var columnMap = new LinkedHashMap<String, Integer>();
                for (int i = 0; i < count; i++) {
                    columnMap.put(meta.getColumnName(i + 1), i + 1);
                }
                _info.set(info = new RowInfo(count, columnMap));
            }
            var rowData = new ArrayList<>(info.count);
            for (int i = 0; i < info.count; i++) {
                rowData.add(row.getObject(i + 1));
            }
            return new SRowImpl(info.columnMap, rowData);
        });
    }

    @Override
    public <R> List<R> list(Class<R> clazz, String s, Object... objs) {
        //noinspection rawtypes,unchecked
        return SList._list(connection, s, objs, new Wrapper(clazz));
    }

    @Override
    public Optional<SRow> single(String sql, Object... args) {
        return list(sql, args).stream().findFirst();
    }

    @Override
    public <R> Optional<R> single(Class<R> clazz, String sql, Object... args) {
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
