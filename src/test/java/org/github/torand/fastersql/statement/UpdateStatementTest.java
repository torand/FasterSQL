package org.github.torand.fastersql.statement;

import org.github.torand.fastersql.Context;
import org.github.torand.fastersql.dialect.MySqlDialect;
import org.junit.Before;
import org.junit.Test;

import static org.github.torand.fastersql.datamodel.TestDataModel.ADDRESS;
import static org.github.torand.fastersql.datamodel.TestDataModel.PERSON;
import static org.github.torand.fastersql.function.system.SystemFunctions.currentTimestamp;
import static org.github.torand.fastersql.statement.Statements.update;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class UpdateStatementTest {
    private PreparableStatement statement;

    @Before
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

        assertThat(statement.sql(context()), equalTo(expectedSql));
    }

    @Test
    public void shouldCollectArgsInCorrectOrder() {
        Object[] expectedArgs = {"Tore Torell", "17016812345"};
        assertThat(statement.params(context()), contains(expectedArgs));
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

        assertThat(statement.sql(context()), equalTo(expectedSql));
        assertThat(statement.params(context()), contains(expectedParams));
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

        assertThat(statement.sql(context()), equalTo(expectedSql));
        assertThat(statement.params(context()), contains(expectedParams));
    }

    private Context context() {
        return Context.of(new MySqlDialect());
    }
}
