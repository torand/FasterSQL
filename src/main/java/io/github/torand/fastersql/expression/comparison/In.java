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

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static io.github.torand.fastersql.statement.Helpers.paramMarkers;
import static io.github.torand.fastersql.util.collection.CollectionHelper.asList;
import static io.github.torand.fastersql.util.contract.Requires.requireNonEmpty;

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
