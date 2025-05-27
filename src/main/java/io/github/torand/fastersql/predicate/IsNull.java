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
package io.github.torand.fastersql.predicate;

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.sql.Context;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Implements the 'is null' predicate.
 */
public class IsNull implements Predicate {
    private final LeftOperand operand;

    IsNull(LeftOperand operand) {
        this.operand = requireNonNull(operand, "No operand specified");
    }

    // Sql

    @Override
    public String sql(Context context) {
        return operand.sql(context) + " is null";
    }

    @Override
    public Stream<Object> params(Context context) {
        return operand.params(context);
    }

    @Override
    public Stream<Column> columnRefs() {
        return operand.columnRefs();
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return operand.aliasRefs();
    }

    // Predicate

    @Override
    public String negatedSql(Context context) {
        return operand.sql(context) + " is not null";
    }
}
