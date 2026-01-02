/*
 * Copyright (c) 2024-2026 Tore Eide Andersen
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

import static java.util.Objects.isNull;

/**
 * Provides factory methods for constants.
 */
public final class Constants {

    private Constants() {}

    /**
     * Creates a scalar value constant.
     * @param value the constant value.
     * @return the constant.
     * @param <T> the constant type.
     */
    public static <T> Constant $(T value) {
        if (isNull(value)) {
            return nullValue();
        }

        return new GenericConstant<>(value, null);
    }

    /**
     * Creates a string inline constant.
     * @param value the constant value.
     * @return the constant.
     */
    public static Constant $i(String value) {
        if (isNull(value)) {
            return nullValue();
        }

        return new InlineStringConstant(value, null);
    }

    /**
     * Creates a numeric inline constant.
     * @param value the constant value.
     * @return the constant.
     */
    public static Constant $i(Number value) {
        if (isNull(value)) {
            return nullValue();
        }

        return new InlineNumberConstant(value, null);
    }

    /**
     * Creates a scalar value constant.
     * @param value the constant value.
     * @return the constant.
     * @param <T> the constant type.
     */
    public static <T> Constant constant(T value) {
        if (isNull(value)) {
            return nullValue();
        }

        return new GenericConstant<>(value, null);
    }

    /**
     * Creates a null constant.
     * @return the constant.
     */
    public static Constant nullValue() {
        return new NullConstant(null);
    }
}
