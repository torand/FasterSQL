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
package io.github.torand.fastersql.util.functional;

import java.util.function.Function;

/**
 * General purpose functions for streams.
 */
public final class Functions {
    private Functions() {}

    /**
     * Returns a function casting the input object to the specified class.
     * @param targetClass the class to cast to
     * @param <S> the input object type
     * @param <T> the output object type
     * @return the function object
     */
    public static <S, T> Function<S,T> castTo(Class<T> targetClass) {
        return targetClass::cast;
    }
}
