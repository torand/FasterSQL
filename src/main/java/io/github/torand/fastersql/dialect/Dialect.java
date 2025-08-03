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

import io.github.torand.fastersql.setoperation.SetOperator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Defines an SQL dialect as implemented by a specific database vendor.
 */
public interface Dialect {
    /**
     * Gets the name of the RDBMS product associated with this SQL dialect.
     * @return the name of the RDBMS product.
     */
    String getProductName();

    /**
     * Indicates whether <i>offset</i> clause must be specified before <i>limit</i> clause; if supported.
     * @return whether <i>offset</i> clause must be specified before <i>limit</i> clause; if supported.
     */
    boolean offsetBeforeLimit();

    /**
     * Returns the <i>row offset</i> clause formatted for a specific SQL dialect.
     * @return the <i>row offset</i> clause formatted for a specific SQL dialect.
     */
    Optional<String> formatRowOffsetClause();

    /**
     * Returns the <i>row limit</i> clause formatted for a specific SQL dialect.
     * @return the <i>row limit</i> clause formatted for a specific SQL dialect.
     */
    Optional<String> formatRowLimitClause();

    /**
     * Returns the <i>row number</i> literal formatted for a specific SQL dialect.
     * @return the <i>row number</i> literal formatted for a specific SQL dialect.
     */
    Optional<String> formatRowNumLiteral();

    /**
     * Returns the specified set operator formatted for a specific SQL dialect.
     * @param setOperator the set operator.
     * @return the set operator for a specific SQL dialect.
     */
    default String formatSetOperator(SetOperator setOperator) {
        return setOperator.sql();
    }

    /**
     * Returns the 'to_number' function formatted for a specific SQL dialect.
     * @param operand the string expression to be evaluated as a number
     * @param precision the precision that represents the number of significant digits
     * @param scale the scale that that represents the number of digits after the decimal point. Must be less than or equal to the precision.
     * @return the 'to_number' function for a specific SQL dialect.
     */
    String formatToNumberFunction(String operand, int precision, int scale);

    /**
     * Returns the 'to_char' function formatted for a specific SQL dialect.
     * @param operand the expression to be evaluated as a string
     * @param format the vendor-specific format mask
     * @return the 'to_char' function for a specific SQL dialect.
     */
    String formatToCharFunction(String operand, String format);

    /**
     * Returns the 'substring' function formatted for a specific SQL dialect.
     * @param operand the string expression to get substring from
     * @param startPos the start position (1-based) of the substring
     * @param length the length of the substring
     * @return the 'substring' function for a specific SQL dialect.
     */
    String formatSubstringFunction(String operand, int startPos, int length);

    /**
     * Returns the 'concat' function formatted for a specific SQL dialect.
     * @param operands the string expressions to concatenate
     * @return the 'concat' function for a specific SQL dialect.
     */
    String formatConcatFunction(List<String> operands);

    /**
     * Returns the 'length' function formatted for a specific SQL dialect.
     * @param operand the string expression to get length of
     * @return the 'length' function for a specific SQL dialect.
     */
    String formatLengthFunction(String operand);

    /**
     * Returns the 'ceil' function formatted for a specific SQL dialect.
     * @param operand the numeric expression to get ceiling of
     * @return the 'ceil' function for a specific SQL dialect.
     */
    String formatCeilFunction(String operand);

    /**
     * Returns the 'ln' function formatted for a specific SQL dialect.
     * @param operand the numeric expression to get natural logarithm of
     * @return the 'ln' function for a specific SQL dialect.
     */
    String formatLnFunction(String operand);

    /**
     * Returns the 'pow' function formatted for a specific SQL dialect.
     * @param base the numeric expression for base operand
     * @param exponent the numeric expression for exponent operand
     * @return the 'pow' function for a specific SQL dialect.
     */
    default String formatPowerFunction(String base, String exponent) {
        return "power(" + base + ", " + exponent + ")";
    }

    /**
     * Returns the 'round' function formatted for a specific SQL dialect.
     * @param operand the numeric expression to perform rounding on
     * @return the 'round' function for a specific SQL dialect.
     */
    String formatRoundFunction(String operand);

    /**
     * Returns the 'mod' function formatted for a specific SQL dialect.
     * @param divisor the numeric expression for divisor operand
     * @param dividend the numeric expression for dividend operand
     * @return the 'mod' function for a specific SQL dialect.
     */
    String formatModuloFunction(String divisor, String dividend);

    /**
     * Returns the 'current_date' system function formatted for a specific SQL dialect.
     * @return the 'current_date' function for a specific SQL dialect.
     */
    String formatCurrentDateFunction();

    /**
     * Returns the string concatenation operator for a specific SQL Dialect.
     * @return the string concatenation operator for a specific SQL Dialect.
     */
    Optional<String> getConcatOperator();

    /**
     * Indicates whether a capability is supported by a specific SQL dialect.
     * @param capability the capability to check support for
     * @return true if specified capability is supported; else false
     */
    boolean supports(Capability capability);

    /**
     * Creates the {@link Dialect} instance corresponding to database vendor associated with specified connection.
     * @param connection the connection.
     * @return the {@link Dialect} instance.
     */
    static Dialect fromConnection(Connection connection) {
        try {
            String productName = connection.getMetaData().getDatabaseProductName().toLowerCase();

            if (productName.contains("h2")) {
                return new H2Dialect().withCustomizations(connection);
            } else if (productName.contains("mysql")) {
                return new MySqlDialect();
            } else if (productName.contains("mariadb")) {
                return new MariaDbDialect();
            } else if (productName.contains("postgresql")) {
                return new PostgreSqlDialect();
            } else if (productName.contains("oracle")) {
                return new OracleDialect();
            } else if (productName.contains("sql server")) {
                return new SqlServerDialect();
            } else if (productName.contains("access")) {
                return new AccessDialect();
            } else if (productName.contains("sqlite")) {
                return new SqliteDialect();
            } else if (productName.contains("hsql")) {
                return new HsqldbDialect();
            } else {
                throw new UnsupportedOperationException("Database with product name " + productName + " not supported");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to detect SQL dialect from connection metadata", e);
        }
    }
}
