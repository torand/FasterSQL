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
package io.github.torand.fastersql.expression.cases;

import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.predicate.Predicate;

import java.util.ArrayList;
import java.util.List;

import static io.github.torand.fastersql.constant.Constants.$;
import static java.util.Objects.requireNonNull;

/**
 * Builder of searched CASE expressions.
 */
public class SearchedCaseBuilder {
    private final List<SearchedWhenThen> whenThenClauses = new ArrayList<>();
    private Expression elseExpression;

    SearchedCaseBuilder() {
    }

    /**
     * Creates a WHEN-THEN clause.
     * @param whenPredicate the WHEN predicate.
     * @return the WHEN-THEN builder.
     */
    public SearchedWhenThenBuilder when(Predicate whenPredicate) {
        requireNonNull(whenPredicate, "No when predicate specified");
        return new SearchedWhenThenBuilder(this, whenPredicate);
    }

    /**
     * Adds an ELSE clause.
     * @param elseExpression the ELSE expression.
     * @return the modified CASE expression.
     */
    public SearchedCaseBuilder else_(Expression elseExpression) {
        this.elseExpression = requireNonNull(elseExpression, "No else expression specified");
        return this;
    }

    /**
     * Adds an ELSE clause.
     * @param elseConstant the ELSE constant value.
     * @return the modified CASE expression.
     */
    public SearchedCaseBuilder else_(String elseConstant) {
        requireNonNull(elseConstant, "No else constant specified");
        return else_($(elseConstant));
    }

    /**
     * Adds an ELSE clause.
     * @param elseConstant the ELSE constant value.
     * @return the modified CASE expression.
     */
    public SearchedCaseBuilder else_(Number elseConstant) {
        requireNonNull(elseConstant, "No else constant specified");
        return else_($(elseConstant));
    }

    /**
     * Creates the searched CASE expression.
     * @return the searched CASE expression.
     */
    public SearchedCase end() {
        return new SearchedCase(whenThenClauses, elseExpression, null);
    }

    void addWhenThenClause(SearchedWhenThen whenThenClause) {
        requireNonNull(whenThenClause, "No when-then clause specified");
        this.whenThenClauses.add(whenThenClause);
    }
}
