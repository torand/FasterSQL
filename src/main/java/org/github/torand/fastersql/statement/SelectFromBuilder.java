package org.github.torand.fastersql.statement;

import org.github.torand.fastersql.Table;
import org.github.torand.fastersql.projection.Projection;
import org.github.torand.fastersql.subquery.Subquery;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.github.torand.fastersql.util.collection.CollectionHelper.asList;
import static org.github.torand.fastersql.util.collection.CollectionHelper.asNonEmptyList;
import static org.github.torand.fastersql.util.contract.Requires.*;

public class SelectFromBuilder {
    private final boolean distinct;
    private final List<Projection> projections;

    SelectFromBuilder(boolean distinct, Collection<Projection> projections) {
        requireNonEmpty(projections, "No projections specified");
        require(() -> !projections.contains(null), "Use nullValue() to provide an SQL NULL projection");

        this.distinct = distinct;
        this.projections = asList(projections);
    }

    public SelectFromBuilder distinct() {
        return new SelectFromBuilder(true, projections);
    }

    public SelectStatement from(Table firstTable, Table... moreTables) {
        requireNonNull(firstTable, "First table is null");
        List<Table> tables = asNonEmptyList(firstTable, moreTables);
        return new SelectStatement(projections, tables, null, null, null, null, null, distinct, null, null, false);
    }

    public SelectStatement from(SelectStatement inner) {
        requireNonNull(inner, "No select statement specified");
        return new SelectStatement(projections, null, null, new Subquery(inner), null, null, null, distinct, null, null, false);
    }

    public SelectStatement from(SelectStatement inner, String alias) {
        requireNonNull(inner, "No select statement specified");
        requireNonBlank(alias, "No alias specified");
        return new SelectStatement(projections, null, null, new Subquery(inner).as(alias), null, null, null, distinct, null, null, false);
    }
}
