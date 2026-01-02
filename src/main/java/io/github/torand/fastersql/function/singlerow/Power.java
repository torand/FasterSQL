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
package io.github.torand.fastersql.function.singlerow;

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.expression.arithmetic.Addition;
import io.github.torand.fastersql.expression.arithmetic.Subtraction;
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.projection.Projection;
import io.github.torand.fastersql.sql.Context;

import java.util.Optional;
import java.util.stream.Stream;

import static io.github.torand.fastersql.dialect.Capability.POWER_OPERATOR;
import static io.github.torand.javacommons.contract.Requires.requireNonBlank;
import static io.github.torand.javacommons.lang.StringHelper.nonBlank;
import static java.util.Objects.requireNonNull;

/**
 * Implements the power (exponentiation) numeric function.
 */
public class Power implements SingleRowFunction {
    private final Expression base;
    private final Expression exponent;
    private final ColumnAlias alias;

    Power(Expression base, Expression exponent, String alias) {
        this.base = requireNonNull(base, "No base expression specified");
        this.exponent = requireNonNull(exponent, "No exponent expression specified");
        this.alias = nonBlank(alias) ? new ColumnAlias(alias) : defaultAlias();
    }

    // Sql

    @Override
    public String sql(Context context) {
        if (context.getDialect().supports(POWER_OPERATOR)) {
            String baseSql = base.sql(context);
            if (base instanceof Addition || base instanceof Subtraction) {
                baseSql = "(" + baseSql + ")";
            }

            String exponentSql = exponent.sql(context);
            if (exponent instanceof Addition || exponent instanceof Subtraction) {
                exponentSql = "(" + exponentSql + ")";
            }

            return "(" + baseSql + " ^ " + exponentSql + ")";
        }

        return context.getDialect().formatPowerFunction(base.sql(context), exponent.sql(context));
    }

    @Override
    public Stream<Object> params(Context context) {
        return Stream.concat(base.params(context), exponent.params(context));
    }

    @Override
    public Stream<Column> columnRefs() {
        return Stream.concat(base.columnRefs(), exponent.columnRefs());
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return Stream.concat(base.aliasRefs(), exponent.aliasRefs());
    }

    // Projection

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new Power(base, exponent, alias);
    }

    @Override
    public Optional<ColumnAlias> alias() {
        return Optional.ofNullable(alias);
    }

    private ColumnAlias defaultAlias() {
        return ColumnAlias.generate("POWER_");
    }
}
