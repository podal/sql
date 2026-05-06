package example;

import sql.SQL;

import java.io.File;

import static java.lang.System.out;

public class Simple {
    public static void main(String[] args) {
        new File("sql.db").delete();

        SQL.connect("jdbc:sqlite:sql.db");
        SQL.create("CREATE TABLE names (id PK, name varchar(50))");

        SQL.update("INSERT INTO names VALUES (?,?)", 1L, "Mark");
        SQL.update("INSERT INTO names VALUES (?,?)", 2L, "Sarah");

        SQL.list("SELECT * FROM names").forEach(out::println);
        SQL.singel(Names.class, "SELECT * FROM names where id = ?", 1L).ifPresent(out::println);
        SQL.close();
    }
}

record Names(Long id, String name) {
}