package example;

import sql.SQL;

import java.io.File;

import static java.lang.System.out;

public class WithTry {
    public static void main(String[] args) {
        new File("sql.db").delete();

        try (var con = SQL.connect("jdbc:sqlite:sql.db")) {
            con.create("CREATE TABLE books (id PK, name varchar(50))");

            con.update("INSERT INTO Books VALUES (?,?)", 1L, "Rebecca");
            con.update("INSERT INTO Books VALUES (?,?)", 2L, "Great Expectations");

            con.list("SELECT * FROM Books").forEach(out::println);
            con.singel(Books.class, "SELECT * FROM Books where id = ?", 1L).ifPresent(out::println);
        }
    }
}

record Books(Long id, String name) {
}