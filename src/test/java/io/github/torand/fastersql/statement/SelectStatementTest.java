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
import io.github.torand.fastersql.datamodel.PersonTable;
import io.github.torand.fastersql.dialect.MySqlDialect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static io.github.torand.fastersql.condition.logical.LogicalConditions.not;
import static io.github.torand.fastersql.constant.Constants.constant;
import static io.github.torand.fastersql.constant.Constants.nullValue;
import static io.github.torand.fastersql.datamodel.TestDataModel.ADDRESS;
import static io.github.torand.fastersql.datamodel.TestDataModel.PERSON;
import static io.github.torand.fastersql.function.aggregate.Aggregates.countAll;
import static io.github.torand.fastersql.function.aggregate.Aggregates.max;
import static io.github.torand.fastersql.function.singlerow.SingleRowFunctions.upper;
import static io.github.torand.fastersql.statement.Statements.select;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class SelectStatementTest {
    private PreparableStatement statement;

    @BeforeEach
    public void setup() {
        statement =
            select(upper(PERSON.NAME), PERSON.SSN, ADDRESS.STREET, ADDRESS.ZIP, nullValue().forField(ADDRESS.COUNTRY))
                .from(PERSON)
                .join(PERSON.ID.on(ADDRESS.PERSON_ID).leftOuter())
                .where(PERSON.SSN.eq("17016812345")
                    .and(not(PERSON.NAME.isNull()))
                    .and(ADDRESS.ZIP.eq("7089").or(ADDRESS.ZIP.eq("7088")))
                    .and(ADDRESS.COUNTRY.in("NOR", "SWE", "DEN")))
                .orderBy(PERSON.SSN.asc(), PERSON.NAME.desc())
                .limit(10)
                .offset(90);
    }

    @Test
    public void shouldBuildValidSql() {
        final String expectedSql = """
            select upper(P.NAME) UPPER_P_NAME, P.SSN P_SSN, A.STREET A_STREET, A.ZIP A_ZIP, null A_COUNTRY \
            from PERSON P \
            left outer join ADDRESS A on P.ID = A.PERSON_ID \
            where P.SSN = ? \
            and P.NAME is not null \
            and (A.ZIP = ? or A.ZIP = ?) \
            and A.COUNTRY in (?, ?, ?) \
            order by P_SSN asc, P_NAME desc \
            limit ? \
            offset ?""";

        assertThat(statement.sql(context())).isEqualTo(expectedSql);
    }

    @Test
    public void shouldCollectArgsInCorrectOrder() {
        Object[] expectedParams = {"17016812345", "7089", "7088", "NOR", "SWE", "DEN", 10L, 90L};
        assertThat(statement.params(context())).contains(expectedParams);
    }

    @Test
    public void shouldHandleOptionalWhereClauses() {
        Optional<String> maybeName = Optional.of("Tore");
        Optional<String> maybeSsn = Optional.empty();

        PreparableStatement statement =
            select(PERSON.NAME)
                .from(PERSON)
                .where(PERSON.NAME.eq(maybeName), PERSON.SSN.eq(maybeSsn));

        final String expectedSql = """
            select P.NAME P_NAME \
            from PERSON P \
            where P.NAME = ?""";
        Object[] expectedParams = {"Tore"};

        assertThat(statement.sql(context())).isEqualTo(expectedSql);
        assertThat(statement.params(context())).contains(expectedParams);
    }

    @Test
    public void shouldHandleSubqueriesInConditions() {
        final PersonTable PERSON2 = PERSON.as("P2");

        PreparableStatement statement =
            select(PERSON.NAME)
                .from(PERSON)
                .where(PERSON.NAME.eq(
                    select(PERSON2.NAME).from(PERSON2).where(PERSON2.SSN.eq("17016812345"))));

        final String expectedSql = """
            select P.NAME P_NAME \
            from PERSON P \
            where P.NAME = (select P2.NAME P2_NAME from PERSON P2 where P2.SSN = ?)""";
        Object[] expectedParams = {"17016812345"};

        assertThat(statement.sql(context())).isEqualTo(expectedSql);
        assertThat(statement.params(context())).contains(expectedParams);
    }

    @Test
    public void shouldHandleSubqueriesInFromClause() {
        PreparableStatement statement =
            select(countAll().as("ANTALL"))
                .from(select(1)
                    .from(PERSON)
                    .where(PERSON.NAME.like("or")), "TREFF");

        final String expectedSql = """
            select count(*) ANTALL from (\
            select 1 \
            from PERSON P \
            where P.NAME like ?\
            ) TREFF""";
        Object[] expectedParams = {"%or%"};

        assertThat(statement.sql(context())).isEqualTo(expectedSql);
        assertThat(statement.params(context())).contains(expectedParams);
    }

    @Test
    public void shouldHandleReplacementConstants() {
        PreparableStatement statement =
            select(PERSON.NAME, constant("123 45 678").forField(PERSON.PHONE_NO), PERSON.SSN)
                .from(PERSON)
                .where(PERSON.NAME.eq("Per"));

        final String expectedSql = """
            select P.NAME P_NAME, '123 45 678' P_PHONE_NO, P.SSN P_SSN \
            from PERSON P \
            where P.NAME = ?""";
        Object[] expectedParams = {"Per"};

        assertThat(statement.sql(context())).isEqualTo(expectedSql);
        assertThat(statement.params(context())).contains(expectedParams);
    }

    @Test
    public void shouldHandleSingleConstantSelect() {
        PreparableStatement statement =
            select(1)
                .from(PERSON)
                .where(PERSON.NAME.eq("Per"));

        final String expectedSql = """
            select 1 \
            from PERSON P \
            where P.NAME = ?""";
        Object[] expectedParams = {"Per"};

        assertThat(statement.sql(context())).isEqualTo(expectedSql);
        assertThat(statement.params(context())).contains(expectedParams);
    }

    @Test
    public void shouldHandleAggregates() {
        PreparableStatement statement =
            select(max(ADDRESS.ZIP).as("MAX_ZIP"))
                .from(ADDRESS)
                .join(ADDRESS.PERSON_ID.on(PERSON.ID))
                .where(PERSON.SSN.eq("17016812345"));

        final String expectedSql = """
            select max(A.ZIP) MAX_ZIP \
            from ADDRESS A \
            inner join PERSON P on A.PERSON_ID = P.ID \
            where P.SSN = ?""";
        Object[] expectedParams = {"17016812345"};

        assertThat(statement.sql(context())).isEqualTo(expectedSql);
        assertThat(statement.params(context())).contains(expectedParams);
    }

    @Test
    public void shouldHandleAggregatesWithGroupingAndOrdering() {
        PreparableStatement statement =
            select(PERSON.NAME, max(ADDRESS.ZIP))
                .from(ADDRESS)
                .join(ADDRESS.PERSON_ID.on(PERSON.ID))
                .where(PERSON.SSN.eq("17016812345"))
                .groupBy(PERSON.NAME)
                .orderBy(max(ADDRESS.ZIP).asc());

        final String expectedSql = """
            select P.NAME P_NAME, max(A.ZIP) MAX_A_ZIP \
            from ADDRESS A \
            inner join PERSON P on A.PERSON_ID = P.ID \
            where P.SSN = ? \
            group by P.NAME \
            order by MAX_A_ZIP asc""";
        final List<Object> expectedParams = singletonList("17016812345");

        assertThat(statement.sql(context())).isEqualTo(expectedSql);
        assertThat(statement.params(context())).isEqualTo(expectedParams);
    }

    private Context context() {
        return Context.of(new MySqlDialect());
    }
}
