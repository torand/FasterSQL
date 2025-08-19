package io.github.torand.fastersql.dialect;

import io.github.torand.fastersql.statement.FasterSQLException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Resolves SQL dialect based on an open JDBC {@link Connection}.
 */
public final class DialectResolver {
    private DialectResolver() {}

    /**
     * Creates the {@link Dialect} instance corresponding to database vendor associated with specified connection.
     * @param connection the connection.
     * @return the {@link Dialect} instance.
     */
    public static Dialect fromConnection(Connection connection) {
        try {
            String productName = connection.getMetaData().getDatabaseProductName().toLowerCase();

            if (productName.contains("h2")) {
                return new H2Dialect().withCustomizations(connection);
            } else if (productName.contains("mysql")) {
                return new MySqlDialect();
            } else if (productName.contains("mariadb")) {
                return new MariaDbDialect();
            } else if (productName.contains("postgresql")) {
                return new PostgreSqlDialect();
            } else if (productName.contains("oracle")) {
                return new OracleDialect();
            } else if (productName.contains("sql server")) {
                return new SqlServerDialect();
            } else if (productName.contains("access")) {
                return new AccessDialect();
            } else if (productName.contains("sqlite")) {
                return new SqliteDialect();
            } else if (productName.contains("hsql")) {
                return new HsqldbDialect();
            } else {
                throw new UnsupportedOperationException("Database with product name " + productName + " not supported");
            }
        } catch (SQLException e) {
            throw new FasterSQLException("Failed to detect SQL dialect from connection metadata", e);
        }
    }
}
