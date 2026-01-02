/*
 * Copyright (c) 2024-2026 Tore Eide Andersen
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
package io.github.torand.fastersql.sql;

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.model.Column;

import java.util.stream.Stream;

/**
 * Defines a construct that can be expressed as an SQL fragment.
 */
public interface Sql {
    /**
     * Formats object as an SQL fragment.
     * @param context the context (incl. dialect).
     * @return the formatted SQL fragment.
     */
    String sql(Context context);

    /**
     * Gets the statement parameters introduced by this fragment.
     * @param context the context (incl. dialect).
     * @return the statement parameters.
     */
    Stream<Object> params(Context context);

    /**
     * Gets the columns referenced by this fragment.
     * For validation purposes.
     * @return the columns referenced by this fragment.
     */
    default Stream<Column> columnRefs() {
        return Stream.empty();
    }

    /**
     * Gets the column aliases referenced by this fragment.
     * For validation purposes.
     * @return the column aliases referenced by this fragment.
     */
    default Stream<ColumnAlias> aliasRefs() {
        return Stream.empty();
    }
}
