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
package io.github.torand.fastersql.condition.comparison;

import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.Field;
import io.github.torand.fastersql.condition.Condition;
import io.github.torand.fastersql.condition.LeftOperand;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class EqField implements Condition {
    private final LeftOperand left;
    private final Field right;

    EqField(LeftOperand left, Field right) {
        this.left = requireNonNull(left, "No left operand specified");
        this.right = requireNonNull(right, "No right operand specified");
    }

    @Override
    public String sql(Context context) {
        return left.sql(context) + " = " + right.sql(context);
    }

    @Override
    public String negatedSql(Context context) {
        return left.sql(context) + " != " + right.sql(context);
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.empty();
    }

    @Override
    public Stream<Field> fields() {
        return Stream.concat(left.fields(), Stream.of(right));
    }
}
