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
import static io.github.torand.fastersql.dialect.Capability.MODULO_OPERATOR;
import static io.github.torand.fastersql.dialect.Capability.TRUNCATE_TABLE;

/**
 * Defines the <a href="https://learn.microsoft.com/en-us/sql/sql-server/?view=sql-server-ver16">Microsoft SQL Server</a> SQL dialect.
 */
public class SqlServerDialect implements Dialect {
    private final EnumSet<Capability> supportedCaps;

    /**
     * Creates a Microsoft SQL Server {@link Dialect} implementation.
     */
    public SqlServerDialect() {
        this(EnumSet.of(LIMIT_OFFSET, CONCAT_OPERATOR, MODULO_OPERATOR, TRUNCATE_TABLE, FULL_OUTER_JOIN));
    }

    private SqlServerDialect(EnumSet<Capability> capabilities) {
        this.supportedCaps = EnumSet.copyOf(capabilities);
    }

    @Override
    public String getProductName() {
        return "SQL Server";
    }

    @Override
    public boolean offsetBeforeLimit() {
        return true;
    }

    /**
     * Row offset clause is supported from SQL Server 2012 onwards
     */
    @Override
    public Optional<String> formatRowOffsetClause() {
        return Optional.of("offset ? rows");
    }

    /**
     * Row limit clause is supported from SQL Server 2012 onwards
     */
    @Override
    public Optional<String> formatRowLimitClause() {
        return Optional.of("fetch next ? rows only");
    }

    /**
     * SQL Server has the ROW_NUMBER() function but must be combined with OVER() in this context.
     */
    @Override
    public Optional<String> formatRowNumLiteral() {
        return Optional.empty();
    }

    @Override
    public String formatToNumberFunction(String operand, int precision, int scale) {
        return "cast(" + operand + " as numeric(" + precision + "," + scale + "))";
    }

    @Override
    public String formatToCharFunction(String operand, String format) {
        throw new UnsupportedOperationException("SQL Server does not support the to_char() function");
    }

    @Override
    public String formatSubstringFunction(String operand, int startPos, int length) {
        return "substring(" + operand + ", " + startPos + ", " + length + ")";
    }

    @Override
    public String formatConcatFunction(List<String> operands) {
        throw new UnsupportedOperationException("SQL Server does not support the concat() function (use the concat infix operator instead)");
    }

    @Override
    public String formatLengthFunction(String operand) {
        return "len(" + operand + ")";
    }

    @Override
    public String formatCeilFunction(String operand) {
        return "ceiling(" + operand + ")";
    }

    @Override
    public String formatLnFunction(String operand) {
        return "log(" + operand + ")";
    }

    @Override
    public String formatRoundFunction(String operand) {
        return "round(" + operand + ", 0)";
    }

    @Override
    public String formatModuloFunction(String divisor, String dividend) {
        throw new UnsupportedOperationException("SQL Server does not support the mod() function (use the modulo infix operator instead)");
    }

    @Override
    public String formatCurrentDateFunction() {
        return "getdate()";
    }

    @Override
    public Optional<String> getConcatOperator() {
        return Optional.of("+");
    }

    @Override
    public boolean supports(Capability capability) {
        return supportedCaps.contains(capability);
    }
}
