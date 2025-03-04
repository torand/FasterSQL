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
package io.github.torand.fastersql.subquery;

import io.github.torand.fastersql.Column;
import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.Sql;
import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.statement.SelectStatement;

import java.util.stream.Stream;

import static io.github.torand.fastersql.util.collection.CollectionHelper.streamSafely;

public interface Subquery extends Sql {

    SelectStatement selectStatement();

    // Sql

    @Override
    default String sql(Context context) {
        return "(" + selectStatement().sql(context) + ")";
    }

    @Override
    default Stream<Object> params(Context context) {
        return streamSafely(selectStatement().params(context));
    }

    @Override
    default Stream<Column> columnRefs() {
        return Stream.empty();
    }

    @Override
    default Stream<ColumnAlias> aliasRefs() {
        return Stream.empty();
    }
}
