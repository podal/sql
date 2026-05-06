package sql;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SConnectionIT {
    record Test1(int pk) {
    }

    @TempDir
    private Path tempDir;

    @Test
    void simple() {
        var dir = tempDir.resolve(Long.toString(System.currentTimeMillis(), Character.MAX_RADIX) + ".db");

        try (var con = SQL.connect("jdbc:sqlite:" + dir)) {
            con.create("CREATE TABLE Test (pk number)");

            con.update("INSERT INTO Test (PK) VALUES (?)", 10);

            assertEquals(List.of(new Test1(10)), con.list(Test1.class, "SELECT * from Test"));

            assertEquals(List.of(), con.list(Test1.class, "SELECT * from Test WHERE pk = ?", 100));

            assertEquals(List.of(List.of(2)), con.list("SELECT 2"));
        }
    }
}
