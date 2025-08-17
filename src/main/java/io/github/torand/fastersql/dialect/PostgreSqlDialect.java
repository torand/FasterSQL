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
import static io.github.torand.fastersql.dialect.Capability.SELECT_FOR_UPDATE;
import static io.github.torand.fastersql.dialect.Capability.SET_OPERATION_PARENTHESES;
import static io.github.torand.fastersql.dialect.Capability.TRUNCATE_TABLE;
import static io.github.torand.javacommons.lang.StringHelper.generate;

/**
 * Defines the <a href="https://www.postgresql.org/docs/current/">PostgreSQL</a> SQL dialect
 */
public class PostgreSqlDialect implements Dialect {
    private static final EnumSet<Capability> SUPPORTED_CAPS = EnumSet.of(LIMIT_OFFSET, CONCAT_OPERATOR, MODULO_OPERATOR, CURRENT_TIME, NULL_ORDERING, SELECT_FOR_UPDATE, TRUNCATE_TABLE, FULL_OUTER_JOIN, SET_OPERATION_PARENTHESES);

    /**
     * Creates a PostgreSQL {@link Dialect} implementation.
     */
    public PostgreSqlDialect() {
        // Default constructor required by Javadoc
    }

    @Override
    public String getProductName() {
        return "PostgreSQL";
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
    public String formatToNumberFunction(String operand, int precision, int scale) {
        StringBuilder mask = new StringBuilder();
        if (precision-scale > 0) {
            mask.append(generate("9", precision-scale));
        }
        if (scale > 0) {
            mask.append(".").append(generate("9", scale));
        }

        return "to_number(" + operand + ", '" + mask + "')";
    }

    @Override
    public String formatConcatFunction(List<String> operands) {
        throw new UnsupportedOperationException("PostgreSQL does not support the concat() function (use the concat infix operator instead)");
    }

    @Override
    public String formatLengthFunction(String operand) {
        return "char_length(" + operand + ")";
    }

    @Override
    public String formatModuloFunction(String divisor, String dividend) {
        throw new UnsupportedOperationException("PostgreSQL does not support the mod() function (use the modulo infix operator instead)");
    }

    @Override
    public Optional<String> getDataType(DataType dataType) {
        // https://www.postgresql.org/docs/current/datatype.html
        return Optional.ofNullable(switch(dataType.getIsoDataType()) {
            case BOOLEAN -> "boolean";
            case CHAR -> "char";
            case VARCHAR -> "varchar";
            case BIT -> "bit";
            case BIT_VARYING -> "bit varying";
            case NUMERIC -> "numeric";
            case DECIMAL -> "decimal";
            case INTEGER -> "integer";
            case SMALLINT -> "smallint";
            case FLOAT -> null;
            case DOUBLE_PRECISION -> "double precision";
            case REAL -> "real";
            case TIME -> "time";
            case DATE -> "date";
            case INTERVAL -> "interval";
            case CHARACTER_LARGE_OBJECT -> null;
            case BINARY_LARGE_OBJECT -> null;
        });
    }

    @Override
    public boolean supports(Capability capability) {
        return SUPPORTED_CAPS.contains(capability);
    }
}
