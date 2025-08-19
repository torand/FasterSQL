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
package io.github.torand.fastersql.statement.sqlite;

import io.github.torand.fastersql.dialect.Dialect;
import io.github.torand.fastersql.statement.StatementTester;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.BeforeAll;
import org.sqlite.SQLiteConfig;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.UUID;

abstract class SqliteTest {

    protected static SQLiteConnectionPoolDataSource ds;

    @BeforeAll
    static void setUp() throws IOException, SQLException {
        String databaseDir = "target/test-db/sqlite";
        Files.createDirectories(Path.of(databaseDir));
        String databaseName = UUID.randomUUID().toString().replace("-", "");

        SQLiteConfig sqLiteConfig = new SQLiteConfig();
        String tempDir = databaseDir + "/temp";
        Files.createDirectories(Path.of(tempDir));
        sqLiteConfig.setTempStoreDirectory(tempDir);

        ds = new SQLiteConnectionPoolDataSource(sqLiteConfig);
        ds.setUrl("jdbc:sqlite:%s/%s.db".formatted(databaseDir, databaseName));

        createDatabase();
    }

    private static void createDatabase() throws SQLException {
        ScriptRunner sr = new ScriptRunner(ds.getConnection());
        Reader initScriptReader = new BufferedReader(new InputStreamReader(SqliteTest.class.getResourceAsStream("/sqlite-init.sql")));
        sr.runScript(initScriptReader);
    }

    protected StatementTester statementTester() {
        return StatementTester.using(ds);
    }

    protected StatementTester statementTester(Dialect dialect) {
        return StatementTester.using(ds, dialect);
    }
}
