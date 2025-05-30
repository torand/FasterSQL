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
package io.github.torand.fastersql.util.lang;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Provides general purpose string utilities.
 */
public final class StringHelper {
    private StringHelper() {}

    public static boolean nonBlank(String string) {
        return nonNull(string) && !string.isEmpty();
    }

    public static boolean isBlank(String string) {
        return isNull(string) || string.isEmpty();
    }

    public static String generate(String s, int count) {
        return s.repeat(count);
    }

    public static String generate(String s, int count, String delimiter) {
        StringBuilder b = new StringBuilder();
        for (int t = 0; t < count; t++) {
            if (t != 0) {
                b.append(delimiter);
            }
            b.append(s);
        }
        return b.toString();
    }

    public static String quote(String string) {
        return "\"" + string + "\"";
    }

    public static Object quoteIfString(Object value) {
        if (value instanceof String string) {
            return quote(string);
        }

        return value;
    }
}
