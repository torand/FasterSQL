package org.github.torand.fastersql.expression;

import org.github.torand.fastersql.expression.logical.LogicalExpressions;

import java.util.Collection;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static org.github.torand.fastersql.util.collection.CollectionHelper.streamSafely;
import static org.github.torand.fastersql.util.contract.Requires.precondition;

public class OptionalExpression {
    private final Expression expression;

    public static OptionalExpression of(Expression expression) {
        requireNonNull(expression, "expression");
        return new OptionalExpression(expression);
    }

    public static OptionalExpression ofNullable(Expression expression) {
        return new OptionalExpression(expression);
    }

    public static OptionalExpression empty() {
        return new OptionalExpression(null);
    }

    private OptionalExpression(Expression expression) {
        this.expression = expression;
    }

    public boolean isPresent() {
        return nonNull(expression);
    }

    public Expression get() {
        precondition(() -> nonNull(expression), "Expression is not present");
        return expression;
    }

    public Stream<Expression> stream() {
        return nonNull(expression) ? Stream.of(expression) : Stream.empty();
    }

    public OptionalExpression or(Expression other) {
        requireNonNull(other, "other");

        if (nonNull(this.expression)) {
            return new OptionalExpression(LogicalExpressions.or(this.expression, other));
        } else {
            return new OptionalExpression(other);
        }
    }

    public OptionalExpression or(OptionalExpression other) {
        if (nonNull(this.expression) && nonNull(other.expression)) {
            return new OptionalExpression(LogicalExpressions.or(this.expression, other.expression));
        } else if (nonNull(this.expression)) {
            return new OptionalExpression(this.expression);
        } else if (nonNull(other.expression)) {
            return new OptionalExpression(other.expression);
        } else {
            return new OptionalExpression(null);
        }
    }

    public OptionalExpression and(OptionalExpression other) {
        if (nonNull(this.expression) && nonNull(other.expression)) {
            return new OptionalExpression(LogicalExpressions.and(this.expression, other.expression));
        } else if (nonNull(this.expression)) {
            return new OptionalExpression(this.expression);
        } else if (nonNull(other.expression)) {
            return new OptionalExpression(other.expression);
        } else {
            return new OptionalExpression(null);
        }
    }

    public OptionalExpression and(Expression other) {
        requireNonNull(other, "other");

        if (nonNull(this.expression)) {
            return new OptionalExpression(LogicalExpressions.and(this.expression, other));
        } else {
            return new OptionalExpression(other);
        }
    }

    public static Collection<Expression> unwrap(OptionalExpression... maybeExpressions) {
        return streamSafely(maybeExpressions).flatMap(OptionalExpression::stream).toList();
    }
}
