package org.github.torand.fastersql.statement;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.dialect.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URI;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Builder for creating PreparedStatement with a Connection.
 */
public class PreparedStatementBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(PreparedStatementBuilder.class);
    private final Connection connection;

    public static PreparedStatementBuilder using(Connection connection) {
        return new PreparedStatementBuilder(connection);
    }

    private PreparedStatementBuilder(Connection connection) {
        this.connection = connection;
    }

    public PreparedStatement prepare(PreparableStatement statement) throws SQLException {
        LOGGER.debug("Preparing SQL statement: {}", statement);

        Context context = Context.of(Dialect.fromConnection(connection));
        String sql = statement.sql(context);
        List<Object> params = statement.params(context);

        PreparedStatement stmt = connection.prepareStatement(sql);
        int i = 1;
        for (Object param : params) {
            if (param instanceof Instant) {
                stmt.setTimestamp(i, Timestamp.from((Instant)param));
            } else if (param instanceof LocalDateTime) {
                stmt.setTimestamp(i, Timestamp.valueOf((LocalDateTime)param));
            } else if (param instanceof LocalDate) {
                stmt.setDate(i, Date.valueOf((LocalDate)param));
            } else if (param instanceof UUID) {
                stmt.setObject(i, param.toString());
            } else if (param instanceof URI) {
                stmt.setObject(i, param.toString());
            } else if (param instanceof Enum) {
                stmt.setObject(i, ((Enum)param).name());
            } else if (param instanceof InputStream) {
                stmt.setBinaryStream(i, (InputStream)param);
            } else {
                stmt.setObject(i, param);
            }

            i++;
        }

        return stmt;
    }
}
