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
package io.github.torand.fastersql.statement;

import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.sql.Context;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * A column-value pair used in INSERT batch statements.
 */
class ColumnValueExtractor<T> {
    private final Column column;
    private final Function<? super T, Object> valueExtractor;

    public ColumnValueExtractor(Column column, Function<? super T, Object> valueExtractor) {
        this.column = requireNonNull(column, "No column specified");
        this.valueExtractor = requireNonNull(valueExtractor, "No valueExtractor specified");
    }

    /**
     * Gets the column being populated.
     * @return the column being populated.
     */
    Column column() {
        return column;
    }

    /**
     * Gets the statement parameter introduced by the extracted column value.
     * @param entity the entity to extract value from.
     * @return the statement parameter.
     */
    Optional<Object> valueParam(T entity) {
        return Optional.ofNullable(extractValue(entity));
    }

    /**
     * Formats extracted column value as an SQL fragment.
     * @param context the context (incl. dialect).
     * @param entity the entity to extract value from.
     * @return the formatted SQL fragment.
     */
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