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
import static io.github.torand.fastersql.statement.Statements.insert;
import static org.assertj.core.api.Assertions.assertThat;

public class InsertStatementTest {
    private PreparableStatement statement;

    @BeforeEach
    public void setup() {
        statement =
            insert().into(PERSON)
                .value(PERSON.NAME, "Tore Torell")
                .value(PERSON.SSN, "17016812345");
    }

    @Test
    public void shouldBuildValidSql() {
        final String expectedSql =
                "insert into PERSON (NAME, SSN) "
              + "values (?, ?)";

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
                insert().into(ADDRESS)
                        .value(ADDRESS.PERSON_ID, 123)
                        .value(ADDRESS.STREET, "Tryllegata 14")
                        .value(ADDRESS.ZIP, "7089")
                        .value(ADDRESS.COUNTRY, "Noreg")
                        .value(ADDRESS.VERIFIED, currentTimestamp());

        final String expectedSql =
                "insert into ADDRESS (PERSON_ID, STREET, ZIP, COUNTRY, VERIFIED) "
              + "values (?, ?, ?, ?, current_timestamp)";
        Object[] expectedParams = {123, "Tryllegata 14", "7089", "Noreg"};

        assertThat(statement.sql(context())).isEqualTo(expectedSql);
        assertThat(statement.params(context())).contains(expectedParams);
    }

    @Test
    public void shouldAllowNullAsValue() {
        String zip = null;
        PreparableStatement statement =
                insert().into(ADDRESS)
                        .value(ADDRESS.PERSON_ID, 123)
                        .value(ADDRESS.STREET, "Tryllegata 14")
                        .value(ADDRESS.ZIP, zip)
                        .value(ADDRESS.COUNTRY, "Noreg")
                        .value(ADDRESS.VERIFIED, currentTimestamp());

        final String expectedSql =
                "insert into ADDRESS (PERSON_ID, STREET, ZIP, COUNTRY, VERIFIED) "
              + "values (?, ?, null, ?, current_timestamp)";
        Object[] expectedParams = {123, "Tryllegata 14", "Noreg"};

        assertThat(statement.sql(context())).isEqualTo(expectedSql);
        assertThat(statement.params(context())).contains(expectedParams);
    }

    private Context context() {
        return Context.of(new MySqlDialect());
    }
}
