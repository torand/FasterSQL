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
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.function.aggregate.AggregateFunction;
import io.github.torand.fastersql.join.Join;
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.model.Table;
import io.github.torand.fastersql.order.Order;
import io.github.torand.fastersql.predicate.OptionalPredicate;
import io.github.torand.fastersql.predicate.Predicate;
import io.github.torand.fastersql.projection.Projection;
import io.github.torand.fastersql.relation.Relation;
import io.github.torand.fastersql.setoperation.SetOperation;
import io.github.torand.fastersql.setoperation.SetOperator;
import io.github.torand.fastersql.sql.Context;
import io.github.torand.fastersql.subquery.Subquery;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.github.torand.fastersql.dialect.Capability.LIMIT_OFFSET;
import static io.github.torand.fastersql.dialect.Capability.SELECT_FOR_UPDATE;
import static io.github.torand.fastersql.sql.Command.SELECT;
import static io.github.torand.fastersql.statement.Helpers.unwrapSuppliers;
import static io.github.torand.javacommons.collection.CollectionHelper.asList;
import static io.github.torand.javacommons.collection.CollectionHelper.concat;
import static io.github.torand.javacommons.collection.CollectionHelper.isEmpty;
import static io.github.torand.javacommons.collection.CollectionHelper.nonEmpty;
import static io.github.torand.javacommons.contract.Requires.require;
import static io.github.torand.javacommons.contract.Requires.requireNonEmpty;
import static io.github.torand.javacommons.functional.Functions.castTo;
import static io.github.torand.javacommons.functional.Optionals.mapSafely;
import static io.github.torand.javacommons.functional.Predicates.instanceOf;
import static io.github.torand.javacommons.stream.StreamHelper.streamSafely;
import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

/**
 * Implements a SELECT statement.
 */
public class SelectStatement implements PreparableStatement {
    private final List<Projection> projections;
    private final List<Relation> relations;
    private final List<Join> joins;
    private final List<Predicate> wherePredicates;
    private final List<Column> groups;
    private final List<Predicate> havingPredicates;
    private final List<Order> orders;
    private final boolean distinct;
    private final Long limit;
    private final Long offset;
    private final boolean forUpdate;

    SelectStatement(List<Projection> projections, List<Relation> relations, List<Join> joins, List<Predicate> wherePredicates, List<Column> groups, List<Predicate> havingPredicates, List<Order> orders, boolean distinct, Long limit, Long offset, boolean forUpdate) {
        this.projections = asList(projections);
        this.relations = asList(relations);
        this.joins = asList(joins);
        this.wherePredicates = asList(wherePredicates);
        this.groups = asList(groups);
        this.havingPredicates = asList(havingPredicates);
        this.orders = asList(orders);
        this.distinct = distinct;
        this.limit = limit;
        this.offset = offset;
        this.forUpdate = forUpdate;
    }

    /**
     * Adds one or more JOIN clauses.
     * @param joins the JOIN clauses.
     * @return the modified statement.
     */
    public SelectStatement join(Join... joins) {
        requireNonEmpty(joins, "No joins specified");
        require(() -> streamSafely(relations).noneMatch(instanceOf(Subquery.class)), "Can't combine a subquery FROM clause with joins");

        List<Join> concatenated = concat(this.joins, joins);
        return new SelectStatement(projections, relations, concatenated, wherePredicates, groups, havingPredicates, orders, distinct, limit, offset, forUpdate);
    }

    /**
     * Adds a LEFT OUTER JOIN clause.
     * @param join the JOIN clause.
     * @return the modified statement.
     */
    public SelectStatement leftOuterJoin(Join join) {
        requireNonNull(join, "No join specified");
        return join(join.leftOuter());
    }

    /**
     * Adds a RIGHT OUTER JOIN clause.
     * @param join the JOIN clause.
     * @return the modified statement.
     */
    public SelectStatement rightOuterJoin(Join join) {
        requireNonNull(join, "No join specified");
        return join(join.rightOuter());
    }

    /**
     * Adds a FULL OUTER JOIN clause.
     * @param join the JOIN clause.
     * @return the modified statement.
     */
    public SelectStatement fullOuterJoin(Join join) {
        requireNonNull(join, "No join specified");
        return join(join.fullOuter());
    }

