package org.github.torand.fastersql.statement;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.dialect.MySqlDialect;
import org.github.torand.fastersql.dialect.OracleDialect;
import org.github.torand.fastersql.domainmodel.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.github.torand.fastersql.datamodel.TestDataModel.PERSON;
import static org.github.torand.fastersql.statement.Statements.insertBatch;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class InsertBatchStatementTest {
    private PreparableStatement statement;

    @Before
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
        assertThat(statement.sql(oracleContext), equalTo(expectedSql));
    }

    @Test
    public void shouldBuildValidSqlForNonOracle() {
        final String expectedSql =
                "insert into PERSON (ID, NAME, SSN, PHONE_NO) "
              + "values (?, ?, ?, ?), (?, ?, ?, null), (?, ?, null, ?)";

        assertThat(statement.sql(context()), equalTo(expectedSql));
    }

    @Test
    public void shouldCollectArgsInCorrectOrder() {
        Object[] expectedArgs = {1, "Ole", "11111111111", "123 45 678", 2, "Dole", "22222222222", 3, "Doffen", "345 67 890"};
        assertThat(statement.params(context()), contains(expectedArgs));
    }

    private Context context() {
        return Context.of(new MySqlDialect());
    }
}
