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
package io.github.torand.fastersql.dialect;

import io.github.torand.fastersql.function.singlerow.cast.DataType;
import io.github.torand.fastersql.statement.FasterSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static io.github.torand.fastersql.dialect.Capability.CONCAT_OPERATOR;
import static io.github.torand.fastersql.dialect.Capability.CURRENT_TIME;
import static io.github.torand.fastersql.dialect.Capability.LIMIT_OFFSET;
import static io.github.torand.fastersql.dialect.Capability.MODULO_OPERATOR;
import static io.github.torand.fastersql.dialect.Capability.NULL_ORDERING;
import static io.github.torand.fastersql.dialect.Capability.SELECT_FOR_UPDATE;
import static io.github.torand.fastersql.dialect.Capability.SET_OPERATION_PARENTHESES;
import static io.github.torand.fastersql.dialect.Capability.TRUNCATE_TABLE;

/**
 * Defines the <a href="https://www.h2database.com/html/grammar.html">H2</a> SQL dialect.
 */
public class H2Dialect implements Dialect {
    private static final EnumSet<Capability> SUPPORTED_CAPS = EnumSet.of(LIMIT_OFFSET, CONCAT_OPERATOR, MODULO_OPERATOR, CURRENT_TIME, NULL_ORDERING, SELECT_FOR_UPDATE, TRUNCATE_TABLE, SET_OPERATION_PARENTHESES);

    /**
     * Creates an H2 {@link Dialect} implementation.
     */
    public H2Dialect() {
        // Default constructor required by Javadoc
    }

    @Override
    public String getProductName() {
        return "H2";
    }

    @Override
    public boolean offsetBeforeLimit() {
        return false;
    }

    @Override
    public Optional<String> formatRowOffsetClause() {
        return Optional.of("offset ?");
    }

    @Override
    public Optional<String> formatRowLimitClause() {
        return Optional.of("limit ?");
    }

    @Override
    public Optional<String> formatRowNumLiteral() {
        return Optional.of("rownum()");
    }

    @Override
    public String formatConcatFunction(List<String> operands) {
        throw new UnsupportedOperationException("H2 does not support the concat() function (use the concat infix operator instead)");
    }

    @Override
    public String formatLengthFunction(String operand) {
        return "char_length(" + operand + ")";
    }

    @Override
    public String formatCeilFunction(String operand) {
        return "ceiling(" + operand + ")";
    }

    @Override
    public String formatModuloFunction(String divisor, String dividend) {
        throw new UnsupportedOperationException("H2 does not support the mod() function (use the modulo infix operator instead)");
    }

    @Override
    public Optional<String> getDataType(DataType dataType) {
        // https://www.h2database.com/html/datatypes.html
        return Optional.ofNullable(switch(dataType.getIsoDataType()) {
            case BOOLEAN -> "boolean";
            case CHAR -> "char";
            case VARCHAR -> "varchar";
            case BIT, BIT_VARYING -> null;
            case NUMERIC -> "numeric";
            case DECIMAL -> "decimal";
            case INTEGER -> "integer";
            case SMALLINT -> "smallint";
            case FLOAT, DOUBLE_PRECISION -> "float";
            case REAL -> "real";
            case DATE -> "date";
            case TIME -> "time";
            case INTERVAL -> "interval";
            case CHARACTER_LARGE_OBJECT -> "clob";
            case BINARY_LARGE_OBJECT -> "blob";
        });
    }

    @Override
    public boolean supports(Capability capability) {
        return SUPPORTED_CAPS.contains(capability);
    }

    /**
     * Adds some user defined functions emulating common SQL constructs not supported by default.
     * @param connection a live H2 connection.
     * @return the modified dialect.
     */
    public H2Dialect withCustomizations(Connection connection) {
        String source = """
            import java.lang.*;
            @CODE
            Double toNumber(String value) throws Exception {
                return value == null ? null : Double.valueOf(value);
            }
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement("CREATE ALIAS IF NOT EXISTS TO_NUMBER AS '%s'".formatted(source))) {
            preparedStatement.execute();
            return this;
        } catch (SQLException e) {
            throw new FasterSQLException("Failed to setup H2 customizations", e);
        }
    }
}
