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

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.alias.TableAlias;
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.relation.Relation;
import io.github.torand.fastersql.sql.Context;
import io.github.torand.fastersql.statement.SelectStatement;

import java.util.stream.Stream;

import static io.github.torand.javacommons.contract.Requires.requireNonBlank;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * Implements a subquery to be used in a FROM clause.
 */
public class TableSubquery implements Subquery, Relation {
    private final SelectStatement query;
    private final TableAlias alias;

    /**
     * Creates a table subquery.
     * @param query the subquery.
     */
    public TableSubquery(SelectStatement query) {
        this.query = requireNonNull(query, "No query specified");
        this.alias = null;
    }

    private TableSubquery(SelectStatement query, String alias) {
        this.query = requireNonNull(query, "No query specified");
        this.alias = new TableAlias(requireNonBlank(alias, "No alias specified"));
    }

    // Sql

    @Override
    public String sql(Context context) {
        return "(" + query.sql(context) + ")" + (nonNull(alias) ? " " + alias.sql(context) : "");
    }

    @Override
    public Stream<Object> params(Context context) {
        return query.params(context);
    }

    @Override
    public Stream<Column> columnRefs() {
        return Stream.empty();
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return Stream.empty();
    }

    // Subquery

    @Override
    public SelectStatement query() {
        return query;
    }

    // Relation

    @Override
    public TableSubquery as(String alias) {
        return new TableSubquery(query, alias);
    }

    @Override
    public TableAlias alias() {
        return alias;
    }
}
