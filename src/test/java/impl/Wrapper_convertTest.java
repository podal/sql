package impl;

import impl.exception.SPrimitiveConverterNullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class Wrapper_convertTest {

    record Test1() {
    }

    private Wrapper<Test1> converter;
    private HashMap<String, Object> testMap;

    @BeforeEach
    void setUp() {
        converter = new Wrapper<>(Test1.class);
        testMap = new HashMap<>();

        testMap.put("age", 25);
        testMap.put("weight", 70L);
        testMap.put("height", 1.85);
        testMap.put("temp", 36.6f);
        testMap.put("level", (byte) 10);
        testMap.put("score", (short) 100);
        testMap.put("name", "Kalle");
    }

    private Parameter createMockParameter(String name, Class<?> type) {
        Parameter param = Mockito.mock(Parameter.class);
        when(param.getName()).thenReturn(name);
        //noinspection unchecked,rawtypes
        when(param.getType()).thenReturn((Class) type);
        return param;
    }

    @Test
    void testConvertInteger() {
        Function<Parameter, Object> func = converter.convert(testMap);
        Parameter p = createMockParameter("Age", int.class);
        Parameter p2 = createMockParameter("Age", Integer.class);

        assertEquals(25, func.apply(p));
        assertEquals(25, func.apply(p2));
    }

    @Test
    void testConvertLong() {
        Function<Parameter, Object> func = converter.convert(testMap);
        Parameter p = createMockParameter("weight", long.class);
        Parameter p2 = createMockParameter("weight", Long.class);

        assertEquals(70L, func.apply(p));
        assertEquals(70L, func.apply(p2));
    }

    @Test
    void testConvertDouble() {
        Function<Parameter, Object> func = converter.convert(testMap);
        Parameter p = createMockParameter("height", double.class);
        Parameter p2 = createMockParameter("height", Double.class);

        assertEquals(1.85, func.apply(p));
        assertEquals(1.85, func.apply(p2));
    }

    @Test
    void testConvertFloat() {
        Function<Parameter, Object> func = converter.convert(testMap);
        Parameter p = createMockParameter("temp", float.class);
        Parameter p2 = createMockParameter("temp", Float.class);

        assertEquals(36.6f, func.apply(p));
        assertEquals(36.6f, func.apply(p2));
    }

    @Test
    void testConvertByte() {
        Function<Parameter, Object> func = converter.convert(testMap);
        Parameter p = createMockParameter("level", byte.class);
        Parameter p2 = createMockParameter("level", Byte.class);

        assertEquals((byte) 10, func.apply(p));
        assertEquals((byte) 10, func.apply(p2));
    }

    @Test
    void testConvertShort() {
        Function<Parameter, Object> func = converter.convert(testMap);
        Parameter p = createMockParameter("score", short.class);
        Parameter p2 = createMockParameter("score", Short.class);

        assertEquals((short) 100, func.apply(p));
        assertEquals((short) 100, func.apply(p2));
    }

    @Test
    void testConvertDefaultCase() {
        Function<Parameter, Object> func = converter.convert(testMap);
        Parameter p = createMockParameter("name", String.class);

        assertEquals("Kalle", func.apply(p));
    }

    @Test
    void testNull() {
        Function<Parameter, Object> func = converter.convert(testMap);
        Parameter p = createMockParameter("hej", Integer.class);

        assertNull(func.apply(p));
    }

    @Test
    void testExceptionWhenPrimitiveAndNull() {
        Function<Parameter, Object> func = converter.convert(testMap);
        Parameter p = createMockParameter("hej", int.class);

        var e = assertThrows(SPrimitiveConverterNullException.class, () ->
                func.apply(p)
        );

        assertEquals("hej", e.getMethodName());
        assertEquals(int.class, e.getMethodType());
    }

    @Test
    void testCaseInsensitivity() {
        Function<Parameter, Object> func = converter.convert(testMap);
        Parameter p = createMockParameter("AGE", int.class);

        assertEquals(25, func.apply(p));
    }
}
