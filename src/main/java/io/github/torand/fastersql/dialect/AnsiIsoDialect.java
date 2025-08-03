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

import static io.github.torand.fastersql.dialect.Capability.FULL_OUTER_JOIN;
import static io.github.torand.fastersql.dialect.Capability.LIMIT_OFFSET;
import static io.github.torand.fastersql.dialect.Capability.MODULO_OPERATOR;
import static io.github.torand.fastersql.dialect.Capability.SELECT_FOR_UPDATE;
import static io.github.torand.fastersql.dialect.Capability.SET_OPERATION_PARENTHESES;
import static io.github.torand.fastersql.dialect.Capability.TRUNCATE_TABLE;

/**
 * Defines the <a href="https://standards.iso.org/iso-iec/9075/-2/ed-6/en/ISO_IEC_9075-2(E)_Foundation.bnf.txt">ANSI/ISO</a> (ISO/IEC 9075) SQL dialect.
 */
public class AnsiIsoDialect implements Dialect {
    private static final EnumSet<Capability> SUPPORTED_CAPS = EnumSet.of(LIMIT_OFFSET, MODULO_OPERATOR, SELECT_FOR_UPDATE, TRUNCATE_TABLE, FULL_OUTER_JOIN, SET_OPERATION_PARENTHESES);

    /**
     * Creates an ANSI/ISO {@link Dialect} implementation.
     */
    public AnsiIsoDialect() {}

    @Override
    public String getProductName() {
        return "ANSI/ISO";
    }

    @Override
    public boolean offsetBeforeLimit() {
        return true;
    }

    @Override
    public Optional<String> formatRowOffsetClause() {
        return Optional.of("offset ? rows");
    }

    @Override
    public Optional<String> formatRowLimitClause() {
        return Optional.of("fetch next ? rows only");
    }

    @Override
    public Optional<String> formatRowNumLiteral() {
        return Optional.empty();
    }

    @Override
    public String formatToNumberFunction(String operand, int precision, int scale) {
        return "to_number(%s)".formatted(operand);
    }

    @Override
    public String formatToCharFunction(String operand, String format) {
        return "to_char(%s, %s)".formatted(operand, format);
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
        throw new UnsupportedOperationException("ANSI/ISO SQL does not support the mod() function (use the modulo infix operator instead)");
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
