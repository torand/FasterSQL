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

import static io.github.torand.fastersql.dialect.Capability.CURRENT_TIME;
import static io.github.torand.fastersql.dialect.Capability.LIMIT_OFFSET;
import static io.github.torand.fastersql.dialect.Capability.MODULO_OPERATOR;
import static io.github.torand.fastersql.dialect.Capability.SELECT_FOR_UPDATE;
import static io.github.torand.fastersql.dialect.Capability.SET_OPERATION_PARENTHESES;
import static io.github.torand.fastersql.dialect.Capability.TRUNCATE_TABLE;

/**
 * Defines the <a href="https://dev.mysql.com/doc/refman/8.4/en/">MySQL</a> SQL dialect.
 */
public class MySqlDialect implements Dialect {
    private static final EnumSet<Capability> SUPPORTED_CAPS = EnumSet.of(LIMIT_OFFSET, CURRENT_TIME, MODULO_OPERATOR, SELECT_FOR_UPDATE, TRUNCATE_TABLE, SET_OPERATION_PARENTHESES);

    /**
     * Creates a MySQL {@link Dialect} implementation.
     */
    public MySqlDialect() {
        // Default constructor required by Javadoc
    }

    @Override
    public String getProductName() {
        return "MySQL";
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
        return Optional.empty();
    }

    @Override
    public String formatToNumberFunction(String operand, int precision, int scale) {
        return "cast(" + operand + " as decimal(" + precision + "," + scale + "))";
    }

    @Override
    public String formatToCharFunction(String operand, String format) {
        throw new UnsupportedOperationException("MySQL does not support the to_char() function");
    }

    @Override
    public String formatSubstringFunction(String operand, int startPos, int length) {
        return "substring(" + operand + ", " + startPos + ", " + length + ")";
    }

    @Override
    public String formatConcatFunction(List<String> operands) {
        return "concat(%s)".formatted(String.join(", ", operands));
    }

    @Override
    public String formatLengthFunction(String operand) {
        return "char_length(" + operand + ")";
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
        throw new UnsupportedOperationException("MySQL does not support the mod() function (use the modulo infix operator instead)");
    }

    @Override
    public String formatCurrentDateFunction() {
        return "current_date";
    }

    @Override
    public Optional<String> getDataType(DataType dataType) {
        // https://dev.mysql.com/doc/refman/8.4/en/data-types.html
        return Optional.ofNullable(switch(dataType.getIsoDataType()) {
            case BOOLEAN -> "bool";
            case CHAR -> "char";
            case VARCHAR -> "varchar";
            case BIT -> "bit";
            case BIT_VARYING -> null;
            case NUMERIC -> "numeric";
            case DECIMAL -> "decimal";
            case INTEGER -> "integer";
            case SMALLINT -> "smallint";
            case FLOAT -> "float";
            case DOUBLE_PRECISION -> "double precision";
            case REAL -> "real";
            case DATE -> "date";
            case TIME -> "time";
            case INTERVAL -> null;
            case CHARACTER_LARGE_OBJECT -> "text";
            case BINARY_LARGE_OBJECT -> "blob";
        });
    }

    @Override
    public Optional<String> getConcatOperator() {
        return Optional.empty();
    }

    @Override
    public boolean supports(Capability capability) {
        return SUPPORTED_CAPS.contains(capability);
    }
}
