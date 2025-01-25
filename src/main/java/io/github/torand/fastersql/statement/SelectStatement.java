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
package io.github.torand.fastersql.statement;

import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.Field;
import io.github.torand.fastersql.Join;
import io.github.torand.fastersql.Table;
import io.github.torand.fastersql.condition.Condition;
import io.github.torand.fastersql.condition.OptionalCondition;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.function.aggregate.AggregateFunction;
import io.github.torand.fastersql.order.Order;
import io.github.torand.fastersql.projection.Projection;
import io.github.torand.fastersql.subquery.Subquery;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.github.torand.fastersql.Command.SELECT;
import static io.github.torand.fastersql.dialect.Capability.LIMIT_OFFSET;
import static io.github.torand.fastersql.statement.Helpers.unwrapSuppliers;
import static io.github.torand.fastersql.util.collection.CollectionHelper.*;
import static io.github.torand.fastersql.util.contract.Requires.require;
import static io.github.torand.fastersql.util.contract.Requires.requireNonEmpty;
import static io.github.torand.fastersql.util.functional.Functions.castTo;
import static io.github.torand.fastersql.util.functional.Optionals.mapIfNonNull;
import static io.github.torand.fastersql.util.functional.Predicates.instanceOf;
import static io.github.torand.fastersql.util.functional.Predicates.not;
import static io.github.torand.fastersql.util.lang.StringHelper.isBlank;
import static io.github.torand.fastersql.util.lang.StringHelper.nonBlank;
import static java.util.Objects.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

public class SelectStatement extends PreparableStatement {
    private final List<Projection> projections;
    private final List<Table<?>> tables;
    private final List<Join> joins;
    private final Subquery subqueryFrom;
    private final List<Condition> conditions;
    private final List<Field> groups;
    private final List<Order> orders;
    private final boolean distinct;
    private final Long limit;
    private final Long offset;
    private final boolean forUpdate;

    SelectStatement(List<Projection> projections, List<Table<?>> tables, List<Join> joins, Subquery subqueryFrom, List<Condition> conditions, List<Field> groups, List<Order> orders, boolean distinct, Long limit, Long offset, boolean forUpdate) {
        this.projections = asList(projections);
        this.tables = asList(tables);
        this.joins = asList(joins);
        this.subqueryFrom = subqueryFrom;
        this.conditions = asList(conditions);
        this.groups = asList(groups);
        this.orders = asList(orders);
        this.distinct = distinct;
        this.limit = limit;
        this.offset = offset;
        this.forUpdate = forUpdate;
    }

    public SelectStatement join(Join... joins) {
        requireNonEmpty(joins, "No joins specified");
        require(() -> isNull(subqueryFrom), "Can't combine a subquery FROM clause with joins");

        List<Join> concatenated = concat(this.joins, joins);
        return new SelectStatement(projections, tables, concatenated, subqueryFrom, conditions, groups, orders, distinct, limit, offset, forUpdate);
    }

    public SelectStatement leftOuterJoin(Join join) {
        requireNonNull(join, "No join specified");
        return join(join.leftOuter());
    }

    public SelectStatement rightOuterJoin(Join join) {
        requireNonNull(join, "No join specified");
        return join(join.rightOuter());
    }

    @SafeVarargs
    public final SelectStatement joinIf(boolean predicate, Supplier<Join>... joinSuppliers) {
        requireNonEmpty(joinSuppliers, "No join suppliers specified");
        require(() -> isNull(subqueryFrom), "Can't combine a subquery FROM clause with joins");
        if (predicate) {
            List<Join> concatenated = concat(this.joins, unwrapSuppliers(joinSuppliers));
            return new SelectStatement(projections, tables, concatenated, subqueryFrom, conditions, groups, orders, distinct, limit, offset, forUpdate);
        } else {
            return this;
        }
    }

    /**
     * Adds one or more conditions to the where clause.
     * @param conditions the conditions to add
     * @return updated statement, for method chaining
     */
    public SelectStatement where(Condition... conditions) {
        requireNonEmpty(conditions, "No conditions specified");

        List<Condition> concatenated = concat(this.conditions, conditions);
        return new SelectStatement(projections, tables, joins, subqueryFrom, concatenated, groups, orders, distinct, limit, offset, forUpdate);
    }

