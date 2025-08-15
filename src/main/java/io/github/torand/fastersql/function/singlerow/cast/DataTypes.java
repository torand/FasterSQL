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
package io.github.torand.fastersql.function.singlerow.cast;

/**
 * Provides factory methods for data types used in CAST expressions.
 */
public class DataTypes {
    private DataTypes() {}

    /**
     * Creates a generic CHAR data type.
     * @return the data type.
     */
    public static DataType char_() {
        return new DataType(IsoDataType.CHAR);
    }

    /**
     * Creates a CHAR data type.
     * @param length the length
     * @return the data type.
     */
    public static DataType char_(int length) {
        return new DataType(IsoDataType.CHAR, length);
    }

    /**
     * Creates a generic VARCHAR data type.
     * @return the data type.
     */
    public static DataType varchar() {
        return new DataType(IsoDataType.VARCHAR);
    }

    /**
     * Creates a VARCHAR data type.
     * @param length the length
     * @return the data type.
     */
    public static DataType varchar(int length) {
        return new DataType(IsoDataType.VARCHAR, length);
    }

    /**
     * Creates a generic NUMERIC data type.
     * @return the data type.
     */
    public static DataType numeric() {
        return new DataType(IsoDataType.NUMERIC);
    }

    /**
     * Creates a NUMERIC data type.
     * @param precision the total number of digits.
     * @return the data type.
     */
    public static DataType numeric(int precision) {
        return numeric(precision, 0);
    }

    /**
     * Creates a NUMERIC data type.
     * @param precision the total number of digits.
     * @param scale the number of decimals.
     * @return the data type.
     */
    public static DataType numeric(int precision, int scale) {
        return new DataType(IsoDataType.NUMERIC, precision, scale);
    }

    /**
     * Creates a DECIMAL data type.
     * @return the data type.
     */
    public static DataType decimal() {
        return new DataType(IsoDataType.DECIMAL);
    }

    /**
     * Creates a DECIMAL data type.
     * @param precision the total number of digits.
     * @return the data type.
     */
    public static DataType decimal(int precision) {
        return decimal(precision, 0);
    }

    /**
     * Creates a DECIMAL data type.
     * @param precision the total number of digits.
     * @param scale the number of decimals.
     * @return the data type.
     */
    public static DataType decimal(int precision, int scale) {
        return new DataType(IsoDataType.DECIMAL, precision, scale);
    }

    /**
     * Creates an INTEGER data type.
     * @return the data type.
     */
    public static DataType integer() {
        return new DataType(IsoDataType.INTEGER);
    }
}
