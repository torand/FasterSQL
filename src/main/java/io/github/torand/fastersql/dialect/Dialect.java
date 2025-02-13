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
import java.sql.SQLException;
import java.util.Optional;

public interface Dialect {
    /**
     * Gets the name of the RDBMS product.
     * @return the name of the RDBMS product.
     */
    String getProductName();

    /**
     * Returns the _row offset clause_ formatted for a specific SQL dialect.
     * @return the _row offset clause_ formatted for a specific SQL dialect.
     */
    Optional<String> formatRowOffsetClause();

    /**
     * Returns the _row limit clause_ formatted for a specific SQL dialect.
     * @return the _row limit clause_ formatted for a specific SQL dialect.
     */
    Optional<String> formatRowLimitClause();

    /**
     * Returns the _row number_ literal formatted for a specific SQL dialect.
     * @return the _row number_ literal formatted for a specific SQL dialect.
     */
    Optional<String> formatRowNumLiteral();

    /**
     * Returns the 'to_number' function formatted for a specific SQL dialect.
     * @param operand the string expression to be evaluated as a number
     * @param precision the precision that represents the number of significant digits
     * @param scale the scale that that represents the number of digits after the decimal point. Must be less than or equal to the precision.
     * @return the 'to_number' function for a specific SQL dialect.
     */
    String formatToNumberFunction(String operand, int precision, int scale);

    /**
     * Returns the 'substring' function formatted for a specific SQL dialect.
     * @param operand the string expression to get substring from
     * @param startPos the start position (1-based) of the substring
     * @param length the length of the substring
     * @return the 'substring' function for a specific SQL dialect.
     */
    String formatSubstringFunction(String operand, int startPos, int length);

    /**
     * Indicates whether a capability is supported by a specific SQL dialect.
     * @param capability the capability to check support for
     * @return true if specified capability is supported; else false
     */
    boolean supports(Capability capability);

    static Dialect fromConnection(Connection connection) {
        try {
            String productName = connection.getMetaData().getDatabaseProductName().toLowerCase();

            if (productName.contains("h2")) {
                return new H2Dialect();
            } else if (productName.contains("mysql")) {
                return new MySqlDialect();
            } else if (productName.contains("mariadb")) {
                return new MariaDbDialect();
            } else if (productName.contains("postgresql")) {
                return new PostgreSqlDialect();
            } else if (productName.contains("oracle")) {
                return new OracleDialect();
            } else {
                throw new UnsupportedOperationException("Database with product name " + productName + " not supported");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to detect SQL dialect from connection metadata", e);
        }
    }
}
