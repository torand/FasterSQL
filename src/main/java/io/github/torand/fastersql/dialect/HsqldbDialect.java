/*
 * Copyright (c) 2024-2026 Tore Eide Andersen
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
import java.util.Optional;

import static io.github.torand.fastersql.dialect.Capability.*;

/**
 * Defines the <a href="https://hsqldb.org/doc/2.0/guide/sqlgeneral-chapt.html">HyperSQL</a> (HSQLDB) SQL dialect.
 */
public class HsqldbDialect implements Dialect {
    private static final EnumSet<Capability> SUPPORTED_CAPS = EnumSet.of(LIMIT_OFFSET, CONCAT_OPERATOR, CURRENT_TIME, NULL_ORDERING, SELECT_FOR_UPDATE, TRUNCATE_TABLE, FULL_OUTER_JOIN, SET_OPERATION_PARENTHESES);

    /**
     * Creates a HyperSQL (HSQLDB) {@link Dialect} implementation.
     */
    public HsqldbDialect() {
        // Default constructor required by Javadoc
    }

    @Override
    public String getProductName() {
        return "HyperSQL/HSQLDB";
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
    public String formatSubstringFunction(String operand, int startPos, int length) {
        return "substr(%s, %d, %d)".formatted(operand, startPos, length);
    }

    @Override
    public String formatLengthFunction(String operand) {
        return "char_length(" + operand + ")";
    }

    @Override
    public Optional<String> getDataType(DataType dataType) {
        // https://hsqldb.org/doc/guide/sqlgeneral-chapt.html#sgc_data_type_guide
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
            case FLOAT, DOUBLE_PRECISION -> "float";
            case REAL -> null;
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
}
