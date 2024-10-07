package org.github.torand.fastersql.statement;

import org.github.torand.fastersql.Table;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.github.torand.fastersql.util.collection.CollectionHelper.asList;
import static org.github.torand.fastersql.util.contract.Requires.requireNonEmpty;

public class InsertBatchIntoBuilder<T> {
    private final List<? extends T> entities;

    InsertBatchIntoBuilder(Collection<? extends T> entities) {
        this.entities = asList(requireNonEmpty(entities, "No entities specified"));
    }

    public InsertBatchStatement<? extends T> into(Table table) {
        return new InsertBatchStatement<>(entities, requireNonNull(table, "No table specified"), null);
    }
}
