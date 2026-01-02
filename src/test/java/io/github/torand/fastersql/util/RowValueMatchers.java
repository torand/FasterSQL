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
package io.github.torand.fastersql.util;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

public final class RowValueMatchers {
    private RowValueMatchers() {}

    public static Matcher<?> isInteger(int number) {
        return is(number);
    }

    public static Matcher<?> isLong(long number) {
        return is(number);
    }

    public static Matcher<?> isDouble(double number) {
        return is(number);
    }

    public static Matcher<?> isDoubleCloseTo(double number, double error) {
        return is(closeTo(number, error));
    }

    public static Matcher<?> isBigDecimal(long number) {
        return is(BigDecimal.valueOf(number));
    }

    public static Matcher<?> isBigDecimal(double number) {
        return is(BigDecimal.valueOf(number));
    }

    public static Matcher<?> isBigDecimalCloseTo(double number, double error) {
        return is(closeTo(BigDecimal.valueOf(number), BigDecimal.valueOf(error)));
    }

    public static Matcher<?> isTimestamp(LocalDateTime dateTime) {
        return is(Timestamp.valueOf(dateTime));
    }

    public static Matcher<?> isTimestamp(LocalDate date) {
        return is(allOf(greaterThanOrEqualTo(Timestamp.valueOf(date.atStartOfDay())), lessThan(Timestamp.valueOf(date.plusDays(1).atStartOfDay()))));
    }

    public static Matcher<?> isNull() {
        return is(CoreMatchers.nullValue());
    }
}
