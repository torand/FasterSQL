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
import io.github.torand.fastersql.dialect.OracleDialect;
import io.github.torand.fastersql.domainmodel.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static java.util.Arrays.asList;
import static io.github.torand.fastersql.datamodel.TestDataModel.PERSON;
import static io.github.torand.fastersql.statement.Statements.insertBatch;
import static org.assertj.core.api.Assertions.assertThat;

public class InsertBatchStatementTest {
    private PreparableStatement statement;

    @BeforeEach
    public void setup() {
        Collection<Person> persons = asList(
                new Person(1, "Ole", "11111111111", Optional.of("123 45 678")),
                new Person(2, "Dole", "22222222222", Optional.empty()),
                new Person(3, "Doffen", null, Optional.of("345 67 890")));

        statement =
            insertBatch(persons).into(PERSON)
                .value(PERSON.ID, Person::getId)
                .value(PERSON.NAME, Person::getName)
                .value(PERSON.SSN, Person::getSsn)
                .value(PERSON.PHONE_NO, Person::getPhoneNo);
    }

    @Test
    public void shouldBuildValidSqlForOracle() {
        final String expectedSql =
                "insert all"
                 + " into PERSON (ID, NAME, SSN, PHONE_NO) values (?, ?, ?, ?)"
                 + " into PERSON (ID, NAME, SSN, PHONE_NO) values (?, ?, ?, null)"
                 + " into PERSON (ID, NAME, SSN, PHONE_NO) values (?, ?, null, ?)"
              + " select 1 from DUAL";

        Context oracleContext = Context.of(new OracleDialect());
        assertThat(statement.sql(oracleContext)).isEqualTo(expectedSql);
    }

    @Test
    public void shouldBuildValidSqlForNonOracle() {
        final String expectedSql =
                "insert into PERSON (ID, NAME, SSN, PHONE_NO) "
              + "values (?, ?, ?, ?), (?, ?, ?, null), (?, ?, null, ?)";

        assertThat(statement.sql(context())).isEqualTo(expectedSql);
    }

    @Test
    public void shouldCollectArgsInCorrectOrder() {
        Object[] expectedArgs = {1, "Ole", "11111111111", "123 45 678", 2, "Dole", "22222222222", 3, "Doffen", "345 67 890"};
        assertThat(statement.params(context())).contains(expectedArgs);
    }

    private Context context() {
        return Context.of(new MySqlDialect());
    }
}