    /**
     * Same as other method of same name, but only adds to the where clause conditions that are present.
     * @param maybeConditions the condition that may be present or not
     * @return updated statement, for method chaining
     */
    @SafeVarargs
    public final SelectStatement where(OptionalCondition... maybeConditions) {
        requireNonEmpty(maybeConditions, "No optional conditions specified");

        List<Condition> concatenated = concat(this.conditions, OptionalCondition.unwrap(maybeConditions));
        return new SelectStatement(projections, tables, joins, subqueryFrom, concatenated, groups, orders, distinct, limit, offset, forUpdate);
    }

    /**
     * Adds one or more conditions to the where clause, if a predicate is true.
     * @param predicate the predicate that must be true for conditions to be added
     * @param conditionSuppliers the suppliers providing conditions to add
     * @return updatet statement, for method chaining
     */
    @SafeVarargs
    public final SelectStatement whereIf(boolean predicate, Supplier<Condition>... conditionSuppliers) {
        requireNonEmpty(conditionSuppliers, "No condition suppliers specified");
        if (predicate) {
            List<Condition> concatenated = concat(this.conditions, unwrapSuppliers(conditionSuppliers));
            return new SelectStatement(projections, tables, joins, subqueryFrom, concatenated, groups, orders, distinct, limit, offset, forUpdate);
        } else {
            return this;
        }
    }

    public SelectStatement groupBy(Field... groups) {
        requireNonEmpty(groups, "No groups specified");

        List<Field> concatenated = concat(this.groups, groups);
        return new SelectStatement(projections, tables, joins, subqueryFrom, conditions, concatenated, orders, distinct, limit, offset, forUpdate);
    }

    public SelectStatement orderBy(Order... orders) {
        requireNonEmpty(orders, "No orders specified");

        List<Order> concatenated = concat(this.orders, orders);
        return new SelectStatement(projections, tables, joins, subqueryFrom, conditions, groups, concatenated, distinct, limit, offset, forUpdate);
    }

    public SelectStatement limit(long limit) {
        return new SelectStatement(projections, tables, joins, subqueryFrom, conditions, groups, orders, distinct, limit, offset, forUpdate);
    }

    public SelectStatement offset(long offset) {
        return new SelectStatement(projections, tables, joins, subqueryFrom, conditions, groups, orders, distinct, limit, offset, forUpdate);
    }

    public SelectStatement forUpdate() {
        return new SelectStatement(projections, tables, joins, subqueryFrom, conditions, groups, orders, distinct, limit, offset, forUpdate);
    }

    @Override
    public String sql(Context context) {
        final Context localContext = context.withCommand(SELECT);
        validate();

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        if (distinct) {
            sb.append("distinct ");
        }

        sb.append(streamSafely(projections)
            .map(p -> p.sql(localContext) + (isBlank(p.alias()) ? "" : " " + p.alias()))
            .collect(joining(", ")));

        sb.append(" from ");

        if (nonNull(subqueryFrom)) {
            sb.append(subqueryFrom.sql(context));
            if (nonBlank(subqueryFrom.alias())) {
                sb.append(" ");
                sb.append(subqueryFrom.alias());
            }
        } else {
            // Tables that are joined with should not be specified in the FROM clause
            Set<Table<?>> joinedTables = streamSafely(joins).map(Join::joined).collect(toSet());

            sb.append(streamSafely(tables)
                .filter(not(joinedTables::contains))
                .map(t -> t.sql(localContext))
                .collect(joining(", ")));

            if (nonEmpty(joins)) {
                sb.append(" ");
                sb.append(streamSafely(joins)
                    .map(j -> j.sql(localContext))
                    .collect(joining(" ")));
            }
        }

        if (nonEmpty(conditions)) {
            sb.append(" where ");
            sb.append(streamSafely(conditions)
                .map(e -> e.sql(localContext))
                .collect(joining(" and ")));
        }

        if (nonEmpty(groups)) {
            sb.append(" group by ");
            sb.append(streamSafely(groups)
                .map(g -> g.sql(localContext))
                .collect(joining(", ")));
        }

        if (nonEmpty(orders)) {
            sb.append(" order by ");
            sb.append(streamSafely(orders)
                .map(o -> o.sql(localContext))
                .collect(joining(", ")));
        }

        if (nonNull(offset) || nonNull(limit)) {
            if (context.getDialect().supports(LIMIT_OFFSET)) {
                if (nonNull(limit)) {
                    sb.append(" limit ?");
                }
                if (nonNull(offset)) {
                    sb.append(" offset ?");
                }
            } else {
                sb = addLimitOffsetFallback(context, sb, rowFrom(), rowTo());
            }
        }

        if (forUpdate) {
            sb.append(" for update");
        }

        return sb.toString();
    }

