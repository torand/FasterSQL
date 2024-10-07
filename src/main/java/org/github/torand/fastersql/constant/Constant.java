package org.github.torand.fastersql.constant;

import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.projection.Projection;

public interface Constant extends Projection {
    Projection forField(Field field);

    Object value();
}
