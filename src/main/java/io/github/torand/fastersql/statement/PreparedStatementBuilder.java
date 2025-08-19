/*
 * Copyright (c) 2024-2025 Tore Eide Andersen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.torand.fastersql.statement;

import io.github.torand.fastersql.dialect.Dialect;
import io.github.torand.fastersql.dialect.DialectResolver;
import io.github.torand.fastersql.sql.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URI;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Builder for creating PreparedStatement with a Connection.
 */
public class PreparedStatementBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(PreparedStatementBuilder.class);
    private final Connection connection;
    private final Dialect dialect;

    /**
     * Creates builder for statements to be executed on specified connection.
     * The SQL dialect is selected based on metadata from connection.
     * @param connection the connection.
     * @return the statement builder
     */
    public static PreparedStatementBuilder using(Connection connection) {
        return new PreparedStatementBuilder(connection, DialectResolver.fromConnection(connection));
    }

    /**
     * Creates builder for statements to be executed on specified connection,
     * using specified SQL dialect.
     * @param connection the connection.
     * @param dialect the SQL dialect.
     * @return the statement builder.
     */
    public static PreparedStatementBuilder using(Connection connection, Dialect dialect) {
        return new PreparedStatementBuilder(connection, dialect);
    }

    private PreparedStatementBuilder(Connection connection, Dialect dialect) {
        this.connection = connection;
        this.dialect = dialect;
    }

    /**
     * Creates JDBC prepared statement from the specified preparable statement,
     * ready for execution.
     * @param statement the preparable statement.
     * @return the prepared statement.
     * @throws SQLException if database access error occurs or statement parameter assignment fails.
     */
    public PreparedStatement prepare(PreparableStatement statement) throws SQLException {
        LOGGER.debug("Preparing SQL statement (ANSI/ISO SQL): {}", statement);

        Context context = Context.of(dialect);

        String sql = statement.sql(context);
        LOGGER.debug("Generated {} SQL statement: {}", context.getDialect().getProductName(), sql);
        List<Object> params = statement.params(context).toList();

        PreparedStatement stmt = connection.prepareStatement(sql);
        int i = 1;
        for (Object param : params) {
            if (param instanceof Instant instant) {
                stmt.setTimestamp(i, Timestamp.from(instant));
            } else if (param instanceof LocalDateTime localDateTime) {
                stmt.setTimestamp(i, Timestamp.valueOf(localDateTime));
            } else if (param instanceof LocalDate localDate) {
                stmt.setDate(i, Date.valueOf(localDate));
            } else if (param instanceof OffsetDateTime offsetDateTime) {
                stmt.setTimestamp(i, Timestamp.valueOf(offsetDateTime.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()));
            } else if (param instanceof ZonedDateTime zonedDateTime) {
                stmt.setTimestamp(i, Timestamp.valueOf(zonedDateTime.toLocalDateTime()));
            } else if (param instanceof UUID uuid) {
                stmt.setObject(i, uuid.toString());
            } else if (param instanceof URI uri) {
                stmt.setObject(i, uri.toString());
            } else if (param instanceof Enum<?> enumValue) {
                stmt.setObject(i, enumValue.name());
            } else if (param instanceof InputStream inputStream) {
                stmt.setBinaryStream(i, inputStream);
            } else {
                stmt.setObject(i, param);
            }

            i++;
        }

        return stmt;
    }
}
