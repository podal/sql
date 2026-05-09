package impl;

import impl.exception.SPrimitiveConverterNullException;
import impl.exception.SPrimitiveNoRecord;
import sql.SException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

public class Wrapper<R> implements SList.ToObjectFunction<R> {
    private final Parameter[] params;
    private Map<Parameter, Optional<Integer>> paramIndex;
    private final Constructor<R> constructor;

    public Wrapper(Class<R> clazz) {
        if (!clazz.isRecord()) {
            throw new SPrimitiveNoRecord(clazz);
        }

        //noinspection unchecked
        this.constructor = (Constructor<R>) Stream.of(clazz.getDeclaredConstructors()).max(Comparator.comparing(Constructor::getParameterCount)).get();
        this.constructor.setAccessible(true);
        this.params = this.constructor.getParameters();
    }

    @Override
    public void columns(HashMap<String, Integer> columns) {
        paramIndex = new LinkedHashMap<>();
        for (var p : this.params) {
            paramIndex.put(p, ofNullable(columns.get(p.getName().toLowerCase())));
        }
    }

    @Override
    public R toObject(ResultSet set) throws SQLException {
        try {
            var as = paramIndex.entrySet().stream().map(convert(set)).toArray();
            return constructor.newInstance(as);
        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    protected Function<Map.Entry<Parameter, Optional<Integer>>, Object> convert(ResultSet set) {
        return a ->
                convert(a.getKey(), a.getValue().map(i -> {
                    try {
                        return set.getObject(i);
                    } catch (SQLException e) {
                        throw new SException(e);
                    }
                }).orElse(null));
    }

    protected Object convert(Parameter p, Object o) {
        var type = p.getType();

        if (type.isPrimitive() && o == null) {
            throw new SPrimitiveConverterNullException(type, p.getName());
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
    }

}
