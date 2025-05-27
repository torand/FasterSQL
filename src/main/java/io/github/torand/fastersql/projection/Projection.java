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
package io.github.torand.fastersql.projection;

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.sql.Sql;

import java.util.Optional;

/**
 * Defines a construct which can be returned in a result set, i.e. an item in the SELECT clause.
 */
public interface Projection extends Sql {

    /**
     * Assigns a column alias (label) for this projection.
     * @param alias the column alias or label.
     * @return the projection with specified alias assigned.
     */
    Projection as(String alias);

    /**
     * Gets the column alias (label) assigned for this projection.
     * @return the column alias (label) assigned for this projection.
     */
    Optional<ColumnAlias> alias();
}
