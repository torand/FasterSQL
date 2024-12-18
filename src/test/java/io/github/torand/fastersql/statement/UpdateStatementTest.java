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
package io.github.torand.fastersql.statement;

import io.github.torand.fastersql.Context;
import io.github.torand.fastersql.dialect.MySqlDialect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.github.torand.fastersql.datamodel.TestDataModel.ADDRESS;
import static io.github.torand.fastersql.datamodel.TestDataModel.PERSON;
import static io.github.torand.fastersql.function.system.SystemFunctions.currentTimestamp;
import static io.github.torand.fastersql.statement.Statements.update;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdateStatementTest {
    private PreparableStatement statement;

    @BeforeEach
    public void setup() {
        statement =
            update(PERSON)
                .set(PERSON.NAME, "Tore Torell")
                .where(PERSON.SSN.eq("17016812345")
                        .and(PERSON.NAME.isNull()));
    }

    @Test
    public void shouldBuildValidSql() {
        final String expectedSql =
                "update PERSON "
              + "set NAME = ? "
              + "where SSN = ? "
              + "and NAME is null";

        assertThat(statement.sql(context())).isEqualTo(expectedSql);
    }

    @Test
    public void shouldCollectArgsInCorrectOrder() {
        Object[] expectedArgs = {"Tore Torell", "17016812345"};
        assertThat(statement.params(context())).contains(expectedArgs);
    }

    @Test
    public void shouldAllowSystemFunctionAsValue() {
        PreparableStatement statement =
                update(ADDRESS)
                        .set(ADDRESS.ZIP, "7089")
                        .set(ADDRESS.VERIFIED, currentTimestamp())
                        .where(ADDRESS.ZIP.eq("7088"));

        final String expectedSql =
                "update ADDRESS "
                        + "set ZIP = ?, "
                        + "VERIFIED = current_timestamp "
                        + "where ZIP = ?";
        Object[] expectedParams = {"7089", "7088"};

        assertThat(statement.sql(context())).isEqualTo(expectedSql);
        assertThat(statement.params(context())).contains(expectedParams);
    }

    @Test
    public void shouldAllowNullValues() {
        String zip = null;
        PreparableStatement statement =
                update(ADDRESS)
                        .set(ADDRESS.ZIP, zip)
                        .where(ADDRESS.ZIP.eq("7088"));

        final String expectedSql =
                "update ADDRESS "
                        + "set ZIP = null "
                        + "where ZIP = ?";
        Object[] expectedParams = {"7088"};

        assertThat(statement.sql(context())).isEqualTo(expectedSql);
        assertThat(statement.params(context())).contains(expectedParams);
    }

    private Context context() {
        return Context.of(new MySqlDialect());
    }
}
