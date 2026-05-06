package impl;

import impl.exception.SPrimitiveConverterNullException;
import impl.exception.SPrimitiveNoRecord;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class Wrapper<R> implements SList.ToObjectFunction<R> {
    private final Parameter[] params;
    private final Constructor<R> constructor;

    public Wrapper(Class<R> clazz) {
        if (!clazz.isRecord()) {
            throw new SPrimitiveNoRecord(clazz);
        }

        //noinspection unchecked
        this.constructor = (Constructor<R>) Stream.of(clazz.getDeclaredConstructors()).max(Comparator.comparing(Constructor::getParameterCount)).get();
        this.params = this.constructor.getParameters();
    }

    @Override
    public R toObject(ResultSet set) throws SQLException {
        this.constructor.setAccessible(true);
        try {
            var map = new HashMap<String, Object>();
            for (int i = 0; i < set.getMetaData().getColumnCount(); i++) {
                map.put(set.getMetaData().getColumnLabel(i + 1).toLowerCase(), set.getObject(i + 1));
            }
            var as = Arrays.stream(params).map(convert(map)).toArray();
            return constructor.newInstance(as);
        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    protected Function<Parameter, Object> convert(Map<String, Object> map) {
        return a -> {
            var o = map.get(a.getName().toLowerCase());
            var type = a.getType();

            if (type.isPrimitive() && o == null) {
                throw new SPrimitiveConverterNullException(type, a.getName());
            } else if (o == null) {
                return null;
            }

            if (type == long.class || type == Long.class) {
                return ((Number) o).longValue();
            } else if (type == int.class || type == Integer.class) {
                return ((Number) o).intValue();
            } else if (type == float.class || type == Float.class) {
                return ((Number) o).floatValue();
            } else if (type == double.class || type == Double.class) {
                return ((Number) o).doubleValue();
            } else if (type == byte.class || type == Byte.class) {
                return ((Number) o).byteValue();
            } else if (type == short.class || type == Short.class) {
                return ((Number) o).shortValue();
            } else {
                return o;
            }
        };
    }
}