    private Long rowFrom() {
        return mapIfNonNull(offset, o -> o + 1);
    }

    private Long rowTo() {
        return mapIfNonNull(limit, l -> (nonNull(offset) ? offset : 0) + l);
    }

    private StringBuilder addLimitOffsetFallback(Context context, StringBuilder innerSql, Long rowFrom, Long rowTo) {
        String rowNum = context.getDialect().getRowNumLiteral()
            .orElseThrow(() -> new RuntimeException("Dialect " + context.getDialect().getProductName() + " has no ROWNUM literal"));

        if (nonNull(rowFrom) && nonNull(rowTo)) {
            String limitSql = "select original.*, {ROWNUM} row_no from ( " + innerSql.toString() + " ) original where {ROWNUM} <= ?";
            String offsetSql = "select * from ( " + limitSql + " ) where row_no >= ?";
            return new StringBuilder(offsetSql.replace("{ROWNUM}", rowNum));
        } else if (nonNull(rowFrom)) {
            String offsetSql = "select * from ( " + innerSql.toString() + " ) where {ROWNUM} >= ?";
            return new StringBuilder(offsetSql.replace("{ROWNUM}", rowNum));
        } else if (nonNull(rowTo)) {
            String limitSql = "select * from ( " + innerSql.toString() + " ) where {ROWNUM} <= ?";
            return new StringBuilder(limitSql.replace("{ROWNUM}", rowNum));
        }

        return innerSql;
    }

    @Override
    public List<Object> params(Context context) {
        List<Object> params = new LinkedList<>();

        streamSafely(projections).flatMap(p -> p.params(context)).forEach(params::add);

        if (nonNull(subqueryFrom)) {
            subqueryFrom.params(context).forEach(params::add);
        }

        streamSafely(conditions).flatMap(e -> e.params(context)).forEach(params::add);

        if (nonNull(limit)) {
            if (context.getDialect().supports(LIMIT_OFFSET)) {
                params.add(limit);
            } else {
                params.add(rowTo());
            }
        }
        if (nonNull(offset)) {
            if (context.getDialect().supports(LIMIT_OFFSET)) {
                params.add(offset);
            } else {
                params.add(rowFrom());
            }
        }

        return params;
    }

    private void validate() {
        if (isEmpty(tables) && isNull(subqueryFrom)) {
            throw new IllegalStateException("No FROM clause specified");
        }

        List<Field> projectedFields = streamSafely(projections)
            .filter(instanceOf(Expression.class))
            .map(castTo(Expression.class))
            .flatMap(Expression::fieldRefs)
            .toList();
        validateFieldTableRelations(streamSafely(projectedFields));

        if (nonNull(joins)) {
            validateFieldTableRelations(streamSafely(joins).flatMap(Join::fieldRefs));
        }

        validateFieldTableRelations(streamSafely(conditions).flatMap(Condition::fieldRefs));
        validateFieldTableRelations(streamSafely(groups));
        validateFieldTableRelations(streamSafely(orders).flatMap(Order::fieldRefs));

        if (forUpdate) {
            if (distinct || nonEmpty(groups) || streamSafely(projections).anyMatch(instanceOf(AggregateFunction.class))) {
                throw new IllegalStateException("SELECT ... FOR UPDATE can't be used with DISTINCT, GROUP BY or aggregates");
            }
        }
    }

    private void validateFieldTableRelations(Stream<Field> fields) {
        Set<String> tableNames = Stream.concat(streamSafely(tables), streamSafely(joins).map(Join::joined))
            .map(Table::name)
            .collect(toSet());

        fields
            .filter(f -> !tableNames.contains(f.table().name()))
            .findFirst()
            .ifPresent(f -> {
                throw new IllegalStateException("Field " + f.name() + " belongs to table " + f.table().name() + ", but is not specified in a FROM or JOIN clause");
            });
    }
}
