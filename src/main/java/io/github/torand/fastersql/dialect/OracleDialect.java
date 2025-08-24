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
import io.github.torand.fastersql.setoperation.SetOperator;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static io.github.torand.fastersql.dialect.Capability.CONCAT_OPERATOR;
import static io.github.torand.fastersql.dialect.Capability.FULL_OUTER_JOIN;
import static io.github.torand.fastersql.dialect.Capability.LIMIT_OFFSET;
import static io.github.torand.fastersql.dialect.Capability.NULL_ORDERING;
import static io.github.torand.fastersql.dialect.Capability.SELECT_FOR_UPDATE;
import static io.github.torand.fastersql.dialect.Capability.SET_OPERATION_PARENTHESES;
import static io.github.torand.fastersql.dialect.Capability.TRUNCATE_TABLE;
import static io.github.torand.javacommons.lang.StringHelper.generate;

/**
 * Defines the <a href="https://docs.oracle.com/en/database/oracle/oracle-database/21/sqlrf/">Oracle</a> SQL dialect.
 * <p>
 * Row offset clause is supported from Oracle 12c onwards
 * Row limit clause is supported from Oracle 12c onwards
 * </p>
 */
public class OracleDialect implements Dialect {
    private final EnumSet<Capability> supportedCaps;

    /**
     * Creates an Oracle {@link Dialect} implementation.
     */
    public OracleDialect() {
        this(EnumSet.of(LIMIT_OFFSET, CONCAT_OPERATOR, NULL_ORDERING, SELECT_FOR_UPDATE, TRUNCATE_TABLE, FULL_OUTER_JOIN, SET_OPERATION_PARENTHESES));
    }

    private OracleDialect(EnumSet<Capability> capabilities) {
        this.supportedCaps = EnumSet.copyOf(capabilities);
    }

    @Override
    public String getProductName() {
        return "Oracle";
    }

    @Override
    public Optional<String> formatRowNumLiteral() {
        return Optional.of("rownum");
    }

    @Override
    public String formatSetOperator(SetOperator setOperator) {
        return setOperator == SetOperator.EXCEPT ? "minus" : setOperator.sql();
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
        return "substr(%s, %d, %d)".formatted(operand, startPos, length);
    }

    @Override
    public String formatConcatFunction(List<String> operands) {
        throw new UnsupportedOperationException("Oracle does not support the concat() function (use the concat infix operator instead)");
    }

    @Override
    public Optional<String> getDataType(DataType dataType) {
        // https://docs.oracle.com/en/database/oracle/oracle-database/19/sqlrf/Data-Types.html
        return Optional.ofNullable(switch(dataType.getIsoDataType()) {
            case BOOLEAN -> null;
            case CHAR -> "char";
            case VARCHAR -> "varchar2";
            case BIT -> null;
            case BIT_VARYING -> null;
            case NUMERIC, DECIMAL, INTEGER, SMALLINT -> "number";
            case FLOAT, DOUBLE_PRECISION, REAL -> "float";
            case DATE, TIME -> "date";
            case INTERVAL -> null;
            case CHARACTER_LARGE_OBJECT -> "clob";
            case BINARY_LARGE_OBJECT -> "blob";
        });
    }

    @Override
    public boolean supports(Capability capability) {
        return supportedCaps.contains(capability);
    }

    /**
     * Enables emulating OFFSET and LIMIT constructs for older Oracle versions.
     * Row offset and limit clauses are supported from Oracle 12c onwards.
     * Invoke this method when using previous versions of Oracle, to simulate these clauses with subqueries.
     * @return the modified dialect.
     */
    public OracleDialect withLegacyRowLimiting() {
        EnumSet<Capability> reducedCaps = EnumSet.copyOf(supportedCaps);
        reducedCaps.remove(LIMIT_OFFSET);
        return new OracleDialect(reducedCaps);
    }
}
