/*
 * Copyright (c) 2024 Tore Eide Andersen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.torand.fastersql.expression;

import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.Field;
import io.github.torand.fastersql.Sql;
import io.github.torand.fastersql.expression.logical.LogicalExpressions;

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
