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
package io.github.torand.fastersql.expression.logical;

import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.Field;
import io.github.torand.fastersql.expression.Expression;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static io.github.torand.fastersql.util.contract.Requires.requireNonEmpty;

public class Or implements Expression {
    private final List<Expression> operands;

    Or(Expression... operands) {
        this.operands = asList(requireNonEmpty(operands, "No operands specified"));
    }

    @Override
    public String sql(Context context) {
        return "(" + operands.stream().map(e -> e.sql(context)).collect(joining(" or ")) + ")";
    }

    @Override
    public String negatedSql(Context context) {
        return "not " + sql(context);
    }

    @Override
    public Stream<Object> params(Context context) {
        return operands.stream().flatMap(o -> o.params(context));
    }

    @Override
    public Stream<Field> fields() {
        return operands.stream().flatMap(Expression::fields);
    }
}