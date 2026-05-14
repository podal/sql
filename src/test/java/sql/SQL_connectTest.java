package sql;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SQL_connectTest {
    record Test1(int pk) {
    }

    @TempDir
    private Path tempDir;

    @Test
    @DisplayName("Error when no environments is provided")
    void no_env() {
        assertThrows(SException.class, () ->
                SQL.connect()
        );
    }

    @Test
    @DisplayName("All env is passed throw")
    void passThrow() {
        try {
            new TestDriver();
            System.setProperty("DB_URL", "jdbc:test:demo");
            System.setProperty("DB_USERNAME", "user");
            System.setProperty("DB_PASSWORD", "pass");
            var con = (TestDriver.TestConnection) SQL.connect().getConnection();
            assertNotNull(con);
            assertEquals("user", con.getInfo().getProperty("user"));
            assertEquals("pass", con.getInfo().getProperty("password"));
        } finally {
            System.clearProperty("DB_URL");
            System.clearProperty("DB_USERNAME");
            System.clearProperty("DB_PASSWORD");
        }
    }


    @Test
    @DisplayName("Test with sqlite")
    void test() {

        var dir = tempDir.resolve(Long.toString(System.currentTimeMillis(), Character.MAX_RADIX) + ".db");
        System.setProperty("DB_URL", "jdbc:sqlite:" + dir);
        SQL.connect();

        SQL.create("CREATE TABLE Test (pk number)");

        assertEquals(1, SQL.update("INSERT INTO Test (PK) VALUES (?)", 10));

        assertEquals(List.of(new Test1(10)), SQL.list(Test1.class, "SELECT * from Test"));

        assertEquals(List.of(), SQL.list(Test1.class, "SELECT * from Test WHERE pk = ?", 100));

        assertEquals(List.of(List.of(2)), SQL.list("SELECT 2"));

        SQL.close();
    }
}
