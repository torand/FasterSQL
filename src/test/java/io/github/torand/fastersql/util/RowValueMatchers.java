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
package io.github.torand.fastersql.util;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;

public final class RowValueMatchers {
    private RowValueMatchers() {}

    public static Matcher<?> isNumber(long number) {
        return is(BigDecimal.valueOf(number));
    }

    public static Matcher<?> isNumber(double number) {
        return is(BigDecimal.valueOf(number));
    }

    public static Matcher<?> isCloseTo(double number, double error) {
        return is(closeTo(BigDecimal.valueOf(number), BigDecimal.valueOf(error)));
    }

    public static Matcher<?> isTimestamp(LocalDateTime dateTime) {
        return is(Timestamp.valueOf(dateTime));
    }

    public static Matcher<?> isNull() {
        return is(CoreMatchers.nullValue());
    }
}
