package io.github.torand.fastersql.alias;

import io.github.torand.fastersql.Column;
import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.order.OrderExpression;
import io.github.torand.fastersql.predicate.LeftOperand;

import java.util.Random;
import java.util.stream.Stream;

import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;

/**
 * Represents an alias (label) for a projection
 */
public class ColumnAlias implements Alias, LeftOperand, OrderExpression {
    private final String name;

    public static ColumnAlias generate(String prefix) {
        return new ColumnAlias(requireNonBlank(prefix, "No prefix specified") + (new Random().nextInt(999) + 1));
    }

    public ColumnAlias(String name) {
        this.name = requireNonBlank(name, "No name specified");
    }

    @Override
    public String name() {
        return name;
    }

    // Sql

    @Override
    public String sql(Context context) {
        return name.contains(" ") ? "\"" + name + "\"" : name;
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.empty();
    }

    @Override
    public Stream<Column> columnRefs() {
        return Stream.empty();
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return Stream.of(this);
    }
}
