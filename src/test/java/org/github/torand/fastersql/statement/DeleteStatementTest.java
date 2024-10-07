package org.github.torand.fastersql.statement;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.dialect.MySqlDialect;
import org.junit.Before;
import org.junit.Test;

import static org.github.torand.fastersql.datamodel.TestDataModel.PERSON;
import static org.github.torand.fastersql.statement.Statements.delete;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class DeleteStatementTest {
    private PreparableStatement statement;

    @Before
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

        assertThat(statement.sql(context()), equalTo(expectedSql));
    }

    @Test
    public void shouldCollectArgsInCorrectOrder() {
        Object[] expectedArgs = {"17016812345"};
        assertThat(statement.params(context()), contains(expectedArgs));
    }

    private Context context() {
        return Context.of(new MySqlDialect());
    }
}
