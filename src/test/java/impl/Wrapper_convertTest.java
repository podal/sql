package impl;

import impl.exception.SPrimitiveConverterNullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Parameter;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Wrapper_convertTest {

    record Test1() {
    }

    private Wrapper<Test1> converter;
    private ResultSet testMap;

    @BeforeEach
    void setUp() {
        converter = new Wrapper<>(Test1.class);
        testMap = mock(ResultSet.class);
    }

    private Parameter createMockParameter(String name, Class<?> type) {
        Parameter param = mock(Parameter.class);
        when(param.getName()).thenReturn(name);
        //noinspection unchecked,rawtypes
        when(param.getType()).thenReturn((Class) type);
        return param;
    }

    @Test
    void testConvertInteger() {
        Parameter p = createMockParameter("Age", int.class);
        Parameter p2 = createMockParameter("Age", Integer.class);

        assertEquals(25, converter.convert(p, 25L));
        assertEquals(25, converter.convert(p2, 25));
    }

    @Test
    void testConvertLong() {
        Parameter p = createMockParameter("weight", long.class);
        Parameter p2 = createMockParameter("weight", Long.class);

        assertEquals(70L, converter.convert(p, 70L));
        assertEquals(70L, converter.convert(p2, 70));
    }

    @Test
    void testConvertDouble() {
        Parameter p = createMockParameter("height", double.class);
        Parameter p2 = createMockParameter("height", Double.class);

        assertEquals(1.85, converter.convert(p, 1.85));
        assertEquals(1.85, converter.convert(p2, 1.85));
    }

    @Test
    void testConvertFloat() {
        Parameter p = createMockParameter("temp", float.class);
        Parameter p2 = createMockParameter("temp", Float.class);

        assertEquals(36.6f, converter.convert(p, 36.6));
        assertEquals(36.6f, converter.convert(p2, 36.6));
    }

    @Test
    void testConvertByte() {
        Parameter p = createMockParameter("level", byte.class);
        Parameter p2 = createMockParameter("level", Byte.class);

        assertEquals((byte) 10, converter.convert(p, 10));
        assertEquals((byte) 10, converter.convert(p2, 10L));
    }

    @Test
    void testConvertShort() {
        Parameter p = createMockParameter("score", short.class);
        Parameter p2 = createMockParameter("score", Short.class);

        assertEquals((short) 100, converter.convert(p, 100));
        assertEquals((short) 100, converter.convert(p2, 100L));
    }

    @Test
    void testConvertDefaultCase() {
        Parameter p = createMockParameter("name", String.class);

        assertEquals("Kalle", converter.convert(p, "Kalle"));
    }

    @Test
    void testNull() {
        Parameter p = createMockParameter("hej", Integer.class);

        assertNull(converter.convert(p, null));
    }

    @Test
    void testExceptionWhenPrimitiveAndNull() {
        var func = converter.convert(testMap);
        Parameter p = createMockParameter("hej", int.class);

        var e = assertThrows(SPrimitiveConverterNullException.class, () ->
                converter.convert(p, null)
        );

        assertEquals("hej", e.getMethodName());
        assertEquals(int.class, e.getMethodType());
    }
}
