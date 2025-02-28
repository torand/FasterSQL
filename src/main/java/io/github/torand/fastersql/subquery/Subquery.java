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
import io.github.torand.fastersql.alias.TableAlias;
import io.github.torand.fastersql.statement.SelectStatement;

import java.util.stream.Stream;

import static io.github.torand.fastersql.util.collection.CollectionHelper.streamSafely;
import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;
import static java.util.Objects.requireNonNull;

public class Subquery implements Sql {
    private final SelectStatement selectStatement;
    private final TableAlias alias; // When used as projection (scalar value) this is interpreted as a column alias

    public Subquery(SelectStatement selectStatement) {
        this.selectStatement = requireNonNull(selectStatement, "No select statement specified");
        this.alias = null;
    }

    private Subquery(SelectStatement selectStatement, String alias) {
        this.selectStatement = requireNonNull(selectStatement, "No select statement specified");
        this.alias = new TableAlias(requireNonBlank(alias, "No alias specified"));
    }

    // Sql

    @Override
    public String sql(Context context) {
        return "(" + selectStatement.sql(context) + ")";
    }

    @Override
    public Stream<Object> params(Context context) {
        return streamSafely(selectStatement.params(context));
    }

    @Override
    public Stream<Column> columnRefs() {
        return Stream.empty();
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return Stream.empty();
    }

    // "Projection"

    public Subquery as(String alias) {
        return new Subquery(selectStatement, alias);
    }

    public String alias() {
        return alias.name();
    }
}
