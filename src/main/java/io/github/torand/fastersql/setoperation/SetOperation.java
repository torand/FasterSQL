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
package io.github.torand.fastersql.setoperation;

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.sql.Context;
import io.github.torand.fastersql.sql.Sql;
import io.github.torand.fastersql.statement.SelectStatement;

import java.util.stream.Stream;

import static io.github.torand.fastersql.dialect.Capability.SET_OPERATION_PARENTHESES;
import static java.util.Objects.requireNonNull;

/**
 * Implements a set operation.
 */
public class SetOperation implements Sql {

    private final SelectStatement operand;
    private final SetOperator operator;
    private final boolean all;
    /**
     * Creates a set operation clause.
     * @param operand the SELECT statement operand.
     * @param operator the set operator applied.
     */
    public SetOperation(SelectStatement operand, SetOperator operator) {
        this.operand = requireNonNull(operand, "No operand specified");
        this.operator = requireNonNull(operator, "No operator specified");
        this.all = false;
    }

    private SetOperation(SelectStatement operand, SetOperator operator, boolean all) {
        this.operand = operand;
        this.operator = operator;
        this.all = all;
    }

    /**
     * Specifies that this is a natural set operation, i.e. not excluding duplicates.
     * @return the modified set operation.
     */
    public SetOperation all() {
        return new SetOperation(operand, operator, true);
    }

    /**
     * Gets the operand of this set operation.
     * @return the operand.
     */
    public SelectStatement operand() {
        return operand;
    }

    // Sql

    @Override
    public String sql(Context context) {
        StringBuilder sql = new StringBuilder()
            .append(context.getDialect().formatSetOperator(operator))
            .append(" ");

        if (all) {
            sql.append("all ");
        }

        if (context.getDialect().supports(SET_OPERATION_PARENTHESES)) {
            sql.append("(");
        }
        sql.append(operand.sql(context));
        if (context.getDialect().supports(SET_OPERATION_PARENTHESES)) {
            sql.append(")");
        }

        return sql.toString();
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
}
