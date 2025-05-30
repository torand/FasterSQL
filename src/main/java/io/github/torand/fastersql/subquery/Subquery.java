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
package io.github.torand.fastersql.subquery;

import io.github.torand.fastersql.sql.Sql;
import io.github.torand.fastersql.statement.SelectStatement;

/**
 * Defines a subquery.
 */
public interface Subquery extends Sql {

    /**
     * Gets the SELECT statement of this subquery.
     * @return the SELECT statement of this subquery.
     */
    SelectStatement query();
}
