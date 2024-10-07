package org.github.torand.fastersql.statement;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.dialect.MySqlDialect;
import org.junit.Before;
import org.junit.Test;

import static org.github.torand.fastersql.datamodel.TestDataModel.PERSON;
import static org.github.torand.fastersql.statement.Statements.truncate;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class TruncateStatementTest {
    private PreparableStatement statement;

    @Before
    public void setup() {
        statement =
            truncate().table(PERSON);
    }

    @Test
    public void shouldBuildValidSql() {
        final String expectedSql =
                "truncate table PERSON";

        assertThat(statement.sql(context()), equalTo(expectedSql));
    }

    @Test
    public void shouldCollectArgsInCorrectOrder() {
        assertThat(statement.params(context()), empty());
    }

    private Context context() {
        return Context.of(new MySqlDialect());
    }
}
