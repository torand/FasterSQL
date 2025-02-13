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
package io.github.torand.fastersql.constant;

import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.Field;
import io.github.torand.fastersql.projection.Projection;

import java.util.stream.Stream;

import static io.github.torand.fastersql.util.contract.Requires.requireNonBlank;
import static java.util.Objects.requireNonNull;

public class GenericConstant<T> implements Constant {
    private final T value;
    private final String alias;

    GenericConstant(T value, String alias) {
        this.value = requireNonNull(value, "Use NullConstant to represent 'null'");
        this.alias = alias;
    }

    // Sql

    @Override
    public String sql(Context context) {
        return "?";
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.of(value);
    }

    // Projection

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new GenericConstant(value, alias);
    }

    @Override
    public String alias() {
        return alias;
    }

    // Expression

    @Override
    public Stream<Field> fieldRefs() {
        return Stream.empty();
    }

    // Constant

    @Override
    public Object value() {
        return value;
    }

    @Override
    public Projection forField(Field field) {
        requireNonNull(field, "No field specified");
        return new GenericConstant(value, field.alias());
    }
}
