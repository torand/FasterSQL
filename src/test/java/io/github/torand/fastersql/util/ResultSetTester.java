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
package io.github.torand.fastersql.util;

import io.github.torand.javacommons.collection.CollectionHelper;
import org.hamcrest.Matcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class ResultSetTester {
    private final Map<Integer, List<RowValueMatcher<?>>> rowValueMatcherMap = new HashMap<>();
    private Integer expectedRowCount;

    public static ResultSetTester resultSetTester() {
        return new ResultSetTester();
    }

    public ResultSetTester assertRowCount(int expectedRowCount) {
        this.expectedRowCount = expectedRowCount;
        return this;
    }

    public ResultSetTester assertRow(int rowNo, String columnLabel, Matcher<?> matcher, Object... additionalColumnMatcherPairs) {
        List<RowValueMatcher<?>> rowValueMatchers = new ArrayList<>();
        rowValueMatchers.add(new RowValueMatcher(columnLabel, matcher));

        if (nonNull(additionalColumnMatcherPairs)) {
            if (additionalColumnMatcherPairs.length % 2 != 0) {
                throw new IllegalArgumentException("additionalColumnMatcherPairs should contain pairs of column labels and matchers");
            }
            for (int i = 0; i < additionalColumnMatcherPairs.length; i += 2) {
                if (additionalColumnMatcherPairs[i] instanceof String additionalColumnLabel
                    && additionalColumnMatcherPairs[i+1] instanceof Matcher additionalMatcher) {
                        rowValueMatchers.add(new RowValueMatcher(additionalColumnLabel, additionalMatcher));
                    }
            }
        }

        this.rowValueMatcherMap.merge(rowNo, rowValueMatchers, CollectionHelper::concat);

        return this;
    }

    public void log(ResultSet rs) throws SQLException {
        List<String> rows = new ArrayList<>();
        while (rs.next()) {
            int rowNo = rs.getRow();
            int colCount = rs.getMetaData().getColumnCount();

            List<String> rowValues = new ArrayList<>();
            for (int colNo = 1; colNo <= colCount; colNo++) {
                String colLabel = rs.getMetaData().getColumnLabel(colNo);
                Object value = rs.getObject(colNo);
                rowValues.add(colLabel + ":" + value + " (" + getValueType(value) + ")");
            }

            rows.add("Row " + rowNo + ": " + String.join(", ", rowValues));
        }

        if (rows.isEmpty()) {
            System.out.println("No rows found");
        } else {
            System.out.println("Found " + rows.size() + " rows:");
            rows.forEach(System.out::println);
        }
    }

    public void verify(ResultSet rs) throws SQLException {
        int actualRowCount = 0;

        while (rs.next()) {
            List<RowValueMatcher<?>> matchers = rowValueMatcherMap.get(rs.getRow());
            if (nonNull(matchers)) {
                String mismatches = matchers.stream()
                    .map(matcher -> matcher.test(rs))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(msg -> "   " + msg)
                    .collect(joining("\n"));

                if (!mismatches.isEmpty()) {
                    fail("Values not matching for row number " + rs.getRow() + ":\n" + mismatches);
                }
            }

            actualRowCount++;
        }

        if (nonNull(expectedRowCount)) {
            assertThat(actualRowCount).describedAs("Row count").isEqualTo(expectedRowCount);
        }
    }

    private String getValueType(Object value) {
        if (isNull(value)) {
            return "?";
        }
        String fullTypeName = value.getClass().getName();
        return fullTypeName.substring(fullTypeName.lastIndexOf('.') + 1);
    }
}
