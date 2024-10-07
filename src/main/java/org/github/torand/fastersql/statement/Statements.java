package org.github.torand.fastersql.statement;

import org.github.torand.fastersql.Table;
import org.github.torand.fastersql.projection.Projection;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static org.github.torand.fastersql.constant.Constants.constant;
import static org.github.torand.fastersql.util.collection.CollectionHelper.asNonEmptyList;

public final class Statements {
    private Statements() {}

    public static SelectFromBuilder select(long value) {
        return new SelectFromBuilder(false, singletonList(constant(value)));
    }

    public static SelectFromBuilder select(Projection firstProjection, Projection... moreProjections) {
        requireNonNull(firstProjection, "First projection is null");
        List<Projection> projections = asNonEmptyList(firstProjection, moreProjections);
        return new SelectFromBuilder(false, projections);
    }

    public static SelectFromBuilder selectDistinct(Projection firstProjection, Projection... moreProjections) {
        requireNonNull(firstProjection, "First projection is null");
        List<Projection> projections = asNonEmptyList(firstProjection, moreProjections);
        return new SelectFromBuilder(true, projections);
    }

    public static UpdateStatement update(Table table) {
        return new UpdateStatement(table, null, null);
    }

    public static DeleteFromBuilder delete() {
        return new DeleteFromBuilder();
    }

    public static DeleteStatement deleteFrom(Table table) {
        return new DeleteStatement(table, null);
    }

    public static TruncateTableBuilder truncate() {
        return new TruncateTableBuilder();
    }

    public static TruncateStatement truncateTable(Table table) {
        return new TruncateStatement(table);
    }

    public static InsertIntoBuilder insert() {
        return new InsertIntoBuilder();
    }

    public static InsertStatement insertInto(Table table) {
        return new InsertStatement(table, null);
    }

    public static <T> InsertBatchIntoBuilder<T> insertBatch(Collection<T> entities) {
        return new InsertBatchIntoBuilder<>(entities);
    }
}
