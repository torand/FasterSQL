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
package io.github.torand.fastersql.predicate.comparison;

import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.predicate.LeftOperand;

import static io.github.torand.fastersql.constant.Constants.$;

public final class ComparisonPredicates {
    private ComparisonPredicates() {}

    public static Eq eq(LeftOperand left, Object right) {
        return new Eq(left, $(right));
    }

    public static Eq eq(LeftOperand left, Expression right) {
        return new Eq(left, right);
    }

    public static Lt lt(LeftOperand left, Object right) {
        return new Lt(left, $(right));
    }

    public static Lt lt(LeftOperand left, Expression right) {
        return new Lt(left, right);
    }

    public static Le le(LeftOperand left, Object right) {
        return new Le(left, $(right));
    }

    public static Le le(LeftOperand left, Expression right) {
        return new Le(left, right);
    }

    public static Gt gt(LeftOperand left, Object right) {
        return new Gt(left, $(right));
    }

    public static Gt gt(LeftOperand left, Expression right) {
        return new Gt(left, right);
    }

    public static Ge ge(LeftOperand left, Object right) {
        return new Ge(left, $(right));
    }

    public static Ge ge(LeftOperand left, Expression right) {
        return new Ge(left, right);
    }
}
