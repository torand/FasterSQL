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
package io.github.torand.fastersql.function.singlerow.cast;

import io.github.torand.fastersql.alias.ColumnAlias;
import io.github.torand.fastersql.expression.Expression;
import io.github.torand.fastersql.function.singlerow.SingleRowFunction;
import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.projection.Projection;
import io.github.torand.fastersql.sql.Context;

import java.util.Optional;
import java.util.stream.Stream;

import static io.github.torand.javacommons.contract.Requires.requireNonBlank;
import static io.github.torand.javacommons.lang.StringHelper.nonBlank;

/**
 * Implements the cast function.
 */
public class Cast implements SingleRowFunction {
    private final Expression operand;
    private final DataType targetType;
    private final ColumnAlias alias;

    Cast(Expression operand, DataType targetType, String alias) {
        this.operand = operand;
        this.targetType = targetType;
        this.alias = nonBlank(alias) ? new ColumnAlias(alias) : defaultAlias();
    }

    // Sql

    @Override
    public String sql(Context context) {
        String operandSql = operand.sql(context);
        return context.getDialect().formatCastFunction(operandSql, targetType);
    }

    @Override
    public Stream<Object> params(Context context) {
        return operand.params(context);
    }

    @Override
    public Stream<Column> columnRefs() {
        return operand.columnRefs();
    }

    @Override
    public Stream<ColumnAlias> aliasRefs() {
        return operand.aliasRefs();
    }

    // Projection

    @Override
    public Projection as(String alias) {
        requireNonBlank(alias, "No alias specified");
        return new Cast(operand, targetType, alias);
    }

    @Override
    public Optional<ColumnAlias> alias() {
        return Optional.ofNullable(alias);
    }

    private ColumnAlias defaultAlias() {
        return ColumnAlias.generate("CAST_");
    }
}
