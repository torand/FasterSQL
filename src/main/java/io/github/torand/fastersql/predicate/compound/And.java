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
package io.github.torand.fastersql.predicate.compound;

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.predicate.Predicate;
import io.github.torand.fastersql.sql.Context;
import io.github.torand.fastersql.sql.Sql;

import java.util.List;
import java.util.stream.Stream;

import static io.github.torand.javacommons.collection.CollectionHelper.asList;
import static io.github.torand.javacommons.contract.Requires.requireNonEmpty;
import static java.util.stream.Collectors.joining;

/**
 * Implements the compound predicate using the boolean operator AND on its operands.
 */
public class And implements Predicate {
    private final List<Predicate> operands;

    And(Predicate... operands) {
        this.operands = asList(requireNonEmpty(operands, "No operands specified"));
    }

    // Sql

    @Override
    public String sql(Context context) {
        return operands.stream()
            .map(e -> e instanceof Or ? "(" + e.sql(context) + ")" : e.sql(context))
            .collect(joining(" and "));
    }

    @Override
    public Stream<Object> params(Context context) {
        return operands.stream().flatMap(o -> o.params(context));
    }

    @Override
    public Stream<Column> columnRefs() {
        return operands.stream().flatMap(Sql::columnRefs);
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return operands.stream().flatMap(Sql::aliasRefs);
    }

    // Predicate

    @Override
    public String negatedSql(Context context) {
        return "not (" + sql(context) + ")";
    }
}
