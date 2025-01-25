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
package io.github.torand.fastersql.expression.arithmetic;

import io.github.torand.fastersql.expression.Expression;

public final class ArithmeticExpressions {
    private ArithmeticExpressions() {}

    public static Plus plus(Expression first, Expression second) {
        return new Plus(first, second, null);
    }

    public static Minus minus(Expression first, Expression second) {
        return new Minus(first, second, null);
    }

    public static Times times(Expression first, Expression second) {
        return new Times(first, second, null);
    }

    public static DivideBy divideBy(Expression first, Expression second) {
        return new DivideBy(first, second, null);
    }
}
