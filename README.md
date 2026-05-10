[![Maven Central](https://img.shields.io/maven-central/v/io.github.podal/sql)](https://central.sonatype.com/artifact/io.github.podal/sql)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/java-17+-orange.svg)](https://openjdk.org/)

# SQL

A lightweight Java library for executing plain SQL with optional record mapping.

No ORM. No query builders. No magic.

Just SQL + Java.


## Quick Start

```java
SQL.connect("jdbc:sqlite:db");
List<Book> books = SQL.list(Book.class, "select * from books");
books.forEach(System.out::println);
```
SQL executes SQL and optionally maps results to Java records.

### Maven

Add the dependency:

```xml
<dependency>
    <groupId>io.github.podal</groupId>
    <artifactId>sql</artifactId>
    <version>1.0.0</version>
</dependency>
```


### Gradle (Kotlin DSL)

```kotlin
implementation("io.github.podal:sql:1.0.0")
```

### Gradle (Groovy)

```groovy
implementation 'io.github.podal:sql:1.0.0'
```


# Why?

Most Java SQL libraries introduce abstraction layers that hide SQL.

This library does the opposite: it keeps SQL explicit and adds only minimal convenience for execution and mapping.

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

## Raw Row Access

Use `SRow` when you want direct access to query results without mapping.

```java
List<SRow> rows = SQL.list("select * from books");

for (SRow row : rows) {
    System.out.println(row.get("name"));
}
```

`SRow` represents a single database row and allows column-based access.

```java
SRow row = SQL.single("select * from books where id = ?", 1).orElseThrow();

String name = row.get("name");
long id = row.get("id", Long.class);
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

## Transactions

SQL is designed around a stateless execution model in version 1.0.

Each query is executed independently using JDBC auto-commit mode.

This avoids connection state complexity and keeps the API minimal.

Transactions will be introduced in a future version if needed.

# License

This project is licensed under the MIT License.