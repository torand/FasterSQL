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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

import static java.util.Objects.isNull;

public final class Constants {

    private Constants() {}

    public static Constant constant(Object value) {
        if (isNull(value)) {
            return nullValue();
        }

        if (value instanceof String string) {
            return constant(string);
        }

        if (value instanceof Integer integer) {
            return constant(Long.valueOf(integer));
        }

        if (value instanceof Long longValue) {
            return constant(longValue);
        }

        if (value instanceof BigInteger biginteger) {
            return constant(biginteger.longValue());
        }

        if (value instanceof Float floatValue) {
            return constant(Double.valueOf(floatValue));
        }

        if (value instanceof Double doubleValue) {
            return constant(doubleValue);
        }

        if (value instanceof BigDecimal bigdecimal) {
            return constant(bigdecimal.doubleValue());
        }

        if (value instanceof UUID uuid) {
            return constant(uuid);
        }

        if (value instanceof Enum enumValue) {
            return constant(enumValue);
        }

        throw new IllegalArgumentException("Unsupported constant type: " + value.getClass());
    }

    public static Constant constant(UUID value) {
        if (isNull(value)) {
            return nullValue();
        }
        return new StringConstant(value.toString(), null);
    }

    public static Constant constant(Enum<? extends Enum<?>> value) {
        if (isNull(value)) {
            return nullValue();
        }
        return new StringConstant(value.name(), null);
    }

    public static Constant constant(String value) {
        if (isNull(value)) {
            return nullValue();
        }
        return new StringConstant(value, null);
    }

    public static Constant constant(Long value) {
        if (isNull(value)) {
            return nullValue();
        }
        return new IntegerConstant(value, null);
    }

    public static Constant constant(Double value) {
        if (isNull(value)) {
            return nullValue();
        }
        return new DecimalConstant(value, null);
    }

    public static Constant nullValue() {
        return new NullConstant(null);
    }
}
