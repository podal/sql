package impl;

import impl.exception.SPrimitiveNoRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WrapperTest {
    @Test
    void testConvertInteger() {
        var e = assertThrows(SPrimitiveNoRecord.class, () ->
                new Wrapper<>(String.class)
        );

        assertEquals(String.class, e.getRecordClass());
    }
}
