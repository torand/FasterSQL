/*
 * Copyright (c) 2024 Tore Eide Andersen
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
package io.github.torand.fastersql;

import io.github.torand.fastersql.alias.ColumnAlias;

import java.util.stream.Stream;

/**
 * A construct that can be expressed as an SQL fragment.
 */
public interface Sql {
    /**
     * Formats object as an SQL fragment
     * @param context the context (incl. dialect)
     * @return the formatted SQL fragment
     */
    String sql(Context context);

    /**
     * Returns the statement parameters introduced by this fragment
     * @param context the context (incl. dialect)
     * @return the statement parameters
     */
    Stream<Object> params(Context context);

    /**
     * Returns the columns referenced by this fragment.
     * For validation purposes.
     * @return the columns referenced by this fragment
     */
    Stream<Column> columnRefs();

    /**
     * Returns the column aliases referenced by this fragment.
     * For validation purposes.
     * @return the column aliases referenced by this fragment
     */
    Stream<ColumnAlias> aliasRefs();
}
