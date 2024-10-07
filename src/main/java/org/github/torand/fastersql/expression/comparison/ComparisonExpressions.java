package org.github.torand.fastersql.expression.comparison;

import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.expression.LeftOperand;
import org.github.torand.fastersql.subquery.Subquery;

import java.util.Collection;

public final class ComparisonExpressions {
    private ComparisonExpressions() {}

    public static Eq eq(LeftOperand left, Object right) {
        return new Eq(left, right);
    }

    public static EqField eq(LeftOperand left, Field right) {
        return new EqField(left, right);
    }

    public static EqSubquery eq(LeftOperand left, Subquery right) {
        return new EqSubquery(left, right);
    }

    public static Lt lt(LeftOperand left, Object right) {
        return new Lt(left, right);
    }

    public static LtField lt(LeftOperand left, Field right) {
        return new LtField(left, right);
    }

    public static Le le(LeftOperand left, Object right) {
        return new Le(left, right);
    }

    public static LeField le(LeftOperand left, Field right) {
        return new LeField(left, right);
    }

    public static Gt gt(LeftOperand left, Object right) {
        return new Gt(left, right);
    }

    public static GtField gt(LeftOperand left, Field right) {
        return new GtField(left, right);
    }

    public static Ge ge(LeftOperand left, Object right) {
        return new Ge(left, right);
    }

    public static GeField ge(LeftOperand left, Field right) {
        return new GeField(left, right);
    }

    public static In in(LeftOperand left, Collection<?> right) {
        return new In(left, right);
    }

    public static Like like(LeftOperand left, String right) {
        return new Like(left, right);
    }

    public static IsNull isNull(Field operand) {
        return new IsNull(operand);
    }
}
