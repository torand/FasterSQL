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

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static io.github.torand.fastersql.dialect.Capability.CONCAT_OPERATOR;
import static io.github.torand.fastersql.dialect.Capability.CURRENT_TIME;
import static io.github.torand.fastersql.dialect.Capability.FULL_OUTER_JOIN;
import static io.github.torand.fastersql.dialect.Capability.LIMIT_OFFSET;
import static io.github.torand.fastersql.dialect.Capability.NULL_ORDERING;
import static io.github.torand.fastersql.dialect.Capability.SELECT_FOR_UPDATE;
import static io.github.torand.fastersql.dialect.Capability.SET_OPERATION_PARENTHESES;
import static io.github.torand.fastersql.dialect.Capability.TRUNCATE_TABLE;

/**
 * Defines the <a href="https://hsqldb.org/doc/2.0/guide/sqlgeneral-chapt.html">HyperSQL</a> (HSQLDB) SQL dialect.
 */
public class HsqldbDialect implements Dialect {
    private static final EnumSet<Capability> SUPPORTED_CAPS = EnumSet.of(LIMIT_OFFSET, CONCAT_OPERATOR, CURRENT_TIME, NULL_ORDERING, SELECT_FOR_UPDATE, TRUNCATE_TABLE, FULL_OUTER_JOIN, SET_OPERATION_PARENTHESES);

    /**
     * Creates a HyperSQL (HSQLDB) {@link Dialect} implementation.
     */
    public HsqldbDialect() {}

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
    public String formatToNumberFunction(String operand, int precision, int scale) {
        return "to_number(" + operand + ")";
    }

    @Override
    public String formatToCharFunction(String operand, String format) {
        return "to_char(" + operand + ", " + format + ")";
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
    public String formatPowerFunction(String base, String exponent) {
        return "power(cast(%s as decimal), cast(%s as decimal))".formatted(base, exponent);
    }

    @Override
    public String formatRoundFunction(String operand) {
        return "round(" + operand + ")";
    }

    @Override
    public String formatModuloFunction(String divisor, String dividend) {
        return "mod(" + divisor + ", " + dividend + ")";
    }

    @Override
    public String formatCurrentDateFunction() {
        return "current_date";
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
