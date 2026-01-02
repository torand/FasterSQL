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
 * Defines the <a href="https://standards.iso.org/iso-iec/9075/-2/ed-6/en/ISO_IEC_9075-2(E)_Foundation.bnf.txt">ANSI/ISO</a> (ISO/IEC 9075) SQL dialect.
 */
public class AnsiIsoDialect implements Dialect {
    private static final EnumSet<Capability> SUPPORTED_CAPS = EnumSet.of(LIMIT_OFFSET, MODULO_OPERATOR, SELECT_FOR_UPDATE, TRUNCATE_TABLE, FULL_OUTER_JOIN, SET_OPERATION_PARENTHESES);

    /**
     * Creates an ANSI/ISO {@link Dialect} implementation.
     */
    public AnsiIsoDialect() {
        // Default constructor required by Javadoc
    }

    @Override
    public String getProductName() {
        return "ANSI/ISO";
    }

    @Override
    public String formatModuloFunction(String divisor, String dividend) {
        throw new UnsupportedOperationException("ANSI/ISO SQL does not support the mod() function (use the modulo infix operator instead)");
    }

    @Override
    public Optional<String> getDataType(DataType dataType) {
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
            case FLOAT -> "float";
            case DOUBLE_PRECISION -> "double precision";
            case REAL -> "real";
            case TIME -> "time";
            case DATE -> "date";
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
