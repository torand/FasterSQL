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
package io.github.torand.fastersql.datamodel;

import io.github.torand.fastersql.Field;
import io.github.torand.fastersql.Table;

public class AddressTable extends Table<AddressTable> {
    public Field PERSON_ID = field("PERSON_ID");
    public Field STREET = field("STREET");
    public Field ZIP = field("ZIP");
    public Field COUNTRY = field("COUNTRY");
    public Field VERIFIED = field("VERIFIED");

    AddressTable(String alias) {
        super("ADDRESS", alias, AddressTable::new);
    }

    AddressTable() {
        super("ADDRESS", "A", AddressTable::new);
    }
}
