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
package io.github.torand.fastersql.statement;

import io.github.torand.fastersql.projection.Projection;
import io.github.torand.fastersql.relation.Relation;

import java.util.Collection;
import java.util.List;

import static io.github.torand.javacommons.collection.CollectionHelper.asList;
import static io.github.torand.javacommons.collection.CollectionHelper.asNonEmptyList;
import static io.github.torand.javacommons.contract.Requires.require;
import static io.github.torand.javacommons.contract.Requires.requireNonEmpty;
import static java.util.Objects.requireNonNull;

/**
 * Builder of SELECT statements with a FROM clause.
 */
public class SelectFromBuilder {
    private final boolean distinct;
    private final List<Projection> projections;

    SelectFromBuilder(boolean distinct, Collection<Projection> projections) {
        requireNonEmpty(projections, "No projections specified");
        require(() -> !projections.contains(null), "Use nullValue() to provide an SQL NULL projection");

        this.distinct = distinct;
        this.projections = asList(projections);
    }

    /**
     * Specifies that statement should return unique rows.
     * @return the modified statement.
     */
    public SelectFromBuilder distinct() {
        return new SelectFromBuilder(true, projections);
    }

    /**
     * Creates a SELECT statement with specified FROM clauses (relations).
     * @param firstRelation the first relation.
     * @param moreRelations the other relations, if any.
     * @return the statement.
     */
    public SelectStatement from(Relation firstRelation, Relation... moreRelations) {
        requireNonNull(firstRelation, "First relation is null");
        List<Relation> relations = asNonEmptyList(firstRelation, moreRelations);
        return new SelectStatement(projections, relations, null, null, null, null, null, distinct, null, null, false);
    }
}
