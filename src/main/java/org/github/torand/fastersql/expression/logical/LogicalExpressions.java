package org.github.torand.fastersql.expression.logical;

import org.github.torand.fastersql.expression.Expression;

public final class LogicalExpressions {
    private LogicalExpressions() {}

    public static And and(Expression... operands) {
        return new And(operands);
    }

    public static Or or(Expression... operands) {
        return new Or(operands);
    }

    public static Not not(Expression operand) {
        return new Not(operand);
    }
}
