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

/**
 * Defines capabilities of an SQL dialect.
 */
public enum Capability {
    /**
     * Supports row limiting clauses
     */
    LIMIT_OFFSET,

    /**
     * Supports a row number literal or function
     */
    ROW_NUM,

    /**
     * Supports a string concatenation infix operator
     */
    CONCAT_OPERATOR,

    /**
     * Supports a numeric modulo infix operator
     */
    MODULO_OPERATOR,

    /**
     * Supports a current time literal or function
     */
    CURRENT_TIME,

    /**
     * Supports "nulls First" and "nulls last" for NULL ordering
     */
    NULL_ORDERING,

    /**
     * Supports "SELECT ... FOR UPDATE" clauses
     */
    SELECT_FOR_UPDATE
}
