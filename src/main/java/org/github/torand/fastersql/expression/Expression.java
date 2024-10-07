package org.github.torand.fastersql.expression;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.Sql;
import org.github.torand.fastersql.expression.logical.LogicalExpressions;

import java.util.stream.Stream;

public interface Expression extends Sql {
    String negatedSql(Context context);

    Stream<Field> fields();

    Stream<Object> params(Context context);

    default Expression or(Expression other) {
        return LogicalExpressions.or(this, other);
    }

    default OptionalExpression or(OptionalExpression maybeOther) {
        if (maybeOther.isPresent()) {
            return OptionalExpression.of(LogicalExpressions.or(this, maybeOther.get()));
        } else {
            return OptionalExpression.of(this);
        }
    }

    default Expression and(Expression other) {
        return LogicalExpressions.and(this, other);
    }

    default OptionalExpression and(OptionalExpression maybeOther) {
        if (maybeOther.isPresent()) {
            return OptionalExpression.of(LogicalExpressions.and(this, maybeOther.get()));
        } else {
            return OptionalExpression.of(this);
        }
    }
}
