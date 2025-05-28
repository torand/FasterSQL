Changelog
=========

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- ...
 
### Changed
- ...

### Deprecated
- ...

### Removed
- ...

### Fixed
- ...

## [1.7.0] - 2025-05-??

### Added
- Supporting SQL dialect of SQLite.

## [1.6.0] - 2025-05-16

### Added
- Supporting SQL dialect of MS Access.

### Fixed
- H2 dialect now correctly supports TO_NUMBER through an inline user defined db function.

## [1.5.0] - 2025-05-12

### Added
- Supporting SQL dialect of Microsoft SQL Server.

### Changed
- Throws when using FOR UPDATE in SELECT with projections from more than one table.

## [1.4.0] - 2025-03-06

### Added
- Supporting multiple subqueries in FROM clause.
- Supporting subqueries as projections.
- Supporting the HAVING clause.
- IS NULL operator now supports any expression as operand.
- ORDER BY now supports expressions.

## [1.3.0] - 2025-02-21

### Added
- Statement parameters of type OffsetDateTime and ZonedDateTime are converted into Timestamp.
- Supporting the 'current_date' and 'current_time' system functions.
- Supporting the 'round', 'abs', 'ceil' and 'floor' scalar functions
- Validates that column aliases in ORDER BY are specified in the SELECT clause.
- ORDER BY now supports column position as order expression.
- ORDER BY now supports the 'nulls first' and 'nulls last' clauses.
- Supporting the modulo arithmetic operator.

### Fixed
- Correctly generates SQL for SELECT FOR UPDATE.

## [1.2.0] - 2025-02-19

### Added
- Supporting the 'to_char', 'concat' and 'length' scalar functions.
- INSERT and UPDATE now support any expression as column values.

### Fixed
- Reversed limit - offset clause ordering for H2 and MySQL dialects.
- Throws when using toChar() in MySQL and MariaDb dialects.

## [1.1.0] - 2025-02-13

### Added
- Constants (scalar values) can be specified using the new Constants.$ factory method.
- Supporting the 'substring' scalar function.
- ORDER BY now accepts plain text aliases, not only projections.
- Aggregate functions now support any expression, not only fields.

## [1.0.0] - 2025-02-04

### Added
- Initial version
