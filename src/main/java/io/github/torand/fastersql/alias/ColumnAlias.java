/*
 * Copyright (c) 2024-2025 Tore Eide Andersen
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
package io.github.torand.fastersql.alias;

import io.github.torand.fastersql.Column;
import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.order.OrderExpression;
import io.github.torand.fastersql.predicate.LeftOperand;

import java.util.Random;
import java.util.stream.Stream;

import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;

/**
 * Represents an alias (label) for a projection
 */
public class ColumnAlias implements Alias, LeftOperand, OrderExpression {
    private final String name;

    public static ColumnAlias generate(String prefix) {
        return new ColumnAlias(requireNonBlank(prefix, "No prefix specified") + (new Random().nextInt(999) + 1));
    }

    public ColumnAlias(String name) {
        this.name = requireNonBlank(name, "No name specified");
    }

    @Override
    public String name() {
        return name;
    }

    // Sql

    @Override
    public String sql(Context context) {
        return name.contains(" ") ? "\"" + name + "\"" : name;
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.empty();
    }

    @Override
    public Stream<Column> columnRefs() {
        return Stream.empty();
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return Stream.of(this);
    }
}
