# SQL

> A lightweight Java library for writing SQL in modern Java.

SQL is a minimal database library focused on simplicity and plain SQL.

It requires **JDK 17+** and uses Java records for automatic object mapping.

---

# Why?

Modern Java has become much more lightweight with features like:

- records
- simpler application structure
- improved syntax

SQL follows the same philosophy:

- write plain SQL
- avoid heavy ORM frameworks
- keep database code readable

---

# Installation

Add the library to your project and include your JDBC driver.

Supported databases depend on the JDBC driver you use.

Examples:

- SQLite
- PostgreSQL
- MySQL
- MariaDB

---

# First Example

## Example1.java

```java
void main() {

    SQL.connect("jdbc:sqlite:sql.db");

    SQL.list(
        Book.class,
        "select id, name, isbn from books"
    ).forEach(System.out::println);

    SQL.close();
}

record Book(long id, String name, String isbn) {}
```

---

# Connection

## Simple connection

```java
SQL.connect("jdbc:sqlite:sql.db");
```

## Connection with credentials

```java
SQL.connect(
    "jdbc:mysql://localhost:3306/app",
    "user",
    "password"
);
```

---

# Auto Mapping

Rows can automatically be mapped to Java records.

```java
record Book(long id, String name, String isbn) {}
```

```java
List<Book> books =
    SQL.list(Book.class,
             "select id, name, isbn from books");
```

---

# Try-With-Resources

`SConnection` implements `AutoCloseable`.

## Example2.java

```java
void main() {

    try (var con = SQL.connect("jdbc:sqlite:sql.db")) {

        con.list(
            Room.class,
            "select id, name from rooms where id = ?",
            1L
        ).forEach(System.out::println);
    }
}

record Room(long id, String name) {}
```

---

# Query Methods

## Multiple Rows

```java
List<SRow> rows =
    SQL.list("select * from books");
```

---

## Mapped Objects

```java
List<Book> books =
    SQL.list(Book.class,
             "select * from books");
```

---

## Single Row

```java
Optional<SRow> row =
    SQL.single("select * from books where id = ?", 1);
```

---

## Single Object

```java
Optional<Book> book =
    SQL.single(Book.class,
               "select * from books where id = ?",
               1);
```

---

# Update Statements

Use `update(...)` for:

- INSERT
- UPDATE
- DELETE

## Insert Example

```java
int rows = SQL.update(
    "insert into books(name, isbn) values(?, ?)",
    "Effective Java",
    "9780134685991"
);
```

---

## Update Example

```java
int rows = SQL.update(
    "update books set name=? where id=?",
    "Modern Java",
    1L
);
```

---

## Delete Example

```java
int rows = SQL.update(
    "delete from books where id=?",
    1L
);
```

The method returns the number of affected rows.

---

# Schema Creation

Use `create(...)` for schema setup and table creation.

## Create Table

```java
SQL.create("""
    create table books (
        id integer primary key,
        name text not null,
        isbn text
    )
""");
```

---

# Philosophy

SQL intentionally stays small.

- No ORM
- No XML
- No query builders

Just Java and SQL.

## License

This project is licensed under the MIT License.