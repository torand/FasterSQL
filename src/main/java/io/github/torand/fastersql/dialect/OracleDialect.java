/*
 * Copyright (c) 2024 Tore Eide Andersen
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
import static io.github.torand.fastersql.dialect.Capability.LIMIT_OFFSET;
import static io.github.torand.fastersql.util.lang.StringHelper.generate;

public class OracleDialect implements Dialect {
    private final EnumSet<Capability> supportedCaps;

    public OracleDialect() {
        this(EnumSet.of(LIMIT_OFFSET, CONCAT_OPERATOR));
    }

    private OracleDialect(EnumSet<Capability> capabilities) {
        this.supportedCaps = EnumSet.copyOf(capabilities);
    }

    @Override
    public String getProductName() {
        return "Oracle";
    }

    /**
     * Row offset clause is supported from Oracle 12c onwards
     */
    @Override
    public Optional<String> formatRowOffsetClause() {
        return Optional.of("offset ? rows");
    }

    /**
     * Row limit clause is supported from Oracle 12c onwards
     */
    @Override
    public Optional<String> formatRowLimitClause() {
        return Optional.of("fetch next ? rows only");
    }

    @Override
    public Optional<String> formatRowNumLiteral() {
        return Optional.of("rownum");
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
    public String formatSubstringFunction(String operand, int startPos, int length) {
        return "substr(" + operand + ", " + startPos + ", " + length + ")";
    }

    @Override
    public String formatConcatFunction(List<String> operands) {
        throw new UnsupportedOperationException("Use the concat infix operator for Oracle");
    }

    @Override
    public String formatLengthFunction(String operand) {
        return "length(" + operand + ")";
    }

    @Override
    public boolean supports(Capability capability) {
        return supportedCaps.contains(capability);
    }

    /**
     * Row offset and limit clauses are supported from Oracle 12c onwards.
     * Invoke this method when using previous versions of Oracle, to simulate these clauses with subqueries.
     */
    public OracleDialect withLegacyRowLimiting() {
        EnumSet<Capability> reducedCaps = EnumSet.copyOf(supportedCaps);
        reducedCaps.remove(LIMIT_OFFSET);
        return new OracleDialect(reducedCaps);
    }
}
