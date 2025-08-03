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
package io.github.torand.fastersql.setoperation;

/**
 * Defines operators for set operations.
 */
public enum SetOperator {
    /**
     * Combine all results from two query blocks into a single result, omitting any duplicates (unless ALL is specified).
     */
    UNION("union"),

    /**
     * Combine only those rows which the results of two query blocks have in common, omitting any duplicates (unless ALL is specified).
     * Note that INTERSECT has precedence over UNION and EXCEPT.
     */
    INTERSECT("intersect"),

    /**
     * For two query blocks A and B, return all results from A which are not also present in B, omitting any duplicates (unless ALL is specified).
     */
    EXCEPT("except");

    private final String sql;

    SetOperator(String sql) {
        this.sql = sql;
    }

    /**
     * Formats set operator as an SQL fragment.
     * @return the formatted SQL fragment.
     */
    public String sql() {
        return sql;
    }
}
