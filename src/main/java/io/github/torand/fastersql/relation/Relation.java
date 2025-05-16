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
package io.github.torand.fastersql.relation;

import io.github.torand.fastersql.Sql;
import io.github.torand.fastersql.alias.TableAlias;

/**
 * Defines a table of columns and rows, whether a permanent database table or a temporary table (the result of a subquery).
 */
public interface Relation extends Sql {

    /**
     * Specifies the table alias name of this relation.
     * @param alias the table alias name.
     * @return the modified relation.
     */
    Relation as(String alias);

    /**
     * Gets the table alias specified for this relation.
     * @return the table alias.
     */
    TableAlias alias();
}
