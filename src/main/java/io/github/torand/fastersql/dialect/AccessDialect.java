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
import static io.github.torand.fastersql.dialect.Capability.FULL_OUTER_JOIN;
import static io.github.torand.fastersql.dialect.Capability.LIMIT_OFFSET;
import static io.github.torand.fastersql.dialect.Capability.POWER_OPERATOR;
import static io.github.torand.fastersql.dialect.Capability.SELECT_FOR_UPDATE;
import static io.github.torand.fastersql.dialect.Capability.TRUNCATE_TABLE;

/**
 * Defines the <a href="https://support.microsoft.com/en-us/office/Queries-93fb69b7-cfc1-4f3e-ab56-b0a01523bb50#ID0EAABAAA=SQL_syntax&id0ebbd=sql_syntax">Microsoft Access</a> SQL dialect.
 */
public class AccessDialect implements Dialect {
    private final EnumSet<Capability> supportedCaps;

    /**
     * Creates a Microsoft Access {@link Dialect} implementation.
     */
    public AccessDialect() {
        this(EnumSet.of(LIMIT_OFFSET, SELECT_FOR_UPDATE, CONCAT_OPERATOR, POWER_OPERATOR, TRUNCATE_TABLE, FULL_OUTER_JOIN));
    }

    private AccessDialect(EnumSet<Capability> capabilities) {
        this.supportedCaps = EnumSet.copyOf(capabilities);
    }

    @Override
    public String getProductName() {
        return "Access";
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
        return "val(" + operand + ")";
    }

    @Override
    public String formatToCharFunction(String operand, String format) {
        throw new UnsupportedOperationException("Access does not support the to_char() function");
    }

    @Override
    public String formatSubstringFunction(String operand, int startPos, int length) {
        return "substring(" + operand + ", " + startPos + ", " + length + ")";
    }

    @Override
    public String formatConcatFunction(List<String> operands) {
        throw new UnsupportedOperationException("Access does not support the concat() function (use the concat infix operator instead)");
    }

    @Override
    public String formatLengthFunction(String operand) {
        return "len(" + operand + ")";
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
        throw new UnsupportedOperationException("Access does not support the power() function (use the power infix operator instead)");
    }

    @Override
    public String formatRoundFunction(String operand) {
        return "round(" + operand + ", 0)";
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
        return Optional.of("&");
    }

    @Override
    public boolean supports(Capability capability) {
        return supportedCaps.contains(capability);
    }
}
