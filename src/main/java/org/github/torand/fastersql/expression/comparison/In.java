package org.github.torand.fastersql.expression.comparison;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.expression.Expression;
import org.github.torand.fastersql.expression.LeftOperand;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static org.github.torand.fastersql.statement.Helpers.paramMarkers;
import static org.github.torand.fastersql.util.collection.CollectionHelper.asList;
import static org.github.torand.fastersql.util.contract.Requires.requireNonEmpty;

public class In implements Expression {
    private final LeftOperand operand;
    private final List<?> values;

    In(LeftOperand operand, Collection<?> values) {
        this.operand = requireNonNull(operand, "No operand specified");
        this.values = asList(requireNonEmpty(values, "No values specified"));
    }

    @Override
    public String sql(Context context) {
        return operand.sql(context) + " in (" + paramMarkers(values.size()) + ")";
    }

    @Override
    public String negatedSql(Context context) {
        return operand.sql(context) + " not in (" + paramMarkers(values.size()) + ")";
    }

    @Override
    public Stream<Object> params(Context context) {
        return values.stream().map(v -> v);
    }

    @Override
    public Stream<Field> fields() {
        return operand.fields();
    }
}
