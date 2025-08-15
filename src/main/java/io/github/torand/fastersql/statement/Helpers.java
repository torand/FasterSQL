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

import static io.github.torand.javacommons.stream.StreamHelper.streamSafely;
import static java.util.stream.Collectors.joining;

/**
 * Provides statement utility functions.
 */
public final class Helpers {
    private Helpers() {}

    /**
     * Generates a string with comma separated statement parameter markers.
     * @param count the number of parameter markers.
     * @return the generated parameter marker string.
     */
    public static String paramMarkers(long count) {
        return Stream.generate(() -> "?").limit(count).collect(joining(", "));
    }

    /**
     * Returns the wrapped object if specified object is an optional, else returns the object itself.
     * @param obj the object.
     * @return the unwrapped object.
     */
    public static Object unwrapOptional(Object obj) {
        if (obj instanceof Optional<?> opt) {
            return opt.orElse(null);
        } else {
            return obj;
        }
    }

    /**
     * Returns a collection of non-null values produces by the specified suppliers.
     * @param suppliers the suppliers.
     * @return the collection of supplied values.
     * @param <T> the supplier value type.
     */
    @SafeVarargs
    public static <T> Collection<T> unwrapSuppliers(Supplier<T>... suppliers) {
        return streamSafely(suppliers).map(Supplier::get).filter(Objects::nonNull).toList();
    }
}
