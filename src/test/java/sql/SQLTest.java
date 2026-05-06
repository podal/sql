package sql;

import impl.exception.SConnectionNotStartedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SQLTest {
    @Test
    void create() {
        assertThrows(SConnectionNotStartedException.class, () -> {
            SQL.list("select 1");
        });
    }
}
