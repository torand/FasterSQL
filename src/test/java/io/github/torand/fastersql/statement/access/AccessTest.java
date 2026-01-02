/*
 * Copyright (c) 2024-2026 Tore Eide Andersen
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
package io.github.torand.fastersql.statement.access;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.torand.fastersql.dialect.Dialect;
import io.github.torand.fastersql.statement.StatementTester;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.BeforeAll;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.UUID;

abstract class AccessTest {

    protected static DataSource ds;

    @BeforeAll
    static void setUp() throws IOException, SQLException {
        String databaseDir = "target/test-db/access";
        Files.createDirectories(Path.of(databaseDir));
        String databaseName = UUID.randomUUID().toString().replace("-", "");

        // https://spannm.github.io/ucanaccess/10-ucanaccess.html
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:ucanaccess://./%s/%s.mdb;memory=true;newDatabaseVersion=V2010".formatted(databaseDir, databaseName));
        hikariConfig.setDriverClassName("net.ucanaccess.jdbc.UcanaccessDriver");

        ds = new HikariDataSource(hikariConfig);

        createDatabase();
    }

    private static void createDatabase() throws SQLException {
        ScriptRunner sr = new ScriptRunner(ds.getConnection());
        Reader initScriptReader = new BufferedReader(new InputStreamReader(AccessTest.class.getResourceAsStream("/access-init.sql")));
        sr.runScript(initScriptReader);
    }

    protected StatementTester statementTester() {
        return StatementTester.using(ds);
    }

    protected StatementTester statementTester(Dialect dialect) {
        return StatementTester.using(ds, dialect);
    }
}