    /**
     * Adds one or more JOIN clauses, if the condition is true.
     * @param condition the condition.
     * @param joinSuppliers the suppliers of JOIN clauses.
     * @return the modified statement.
     */
    @SafeVarargs
    public final SelectStatement joinIf(boolean condition, Supplier<Join>... joinSuppliers) {
        requireNonEmpty(joinSuppliers, "No join suppliers specified");
        require(() -> streamSafely(relations).noneMatch(instanceOf(Subquery.class)), "Can't combine a subquery FROM clause with joins");
        if (condition) {
            List<Join> concatenated = concat(this.joins, unwrapSuppliers(joinSuppliers));
            return new SelectStatement(projections, relations, concatenated, wherePredicates, groups, havingPredicates, orders, distinct, limit, offset, forUpdate);
        } else {
            return this;
        }
    }

    /**
     * Adds one or more predicates to the WHERE clause.
     * @param predicates the predicates.
     * @return the modified statement.
     */
    public SelectStatement where(Predicate... predicates) {
        requireNonEmpty(predicates, "No WHERE predicates specified");

        List<Predicate> concatenated = concat(this.wherePredicates, predicates);
        return new SelectStatement(projections, relations, joins, concatenated, groups, havingPredicates, orders, distinct, limit, offset, forUpdate);
    }

    /**
     * Adds optional predicates to the WHERE clause if the wrapped predicates are present.
     * @param maybePredicates the optional predicates.
     * @return the modified statement.
     */
    @SafeVarargs
    public final SelectStatement where(OptionalPredicate... maybePredicates) {
        requireNonEmpty(maybePredicates, "No optional WHERE predicates specified");

        List<Predicate> concatenated = concat(this.wherePredicates, OptionalPredicate.unwrap(maybePredicates));
        return new SelectStatement(projections, relations, joins, concatenated, groups, havingPredicates, orders, distinct, limit, offset, forUpdate);
    }

    /**
     * Adds supplied predicates to the WHERE clause, if the condition is true.
     * @param condition the condition.
     * @param predicateSuppliers the suppliers providing predicates
     * @return the modified statement.
     */
    @SafeVarargs
    public final SelectStatement whereIf(boolean condition, Supplier<Predicate>... predicateSuppliers) {
        requireNonEmpty(predicateSuppliers, "No WHERE predicate suppliers specified");
        if (condition) {
            List<Predicate> concatenated = concat(this.wherePredicates, unwrapSuppliers(predicateSuppliers));
            return new SelectStatement(projections, relations, joins, concatenated, groups, havingPredicates, orders, distinct, limit, offset, forUpdate);
        } else {
            return this;
        }
    }

    /**
     * Adds one or more columns as groups to the GROUP BY clause.
     * @param groups the groups
     * @return the modified statement.
     */
    public SelectStatement groupBy(Column... groups) {
        requireNonEmpty(groups, "No groups specified");

        List<Column> concatenated = concat(this.groups, groups);
        return new SelectStatement(projections, relations, joins, wherePredicates, concatenated, havingPredicates, orders, distinct, limit, offset, forUpdate);
    }

    /**
     * Adds one or more predicates to the HAVING clause.
     * @param predicates the predicates.
     * @return the modified statement.
     */
    public SelectStatement having(Predicate... predicates) {
        requireNonEmpty(predicates, "No HAVING predicates specified");

        List<Predicate> concatenated = concat(this.havingPredicates, predicates);
        return new SelectStatement(projections, relations, joins, wherePredicates, groups, concatenated, orders, distinct, limit, offset, forUpdate);
    }

    /**
     * Adds optional predicates to the HAVING clause if the wrapped predicates are present.
     * @param maybePredicates the optional predicates.
     * @return the modified statement.
     */
    @SafeVarargs
    public final SelectStatement having(OptionalPredicate... maybePredicates) {
        requireNonEmpty(maybePredicates, "No optional HAVING predicates specified");

        List<Predicate> concatenated = concat(this.havingPredicates, OptionalPredicate.unwrap(maybePredicates));
        return new SelectStatement(projections, relations, joins, wherePredicates, groups, concatenated, orders, distinct, limit, offset, forUpdate);
    }

