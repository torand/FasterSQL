FasterSQL
=========

[![CI](https://github.com/torand/FasterSQL/actions/workflows/continuous-integration.yml/badge.svg)](https://github.com/torand/FasterSQL/actions/workflows/continuous-integration.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.torand/fastersql.svg?label=maven%20central)](https://central.sonatype.com/artifact/io.github.torand/fastersql)
[![Javadoc](https://img.shields.io/badge/javadoc-online-green)](https://torand.github.io/FasterSQL/apidocs/)
[![Coverage](https://coveralls.io/repos/github/torand/FasterSQL/badge.svg?branch=main)](https://coveralls.io/github/torand/FasterSQL?branch=main)
[![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202.0-orange)](LICENSE)

Java library for a faster and more efficient integration with relational databases.

## Table of Contents

- [Overview](#overview)
- [Usage](#usage)
- [Getting Started](#getting-started)
- [Logging](#logging)
- [Contributing](#contributing)
- [License](#license)

## Overview

Writing SQL statements as Java strings and executing them with the JDBC API is tedious, time-consuming, repetitive and
thus error-prone.

FasterSQL lets you write SQL using plain Java, by emulating the syntax and structure of SQL DQL and DML statements
using a set of predefined classes, factories and methods representing the clauses, expressions and functions
from the SQL language. A Java DSL (Domain Specific Language) for database operations.

### Benefits

* Enables code completion of standard SQL clauses and your database model in the IDE.
* SQL authored as Java code enables SQL error detection at compile time.
* Semantic validation at run time produces more readable error messages than the JDBC wrapped messages from the RDBMS.
* Write SQL once, run against (almost) any database engine using the SQL dialect awareness feature. The DSL emulates
  the ANSI/ISO SQL standard ([ISO/IEC 9075](https://www.iso.org/standard/76583.html)), while a statement builder translates into the SQL dialect of the underlying RDBMS product.
* Simplified passing of arguments to prepared statements. This is particularly tedious when the number of query predicates
  are dynamic and not known when the code is written.
* Light-weight, concise library not pretending to be yet another Object Relational Mapping (ORM) tool. Just easing the
  most cumbersome parts of database querying using JDBC.
* Easily extend the API with clauses and elements not currently supported by implementing relevant interfaces - or,
  preferably, [register a feature request](https://github.com/torand/FasterSQL/issues/new).

### Example

Consider the following function executing a query using JDBC constructs only:

```java
ResultSet findPersons(Connection connection) {
    String sql = """
        select upper(P.NAME) P_NAME, P.SSN P_SSN, A.STREET A_STREET, A.ZIP A_ZIP
        from PERSON P 
        left outer join ADDRESS A on P.ID = A.PERSON_ID 
        where P.SSN = ? 
        and P.NAME is not null 
        and (A.ZIP = ? or A.ZIP = ?) 
        and A.COUNTRY in (?, ?, ?) 
        order by P_SSN asc, P_NAME desc 
        limit ? 
        offset ?        
    """;
    
    PreparedStatement stmt = connection.prepareStatement(sql);
    stmt.setString(1, "31129912345");
    stmt.setString(2, "7089");
    stmt.setString(3, "7089");
    stmt.setString(4, "NOR");
    stmt.setString(5, "SWE");
    stmt.setString(6, "DEN");
    stmt.setInt(7, 10);
    stmt.setInt(8, 90);
    
    return stmt.executeQuery();
}
```

Using FasterSQL this function can be simplified (and made more readable) like this:

```java
ResultSet findPersons(Connection connection) {
    PreparableStatement stmt =
        select(upper(PERSON.NAME), PERSON.SSN, ADDRESS.STREET, ADDRESS.ZIP)
            .from(PERSON)
            .leftOuterJoin(PERSON.ID.on(ADDRESS.PERSON_ID))
            .where(PERSON.SSN.eq("31129912345")
                .and(not(PERSON.NAME.isNull()))
                .and(ADDRESS.ZIP.eq("7089").or(ADDRESS.ZIP.eq("7088")))
                .and(ADDRESS.COUNTRY.in("NOR", "SWE", "DEN")))
            .orderBy(PERSON.SSN.asc(), PERSON.NAME.desc())
            .limit(10)
            .offset(90);

    return using(connection).prepare(stmt).executeQuery();
}
```

The example assumes a connection to a MySQL database.

### Supported RDBMS Products

FasterSQL supports a majority of the [most popular](https://db-engines.com/en/ranking/relational+dbms) relational database management systems:

* Oracle
* SQL Server
* MySQL
* MariaDb
* PostgreSQL
* MS Access
* SQLite
* HSQLDB
* H2

### Supported SQL Features

FasterSQL supports the most widely used SQL data query and manipulation language features:

* Statements: SELECT, SELECT FOR UPDATE, INSERT (both single row and batch), UPDATE, DELETE, TRUNCATE
* Joins: inner, left outer, right outer, full outer
* Scalar string functions: upper, lower, to_number, to_char, substring, concat, length
* Scalar math functions: round, abs, ceil, floor, ln, exp, sqrt, power
* Aggregate functions: count, max, min, sum, avg
* System functions: current_timestamp, current_date, current_time
* Comparison operators: eq (=), ge (>=), gt (>), le (<=), lt (<), ne (<>), between
* Arithmetic operators: add (+), subtract (-), multiply (*), divide (/), modulo (%), negate (-)
* Logical operators: and, or, not
* Other operators: in, is null, like, exists
* Expressions: Any nested expression using functions, operators and constant values
* Ordering: asc, desc, nulls first/last
* Grouping: group by, having
* Subqueries: Supported as projections (SELECT clause), relations (FROM clause) and as right operand of predicates (WHERE clause)

### Supported Statement Parameters

Statement parameter values are registered with a PreparedStatement using the setObject method, by default. This covers all the basic
Java primitive types like Integer, Long, BigInteger, Float, Double, BigDecimal, String and Boolean. More complex standard Java types
are transformed into their Java SQL counterparts before registration, as specified by the table below:

| Java Standard Type | Java SQL Type | Parameter method |
|--------------------|---------------|------------------|
| Instant            | Timestamp     | setTimestamp     |
| LocalDateTime      | Timestamp     | setTimestamp     |
| LocalDate          | Date          | setDate          |
| OffsetDateTime     | Timestamp     | setTimestamp     |
| ZonedDateTime      | Timestamp     | setTimestamp     |
| UUID               | String        | setObject        |
| URI                | String        | setObject        |
| Enum               | String        | setObject        |
| InputStream        | Blob          | setBinaryStream  |

## Usage

The package is available from the [Maven Central Repository](https://central.sonatype.com/artifact/io.github.torand/fastersql).

### Maven

Include in a ```pom.xml``` file like this:

```xml
<dependencies>
  <dependency>
    <groupId>io.github.torand</groupId>
    <artifactId>fastersql</artifactId>
    <version>1.8.0</version>
  </dependency>
</dependencies>
```

### Gradle

Include in a ```build.gradle``` file like this:

```groovy
dependencies {
    implementation 'io.github.torand:fastersql:1.8.0'
}
```

## Getting Started

1. Define a database model using the [Table](https://github.com/torand/FasterSQL/blob/main/src/main/java/io/github/torand/fastersql/model/Table.java) and [Column](https://github.com/torand/FasterSQL/blob/main/src/main/java/io/github/torand/fastersql/model/Column.java) classes
2. Write an SQL statement using the DSL factories. E.g. start with the [Statements.select](https://github.com/torand/FasterSQL/blob/main/src/main/java/io/github/torand/fastersql/statement/Statements.java#L39) factory method.
3. Transform to a JDBC PreparedStatement with parameters set automatically using the [PreparedStatementBuilder](https://github.com/torand/FasterSQL/blob/main/src/main/java/io/github/torand/fastersql/statement/PreparedStatementBuilder.java) class
4. Execute the PreparedStatement as normal.

## Logging

The library outputs diagnostics etc using the [SLF4J](https://www.slf4j.org/) log framework. Visit its web page to
get instructions on how to integrate with the specific log framework used by your application.

The generated SQL statement in the dialect of the underlying RDBMS is logged using log level DEBUG. To make them appear
in your application log file, configure the following logger to print DEBUG level messages:

```
io.github.torand.fastersql.statement.PreparedStatementBuilder
```

## Contributing

1. Fork it (https://github.com/torand/FasterSQL/fork)
2. Create your feature branch (git checkout -b feature/fooBar)
3. Commit your changes (git commit -am 'Add some fooBar')
4. Push to the branch (git push origin feature/fooBar)
5. Create a new Pull Request

## License

This project is licensed under the [Apache-2.0 License](LICENSE).
