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

import io.github.torand.fastersql.alias.Alias;
import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.dialect.AnsiIsoDialect;
import io.github.torand.fastersql.order.Order;
import io.github.torand.fastersql.projection.Projection;
import io.github.torand.fastersql.setoperation.SetOperation;
import io.github.torand.fastersql.setoperation.SetOperator;
import io.github.torand.fastersql.sql.Context;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;

import static io.github.torand.fastersql.dialect.Capability.SET_OPERATION_PARENTHESES;
import static io.github.torand.fastersql.sql.Command.SELECT_SET_OP;
import static io.github.torand.javacommons.collection.CollectionHelper.asList;
import static io.github.torand.javacommons.collection.CollectionHelper.concat;
import static io.github.torand.javacommons.collection.CollectionHelper.nonEmpty;
import static io.github.torand.javacommons.contract.Requires.requireNonEmpty;
import static io.github.torand.javacommons.stream.StreamHelper.streamSafely;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

/**
 * Implements a SELECT statement with set operations.
 */
public class SelectSetOpStatement implements PreparableStatement {
    private final SelectStatement selectStatement;
    private final List<SetOperation> setOperations;
    private final List<Order> orders;

    SelectSetOpStatement(SelectStatement selectStatement, List<SetOperation> setOperations, List<Order> orders) {
        this.selectStatement = selectStatement;
        this.setOperations = asList(setOperations);
        this.orders = asList(orders);
    }

    /**
     * Adds one or more ORDER clauses.
     * @param orders the ORDER clauses.
     * @return the modified statement.
     */
    public SelectSetOpStatement orderBy(Order... orders) {
        requireNonEmpty(orders, "No orders specified");

        List<Order> concatenated = concat(this.orders, orders);
        return new SelectSetOpStatement(selectStatement, setOperations, concatenated);
    }

    /**
     * Creates a UNION set operation between this and the specified statement.
     * @param operand the set operation operand.
     * @return the modified statement.
     */
    public SelectSetOpStatement union(SelectStatement operand) {
        SetOperation setOperation = new SetOperation(operand, SetOperator.UNION);
        List<SetOperation> concatenated = concat(this.setOperations, setOperation);
        return new SelectSetOpStatement(selectStatement, concatenated, orders);
    }

    /**
     * Creates a UNION ALL set operation between this and the specified statement.
     * @param operand the set operation operand.
     * @return the modified statement.
     */
    public SelectSetOpStatement unionAll(SelectStatement operand) {
        SetOperation setOperation = new SetOperation(operand, SetOperator.UNION).all();
        List<SetOperation> concatenated = concat(this.setOperations, setOperation);
        return new SelectSetOpStatement(selectStatement, concatenated, orders);
    }

    /**
     * Creates an INTERSECT set operation between this and the specified statement.
     * Note that INTERSECT has precedence over UNION and EXCEPT.
     * @param operand the set operation operand.
     * @return the modified statement.
     */
    public SelectSetOpStatement intersect(SelectStatement operand) {
        SetOperation setOperation = new SetOperation(operand, SetOperator.INTERSECT);
        List<SetOperation> concatenated = concat(this.setOperations, setOperation);
        return new SelectSetOpStatement(selectStatement, concatenated, orders);
    }

    /**
     * Creates an INTERSECT ALL set operation between this and the specified statement.
     * Note that INTERSECT has precedence over UNION and EXCEPT.
     * @param operand the set operation operand.
     * @return the modified statement.
     */
    public SelectSetOpStatement intersectAll(SelectStatement operand) {
        SetOperation setOperation = new SetOperation(operand, SetOperator.INTERSECT).all();
        List<SetOperation> concatenated = concat(this.setOperations, setOperation);
        return new SelectSetOpStatement(selectStatement, concatenated, orders);
    }

    /**
     * Creates an EXCEPT set operation between this and the specified statement.
     * @param operand the set operation operand.
     * @return the modified statement.
     */
    public SelectSetOpStatement except(SelectStatement operand) {
        SetOperation setOperation = new SetOperation(operand, SetOperator.EXCEPT);
        List<SetOperation> concatenated = concat(this.setOperations, setOperation);
        return new SelectSetOpStatement(selectStatement, concatenated, orders);
    }

    /**
     * Creates an EXCEPT ALL set operation between this and the specified statement.
     * @param operand the set operation operand.
     * @return the modified statement.
     */
    public SelectSetOpStatement exceptAll(SelectStatement operand) {
        SetOperation setOperation = new SetOperation(operand, SetOperator.EXCEPT).all();
        List<SetOperation> concatenated = concat(this.setOperations, setOperation);
        return new SelectSetOpStatement(selectStatement, concatenated, orders);
    }

    @Override
    public String sql(Context context) {
        final Context localContext = context.withCommand(SELECT_SET_OP);

        validate();

        StringBuilder sb = new StringBuilder();
        if (localContext.getDialect().supports(SET_OPERATION_PARENTHESES)) {
            sb.append("(");
        }

        sb.append(selectStatement.sql(context));

        if (localContext.getDialect().supports(SET_OPERATION_PARENTHESES)) {
            sb.append(")");
        }

        sb.append(" ");

        sb.append(streamSafely(setOperations)
            .map(j -> j.sql(localContext))
            .collect(joining(" ")));

        if (nonEmpty(orders)) {
            sb.append(" order by ")
              .append(streamSafely(orders)
              .map(o -> o.sql(localContext))
              .collect(joining(", ")));
        }

        return sb.toString();
    }

    @Override
    public Stream<Object> params(Context context) {
        List<Object> params = new LinkedList<>();

        selectStatement.params(context).forEach(params::add);
        streamSafely(setOperations).flatMap(so -> so.params(context)).forEach(params::add);

        return params.stream();
    }

    private void validate() {
        ToLongFunction<SelectStatement> projCount = st -> st.projections().count();
        ToLongFunction<SetOperation> setOpProjCount = so -> projCount.applyAsLong(so.operand());

        long expectedProjCount = projCount.applyAsLong(selectStatement);
        if (streamSafely(setOperations).map(setOpProjCount::applyAsLong).anyMatch(actualProjCount -> actualProjCount != expectedProjCount)) {
            throw new IllegalStateException("SELECT statements in a set operation can't have different number of projections");
        }

        if (nonEmpty(orders)) {
            Set<String> orderableAliases = selectStatement.projections()
                .map(Projection::alias)
                .flatMap(Optional::stream)
                .map(Alias::name)
                .collect(toSet());

            streamSafely(orders)
                .flatMap(o -> o.aliasRefs())
                .map(ColumnAlias::name)
                .filter(a -> !orderableAliases.contains(a))
                .findFirst()
                .ifPresent(a -> {
                    throw new IllegalStateException("Set operation ORDER BY column alias " + a + " is not specified in the first SELECT clause");
                });
        }
    }

    @Override
    public String toString() {
        return toString(new AnsiIsoDialect());
    }
}