    /**
     * Adds one or more predicates to the HAVING clause, if the condition is true.
     * @param condition the condition.
     * @param predicateSuppliers the suppliers providing predicates.
     * @return the modified statement.
     */
    @SafeVarargs
    public final SelectStatement havingIf(boolean condition, Supplier<Predicate>... predicateSuppliers) {
        requireNonEmpty(predicateSuppliers, "No HAVING predicate suppliers specified");
        if (condition) {
            List<Predicate> concatenated = concat(this.havingPredicates, unwrapSuppliers(predicateSuppliers));
            return new SelectStatement(projections, relations, joins, wherePredicates, groups, concatenated, orders, distinct, limit, offset, forUpdate);
        } else {
            return this;
        }
    }

    /**
     * Adds one or more ORDER clauses.
     * @param orders the ORDER clauses.
     * @return the modified statement.
     */
    public SelectStatement orderBy(Order... orders) {
        requireNonEmpty(orders, "No orders specified");
        List<Order> concatenated = concat(this.orders, orders);
        return new SelectStatement(projections, relations, joins, wherePredicates, groups, havingPredicates, concatenated, distinct, limit, offset, forUpdate);
    }

    /**
     * Adds a LIMIT clause.
     * @param limit the limit.
     * @return the modified statement.
     */
    public SelectStatement limit(long limit) {
        return new SelectStatement(projections, relations, joins, wherePredicates, groups, havingPredicates, orders, distinct, limit, offset, forUpdate);
    }

    /**
     * Adds a OFFSET clause.
     * @param offset the offset.
     * @return the modified statement.
     */
    public SelectStatement offset(long offset) {
        return new SelectStatement(projections, relations, joins, wherePredicates, groups, havingPredicates, orders, distinct, limit, offset, forUpdate);
    }

    /**
     * Adds a FOR UPDATE clause.
     * @return the modified statement.
     */
    public SelectStatement forUpdate() {
        return new SelectStatement(projections, relations, joins, wherePredicates, groups, havingPredicates, orders, distinct, limit, offset, true);
    }

    /**
     * Creates a UNION set operation between this and the specified statement.
     * @param other the other SELECT statement
     * @return the SELECT set operation statement.
     */
    public SelectSetOpStatement union(SelectStatement other) {
        SetOperation setOperation = new SetOperation(other, SetOperator.UNION);
        return new SelectSetOpStatement(this, asList(setOperation), emptyList());
    }

    /**
     * Creates a UNION ALL set operation between this and the specified statement.
     * @param other the other SELECT statement
     * @return the SELECT set operation statement.
     */
    public SelectSetOpStatement unionAll(SelectStatement other) {
        SetOperation setOperation = new SetOperation(other, SetOperator.UNION).all();
        return new SelectSetOpStatement(this, asList(setOperation), emptyList());
    }

    /**
     * Creates an INTERSECT set operation between this and the specified statement.
     * Note that INTERSECT has precedence over UNION and EXCEPT.
     * @param other the other SELECT statement
     * @return the SELECT set operation statement.
     */
    public SelectSetOpStatement intersect(SelectStatement other) {
        SetOperation setOperation = new SetOperation(other, SetOperator.INTERSECT);
        return new SelectSetOpStatement(this, asList(setOperation), emptyList());
    }

    /**
     * Creates an INTERSECT ALL set operation between this and the specified statement.
     * Note that INTERSECT has precedence over UNION and EXCEPT.
     * @param other the other SELECT statement
     * @return the SELECT set operation statement.
     */
    public SelectSetOpStatement intersectAll(SelectStatement other) {
        SetOperation setOperation = new SetOperation(other, SetOperator.INTERSECT).all();
        return new SelectSetOpStatement(this, asList(setOperation), emptyList());
    }

    /**
     * Creates an EXCEPT set operation between this and the specified statement.
     * @param other the other SELECT statement
     * @return the SELECT set operation statement.
     */
    public SelectSetOpStatement except(SelectStatement other) {
        SetOperation setOperation = new SetOperation(other, SetOperator.EXCEPT);
        return new SelectSetOpStatement(this, asList(setOperation), emptyList());
    }

    /**
     * Creates an EXCEPT ALL set operation between this and the specified statement.
     * @param other the other SELECT statement
     * @return the SELECT set operation statement.
     */
    public SelectSetOpStatement exceptAll(SelectStatement other) {
        SetOperation setOperation = new SetOperation(other, SetOperator.EXCEPT).all();
        return new SelectSetOpStatement(this, asList(setOperation), emptyList());
    }

