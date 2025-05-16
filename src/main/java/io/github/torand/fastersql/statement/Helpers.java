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

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.github.torand.fastersql.util.collection.CollectionHelper.streamSafely;
import static java.util.stream.Collectors.joining;

/**
 * Provides statement utility functions.
 */
public final class Helpers {
    private Helpers() {}

    public static String paramMarkers(long count) {
        return Stream.generate(() -> "?").limit(count).collect(joining(", "));
    }

    public static Object unwrapOptional(Object obj) {
        if (obj instanceof Optional<?> opt) {
            return opt.orElse(null);
        } else {
            return obj;
        }
    }

    @SafeVarargs
    public static <T> Collection<T> unwrapSuppliers(Supplier<T>... suppliers) {
        return streamSafely(suppliers).map(Supplier::get).filter(Objects::nonNull).toList();
    }
}
