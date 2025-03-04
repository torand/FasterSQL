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

import io.github.torand.fastersql.alias.TableAlias;
import io.github.torand.fastersql.statement.SelectStatement;

import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;
import static java.util.Objects.requireNonNull;

public class TableSubquery implements Subquery {
    private final SelectStatement selectStatement;
    private final TableAlias alias;

    public TableSubquery(SelectStatement selectStatement) {
        this.selectStatement = requireNonNull(selectStatement, "No select statement specified");
        this.alias = null;
    }

    private TableSubquery(SelectStatement selectStatement, String alias) {
        this.selectStatement = requireNonNull(selectStatement, "No select statement specified");
        this.alias = new TableAlias(requireNonBlank(alias, "No alias specified"));
    }

    public TableSubquery as(String alias) {
        return new TableSubquery(selectStatement, alias);
    }

    public String alias() {
        return alias.name();
    }

    // Subquery

    @Override
    public SelectStatement selectStatement() {
        return selectStatement;
    }
}