    @Override
    public String sql(Context context) {
        final Context localContext = context
            .withCommand(SELECT)
            .withOuterStatement(this);

        validate(context);

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        if (distinct) {
            sb.append("distinct ");
        }

        sb.append(streamSafely(projections)
            .map(p -> p.sql(localContext) + (p.alias().isEmpty() ? "" : " " + p.alias().map(a -> a.sql(localContext)).get()))
            .collect(joining(", ")));

        sb.append(" from ");

        // Tables that are joined with should not be specified in the FROM clause
        Set<Table> joinedTables = streamSafely(joins).map(Join::joined).collect(toSet());

        sb.append(streamSafely(relations)
            .filter(not(joinedTables::contains))
            .map(t -> t.sql(localContext))
            .collect(joining(", ")));

        if (nonEmpty(joins)) {
            sb.append(" ");
            sb.append(streamSafely(joins)
                .map(j -> j.sql(localContext))
                .collect(joining(" ")));
        }

        if (nonEmpty(wherePredicates)) {
            sb.append(" where ");
            sb.append(streamSafely(wherePredicates)
                .map(p -> p.sql(localContext))
                .collect(joining(" and ")));
        }

        if (nonEmpty(groups)) {
            sb.append(" group by ");
            sb.append(streamSafely(groups)
                .map(g -> g.sql(localContext))
                .collect(joining(", ")));
        }

        if (nonEmpty(havingPredicates)) {
            sb.append(" having ");
            sb.append(streamSafely(havingPredicates)
                .map(p -> p.sql(localContext))
                .collect(joining(" and ")));
        }

        if (nonEmpty(orders)) {
            sb.append(" order by ");
            sb.append(streamSafely(orders)
                .map(o -> o.sql(localContext))
                .collect(joining(", ")));
        }

        if (nonNull(offset) || nonNull(limit)) {
            if (context.getDialect().supports(LIMIT_OFFSET)) {
                String offsetClause = nonNull(offset) ? " " + localContext.getDialect().formatRowOffsetClause().orElseThrow(() -> new RuntimeException("Dialect " + localContext.getDialect().getProductName() + " has no row offset clause")) : "";
                String limitClause = nonNull(limit) ? " " + localContext.getDialect().formatRowLimitClause().orElseThrow(() -> new RuntimeException("Dialect " + localContext.getDialect().getProductName() + " has no row limit clause")) : "";

                if (localContext.getDialect().offsetBeforeLimit()) {
                    sb.append(offsetClause).append(limitClause);
                } else {
                    sb.append(limitClause).append(offsetClause);
                }
            } else {
                sb = addLimitOffsetFallback(localContext, sb, rowFrom(), rowTo());
            }
        }

        if (forUpdate) {
            sb.append(" for update");
        }

        return sb.toString();
    }

    Stream<Projection> projections() {
        return streamSafely(projections);
    }

    private Long rowFrom() {
        return mapSafely(offset, o -> o + 1);
    }

    private Long rowTo() {
        return mapSafely(limit, l -> (nonNull(offset) ? offset : 0) + l);
    }

    private StringBuilder addLimitOffsetFallback(Context context, StringBuilder innerSql, Long rowFrom, Long rowTo) {
        final String ROWNUM = "{ROWNUM}";

        String rowNum = context.getDialect().formatRowNumLiteral()
            .orElseThrow(() -> new RuntimeException("Dialect " + context.getDialect().getProductName() + " has no row number literal"));

        if (nonNull(rowFrom) && nonNull(rowTo)) {
            String limitSql = "select ORIGINAL.*, " + ROWNUM + " ROW_NO from ( " + innerSql.toString() + " ) ORIGINAL where " + ROWNUM + " <= ?";
            String offsetSql = "select * from ( " + limitSql + " ) where ROW_NO >= ?";
            return new StringBuilder(offsetSql.replace(ROWNUM, rowNum));
        } else if (nonNull(rowFrom)) {
            String offsetSql = "select * from ( " + innerSql.toString() + " ) where " + ROWNUM + " >= ?";
            return new StringBuilder(offsetSql.replace(ROWNUM, rowNum));
        } else if (nonNull(rowTo)) {
            String limitSql = "select * from ( " + innerSql.toString() + " ) where " + ROWNUM + " <= ?";
            return new StringBuilder(limitSql.replace(ROWNUM, rowNum));
        }

        return innerSql;
    }

