package org.github.torand.fastersql;

import org.github.torand.fastersql.expression.Expression;
import org.github.torand.fastersql.expression.LeftOperand;
import org.github.torand.fastersql.expression.comparison.ComparisonExpressions;
import org.github.torand.fastersql.order.Order;
import org.github.torand.fastersql.order.Orders;
import org.github.torand.fastersql.projection.Projection;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static org.github.torand.fastersql.Command.SELECT;
import static org.github.torand.fastersql.util.contract.Requires.requireNonBlank;

public class Field implements Projection, LeftOperand {
    private final Table<?> table;
    private final String name;
    private final String alias;

    public Field(Table<?> table, String name) {
        this.table = requireNonNull(table, "No table specified");
        this.name = requireNonBlank(name, "No name specified");
        this.alias = defaultAlias(table, name);
    }

    private Field(Table<?> table, String name, String alias) {
        this.table = requireNonNull(table, "No table specified");
        this.name = requireNonBlank(name, "No name specified");
        this.alias = requireNonBlank(alias, "No alias specified");
    }

    @Override
    public Projection as(String alias) {
        return new Field(table, name, alias);
    }

    @Override
    public Stream<Field> fields() {
        return Stream.of(this);
    }

    public Expression isNull() {
        return ComparisonExpressions.isNull(this);
    }

    public Join on(Field other) {
        return new Join(this, other);
    }

    public Order asc() {
        return Orders.asc(this);
    }

    public Order ascIf(boolean condition) {
        return condition ? asc() : desc();
    }

    public Order desc() {
        return Orders.desc(this);
    }

    public String name() {
        return name;
    }

    @Override
    public String alias() {
        return alias;
    }

    public Table<?> table() {
        return table;
    }

    @Override
    public String sql(Context context) {
        if (context.isCommand(SELECT)) {
            return table.alias() + "." + name;
        } else {
            return name;
        }
    }

    private String defaultAlias(Table<?> table, String name) {
        return (table.alias() + "_" + name).toUpperCase();
    }
}
