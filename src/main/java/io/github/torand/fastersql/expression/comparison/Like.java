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
package io.github.torand.fastersql.expression.comparison;

import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.Field;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.expression.LeftOperand;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class Like implements Expression {
    private final LeftOperand operand;
    private final String pattern;

    Like(LeftOperand operand, String pattern) {
        this.operand = requireNonNull(operand, "No operand specified");

        requireNonNull(pattern, "No pattern specified");
        this.pattern = pattern.contains("%") ? pattern : "%" + pattern + "%";
    }

    @Override
    public String sql(Context context) {
        return operand.sql(context) + " like ?";
    }

    @Override
    public String negatedSql(Context context) {
        return operand.sql(context) + " not like ?";
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.of(pattern);
    }

    @Override
    public Stream<Field> fields() {
        return operand.fields();
    }
}
