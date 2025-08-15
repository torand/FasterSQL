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
 * Defines the ISO SQL data types.
 */
public enum IsoDataType {
    /** The BOOLEAN data type */
    BOOLEAN,

    /** The CHAR data type */
    CHAR,

    /** The VARCHAR data type */
    VARCHAR,

    /** The BIT data type */
    BIT,

    /** The BIT VARYING data type */
    BIT_VARYING,

    /** The NUMERIC data type */
    NUMERIC,

    /** The DECIMAL data type */
    DECIMAL,

    /** The INTEGER  data type */
    INTEGER,

    /** The SMALLINT data type */
    SMALLINT,

    /** The FLOAT data type */
    FLOAT,

    /** The REAL data type */
    REAL,

    /** The DOUBLE PRECISION  data type */
    DOUBLE_PRECISION,

    /** The DATE data type */
    DATE,

    /** The TIME data type */
    TIME,

    /** The INTERVAL data type */
    INTERVAL,

    /** The CHARACTER LARGE OBJECT data type */
    CHARACTER_LARGE_OBJECT,

    /** The BINARY LARGE OBJECT data type */
    BINARY_LARGE_OBJECT
}
