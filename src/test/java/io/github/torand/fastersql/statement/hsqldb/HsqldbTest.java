/*
 * Copyright (c) 2024-2025 Tore Eide Andersen
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
package io.github.torand.fastersql.statement.hsqldb;

import io.github.torand.fastersql.dialect.Dialect;
import io.github.torand.fastersql.statement.StatementTester;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.UUID;

public abstract class HsqldbTest {
    private static final Logger logger = LoggerFactory.getLogger(HsqldbTest.class);

    protected static JDBCDataSource ds;

    @BeforeAll
    static void setUp() throws IOException, SQLException {
        String databaseDir = "target/test-db/hsqldb";
        Files.createDirectories(Path.of(databaseDir));
        String databaseName = UUID.randomUUID().toString().replace("-", "");

        ds = new JDBCDataSource();
        ds.setUrl("jdbc:hsqldb:file:%s/%s.db".formatted(databaseDir, databaseName));
        ds.setUser("SA");
        ds.setPassword("password");

        createDatabase();
    }

    private static void createDatabase() throws SQLException {
        ScriptRunner sr = new ScriptRunner(ds.getConnection());
        Reader initScriptReader = new BufferedReader(new InputStreamReader(HsqldbTest.class.getResourceAsStream("/hsqldb-init.sql")));
        sr.runScript(initScriptReader);
    }

    protected StatementTester statementTester() {
        return StatementTester.using(ds);
    }

    protected StatementTester statementTester(Dialect dialect) {
        return StatementTester.using(ds, dialect);
    }
}
