Changelog
=========

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Supporting the 'to_char', 'concat' and 'length' scalar functions.
- INSERT and UPDATE now support any expression as field values.

### Changed
- ...

### Deprecated
- ...

### Removed
- ...

### Fixed
- ...

## [1.1.0] - 2025-02-13

### Added
- Constants (scalar values) can be specified using the new Constants.$ factory method.
- Supporting the 'substring' scalar function.
- Orders.asc and Orders.desc now accept plain text aliases, not only projections.
- Aggregate functions now support any expression, not only fields.

## [1.0.0] - 2025-02-04

### Added
- Initial version
