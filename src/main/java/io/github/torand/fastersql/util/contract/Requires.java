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
package io.github.torand.fastersql.util.contract;

import io.github.torand.fastersql.util.collection.ArrayHelper;
import io.github.torand.fastersql.util.collection.CollectionHelper;

import java.util.Collection;
import java.util.Optional;

import static io.github.torand.fastersql.util.lang.StringHelper.nonBlank;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public final class Requires {

    private Requires() {}

    public static int[] requireNonEmpty(int[] array, String msg, Object... args) {
        requireNonNull(array, String.format(msg, args));
        require(() -> ArrayHelper.nonEmpty(array), msg, args);
        return array;
    }

    public static long[] requireNonEmpty(long[] array, String msg, Object... args) {
        requireNonNull(array, String.format(msg, args));
        require(() -> ArrayHelper.nonEmpty(array), msg, args);
        return array;
    }

    public static <T> T[] requireNonEmpty(T[] array, String msg, Object... args) {
        requireNonNull(array, String.format(msg, args));
        require(() -> ArrayHelper.nonEmpty(array), msg, args);
        return array;
    }

    public static <T extends Collection<?>> T requireNonEmpty(T collection, String msg, Object... args) {
        requireNonNull(collection, String.format(msg, args));
        require(() -> CollectionHelper.nonEmpty(collection), msg, args);
        return collection;
    }

    public static String requireNonBlank(String string, String msg, Object... args) {
        requireNonNull(string, String.format(msg, args));
        require(() -> nonBlank(string), msg, args);
        return string;
    }

    public static <T> Optional<T> requireNonEmpty(Optional<T> optional, String msg, Object... args) {
        require(() -> nonNull(optional) && optional.isPresent(), msg, args);
        return optional;
    }

    public static void require(Requirement requirement, String msg, Object... args) {
        if (!requirement.test()) {
            throw new IllegalArgumentException(String.format(msg, args));
        }
    }

    public static void precondition(Requirement requirement, String msg, Object... args) {
        if (!requirement.test()) {
            throw new IllegalStateException(String.format(msg, args));
        }
    }
}
