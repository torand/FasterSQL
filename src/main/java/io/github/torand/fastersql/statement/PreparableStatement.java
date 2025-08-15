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
package io.github.torand.fastersql.statement;

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.dialect.Dialect;
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.sql.Context;
import io.github.torand.fastersql.sql.Sql;

import java.util.Objects;
import java.util.stream.Stream;

import static io.github.torand.javacommons.lang.StringHelper.isBlank;
import static io.github.torand.javacommons.stream.StreamHelper.streamSafely;
import static java.util.stream.Collectors.joining;

/**
 * Defines a statement that can be formatted as SQL.
 */
public interface PreparableStatement extends Sql {

    @Override
    default Stream<Column> columnRefs() {
        return Stream.empty();
    }

    @Override
    default Stream<ColumnAlias> aliasRefs() {
        return Stream.empty();
    }

    /**
     * Gets the SQL of specified dialect for this statement.
     * @param dialect the SQL dialect.
     * @return the SQL statement.
     */
    default String toString(Dialect dialect) {
        Context context = Context.of(dialect);

        String stringifiedParams = streamSafely(params(context))
            .map(Objects::toString)
            .collect(joining(", "));

        return sql(context) + (isBlank(stringifiedParams) ? " with no params" : " with params " + stringifiedParams);
    }
}
