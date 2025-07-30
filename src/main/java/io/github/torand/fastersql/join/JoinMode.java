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
package io.github.torand.fastersql.join;

/**
 * Defines modes of joining two tables.
 */
enum JoinMode {
    /**
     * The inner join
     */
    INNER("inner join"),

    /**
     * The left outer join
     */
    LEFT_OUTER("left outer join"),

    /**
     * The right outer join.
     */
    RIGHT_OUTER("right outer join"),

    /**
     * The full outer join.
     */
    FULL_OUTER("full outer join");

    final String sql;

    JoinMode(String sql) {
        this.sql = sql;
    }
}
