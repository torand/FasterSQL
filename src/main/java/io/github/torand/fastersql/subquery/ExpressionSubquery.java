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

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.projection.Projection;
import io.github.torand.fastersql.statement.SelectStatement;

import java.util.Optional;

import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;
import static java.util.Objects.requireNonNull;

public class ExpressionSubquery implements Subquery, Expression {
    private final SelectStatement selectStatement;
    private final ColumnAlias alias;

    public ExpressionSubquery(SelectStatement selectStatement) {
        this.selectStatement = requireNonNull(selectStatement, "No select statement specified");
        this.alias = null;
    }

    private ExpressionSubquery(SelectStatement selectStatement, String alias) {
        this.selectStatement = requireNonNull(selectStatement, "No select statement specified");
        this.alias = new ColumnAlias(requireNonBlank(alias, "No alias specified"));
    }

    // Subquery

    @Override
    public SelectStatement selectStatement() {
        return selectStatement;
    }

    // Projection

    @Override
    public Projection as(String alias) {
        return new ExpressionSubquery(selectStatement, alias);
    }

    @Override
    public Optional<ColumnAlias> alias() {
        return Optional.ofNullable(alias);
    }
}
