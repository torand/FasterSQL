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

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static io.github.torand.fastersql.dialect.Capability.CONCAT_OPERATOR;
import static io.github.torand.fastersql.dialect.Capability.CURRENT_TIME;
import static io.github.torand.fastersql.dialect.Capability.FULL_OUTER_JOIN;
import static io.github.torand.fastersql.dialect.Capability.LIMIT_OFFSET;
import static io.github.torand.fastersql.dialect.Capability.MODULO_OPERATOR;
import static io.github.torand.fastersql.dialect.Capability.NULL_ORDERING;

/**
 * Defines the <a href="https://www.sqlite.org/lang.html">SQLite</a> SQL dialect.
 */
public class SqliteDialect implements Dialect {
    private static final EnumSet<Capability> SUPPORTED_CAPS = EnumSet.of(LIMIT_OFFSET, CONCAT_OPERATOR, MODULO_OPERATOR, CURRENT_TIME, NULL_ORDERING, FULL_OUTER_JOIN);

    /**
     * Creates a SQLite {@link Dialect} implementation.
     */
    public SqliteDialect() {}

    @Override
    public String getProductName() {
        return "SQLite";
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
        return Optional.of("row_number()");
    }

    @Override
    public String formatToNumberFunction(String operand, int precision, int scale) {
        return "cast(" + operand + " as decimal)";
    }

    @Override
    public String formatToCharFunction(String operand, String format) {
        throw new UnsupportedOperationException("SQLite does not support the to_char() function (timestamps are already stored as ISO8601 strings)");
    }

    @Override
    public String formatSubstringFunction(String operand, int startPos, int length) {
        return "substr(" + operand + ", " + startPos + ", " + length + ")";
    }

    @Override
    public String formatConcatFunction(List<String> operands) {
        // Note! The concat infix operator is used in output SQL
        return "concat(%s)".formatted(String.join(", ", operands));
    }

    @Override
    public String formatLengthFunction(String operand) {
        return "length(" + operand + ")";
    }

    @Override
    public String formatCeilFunction(String operand) {
        return "ceil(" + operand + ")";
    }

    @Override
    public String formatLnFunction(String operand) {
        return "ln(" + operand + ")";
    }

    @Override
    public String formatRoundFunction(String operand) {
        return "round(" + operand + ")";
    }

    @Override
    public String formatModuloFunction(String divisor, String dividend) {
        // Note! The modulo infix operator is used in output SQL
        return "mod(" + divisor + ", " + dividend + ")";
    }

    @Override
    public String formatCurrentDateFunction() {
        return "current_date";
    }

    @Override
    public Optional<String> getDataType(DataType dataType) {
        // https://www.sqlite.org/datatype3.html
        return Optional.ofNullable(switch(dataType.getIsoDataType()) {
            case BOOLEAN -> "boolean";
            case CHAR -> "char";
            case VARCHAR -> "varchar";
            case BIT -> null;
            case BIT_VARYING -> null;
            case NUMERIC -> "numeric";
            case DECIMAL -> "decimal";
            case INTEGER -> "integer";
            case SMALLINT -> "smallint";
            case FLOAT -> "float";
            case DOUBLE_PRECISION -> "double precision";
            case REAL -> "real";
            case TIME -> null;
            case DATE -> "date";
            case INTERVAL -> null;
            case CHARACTER_LARGE_OBJECT -> "clob";
            case BINARY_LARGE_OBJECT -> "blob";
        });
    }

    @Override
    public Optional<String> getConcatOperator() {
        return Optional.of("||");
    }

    @Override
    public boolean supports(Capability capability) {
        return SUPPORTED_CAPS.contains(capability);
    }
}
