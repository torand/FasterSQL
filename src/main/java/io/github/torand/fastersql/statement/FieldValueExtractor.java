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
package io.github.torand.fastersql.statement;

import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.Field;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * A field-value pair used in INSERT batch statements.
 */
class FieldValueExtractor<T> {
    private final Field field;
    private final Function<? super T, Object> valueExtractor;

    public FieldValueExtractor(Field field, Function<? super T, Object> valueExtractor) {
        this.field = requireNonNull(field, "No field specified");
        this.valueExtractor = requireNonNull(valueExtractor, "No valueExtractor specified");
    }

    Field field() {
        return field;
    }

    Optional<Object> param(T entity) {
        return Optional.ofNullable(extractValue(entity));
    }

    String valueSql(Context context, T entity) {
        Object value = extractValue(entity);
        if (isNull(value)) {
            return "null";
        } else {
            return "?";
        }
    }

    private Object extractValue(T entity) {
        return valueExtractor.andThen(Helpers::unwrapOptional).apply(entity);
    }
}