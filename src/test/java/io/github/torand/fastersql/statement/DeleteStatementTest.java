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

import static io.github.torand.fastersql.datamodel.TestDataModel.PERSON;
import static io.github.torand.fastersql.statement.Statements.delete;
import static org.assertj.core.api.Assertions.assertThat;

public class DeleteStatementTest {
    private PreparableStatement statement;

    @BeforeEach
    public void setup() {
        statement =
            delete().from(PERSON)
                .where(PERSON.SSN.eq("17016812345"), PERSON.NAME.isNull());
    }

    @Test
    public void shouldBuildValidSql() {
        final String expectedSql =
                "delete from PERSON "
              + "where SSN = ? "
              + "and NAME is null";

        assertThat(statement.sql(context())).isEqualTo(expectedSql);
    }

    @Test
    public void shouldCollectArgsInCorrectOrder() {
        Object[] expectedArgs = {"17016812345"};
        assertThat(statement.params(context())).contains(expectedArgs);
    }

    private Context context() {
        return Context.of(new MySqlDialect());
    }
}