    @Override
    public Stream<Object> params(Context context) {
        List<Object> params = new LinkedList<>();

        streamSafely(projections).flatMap(p -> p.params(context)).forEach(params::add);

        streamSafely(relations).flatMap(r -> r.params(context)).forEach(params::add);

        streamSafely(wherePredicates).flatMap(p -> p.params(context)).forEach(params::add);

        streamSafely(havingPredicates).flatMap(p -> p.params(context)).forEach(params::add);

        if (context.getDialect().supports(LIMIT_OFFSET)) {
            if (context.getDialect().offsetBeforeLimit()) {
                if (nonNull(offset)) {
                    params.add(offset);
                }
                if (nonNull(limit)) {
                    params.add(limit);
                }
            } else {
                if (nonNull(limit)) {
                    params.add(limit);
                }
                if (nonNull(offset)) {
                    params.add(offset);
                }
            }
        } else {
            if (nonNull(limit)) {
                params.add(rowTo());
            }
            if (nonNull(offset)) {
                params.add(rowFrom());
            }
        }

        return params.stream();
    }

    private void validate(Context context) {
        if (isEmpty(relations)) {
            throw new IllegalStateException("No FROM clause specified");
        }

        Stream<Column> projectedColumns = streamSafely(projections)
            .filter(instanceOf(Expression.class))
            .map(castTo(Expression.class))
            .flatMap(Expression::columnRefs);
        validateColumnTableRelations(context, projectedColumns);

        if (nonEmpty(joins)) {
            validateColumnTableRelations(context, streamSafely(joins).flatMap(Join::columnRefs));
        }

        if (nonEmpty(orders)) {
            Set<String> orderableAliases = streamSafely(projections)
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
                    throw new IllegalStateException("ORDER BY column alias " + a + " is not specified in the SELECT clause");
                });
        }

        validateColumnTableRelations(context, streamSafely(wherePredicates).flatMap(Predicate::columnRefs));
        validateColumnTableRelations(context, streamSafely(groups));
        validateColumnTableRelations(context, streamSafely(havingPredicates).flatMap(Predicate::columnRefs));
        validateColumnTableRelations(context, streamSafely(orders).flatMap(Order::columnRefs));

        if (forUpdate) {
            if (!context.getDialect().supports(SELECT_FOR_UPDATE)) {
                throw new UnsupportedOperationException("%s does not support the SELECT ... FOR UPDATE clause".formatted(context.getDialect().getProductName()));
            }

            if (distinct || nonEmpty(groups) || streamSafely(projections).anyMatch(instanceOf(AggregateFunction.class))) {
                throw new IllegalStateException("SELECT ... FOR UPDATE can't be used with DISTINCT, GROUP BY or aggregates");
            }

            List<String> projectedTables = streamSafely(projections)
                .filter(instanceOf(Expression.class))
                .map(castTo(Expression.class))
                .flatMap(Expression::columnRefs)
                .map(cr -> cr.table().name())
                .distinct()
                .toList();

            if (projectedTables.size() != 1) {
                throw new IllegalStateException("SELECT ... FOR UPDATE can be used for a single projected table only. Projected tables in this statement are: %s".formatted(projectedTables));
            }
        }
    }

    private void validateColumnTableRelations(Context context, Stream<Column> columns) {
        Function<Relation, Stream<Table>> filterTables = r -> r instanceof Table table ? Stream.of(table) : Stream.empty();

        Set<String> outerTableNames = new HashSet<>();
        if (nonEmpty(context.getOuterStatements())) {
            context.getOuterStatements().forEach(os -> streamSafely(os.relations).flatMap(filterTables).map(Table::name).forEach(outerTableNames::add));
            context.getOuterStatements().forEach(os -> streamSafely(os.joins).map(Join::joined).map(Table::name).forEach(outerTableNames::add));
        }

        Set<String> tableNames = Stream.concat(streamSafely(relations).flatMap(filterTables), streamSafely(joins).map(Join::joined))
            .map(Table::name)
            .collect(toSet());

        columns
            .filter(c -> !outerTableNames.contains(c.table().name()) && !tableNames.contains(c.table().name()))
            .findFirst()
            .ifPresent(c -> {
                throw new IllegalStateException("Column " + c.name() + " belongs to table " + c.table().name() + ", but is not specified in a FROM or JOIN clause");
            });
    }

    @Override
    public String toString() {
        return toString(new AnsiIsoDialect());
    }
}
