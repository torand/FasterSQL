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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static io.github.torand.fastersql.dialect.Capability.CONCAT_OPERATOR;
import static io.github.torand.fastersql.dialect.Capability.LIMIT_OFFSET;
import static io.github.torand.fastersql.util.lang.StringHelper.generate;

public class H2Dialect implements Dialect {
    private static final EnumSet<Capability> SUPPORTED_CAPS = EnumSet.of(LIMIT_OFFSET, CONCAT_OPERATOR);

    @Override
    public String getProductName() {
        return "H2";
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
        return "substring(" + operand + ", " + startPos + ", " + length + ")";
    }

    @Override
    public String formatConcatFunction(List<String> operands) {
        throw new UnsupportedOperationException("Use the concat infix operator for H2");
    }

    @Override
    public String formatLengthFunction(String operand) {
        return "char_length(" + operand + ")";
    }

    @Override
    public boolean supports(Capability capability) {
        return SUPPORTED_CAPS.contains(capability);
    }

    public static void setupCustomizations(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("CREATE ALIAS IF NOT EXISTS TO_NUMBER FOR \"io.github.torand.fastersql.dialect.H2CustomFunctions.toNumber\"")) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to setup H2 customizations", e);
        }
    }
}
