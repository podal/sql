package sql;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SQLIT {
    record Test1(int pk) {
    }

    @TempDir
    private Path tempDir;

    @Test
    void simple() {
        var dir = tempDir.resolve(Long.toString(System.currentTimeMillis(), Character.MAX_RADIX) + ".db");

        SQL.connect("jdbc:sqlite:" + dir);

        SQL.create("CREATE TABLE Test (pk number)");

        assertEquals(1, SQL.update("INSERT INTO Test (PK) VALUES (?)", 10));

        assertEquals(List.of(new Test1(10)), SQL.list(Test1.class, "SELECT * from Test"));

        assertEquals(List.of(), SQL.list(Test1.class, "SELECT * from Test WHERE pk = ?", 100));

        assertEquals(List.of(List.of(2)), SQL.list("SELECT 2"));

        SQL.close();
    }
}
