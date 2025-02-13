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

public class CustomerTable extends Table<CustomerTable> {
    public final Field ID = field("ID");
    public final Field FIRST_NAME = field("FIRST_NAME");
    public final Field LAST_NAME = field("LAST_NAME");
    public final Field STREET_ADDRESS = field("STREET_ADDRESS");
    public final Field ZIP_CODE = field("ZIP_CODE");
    public final Field CITY = field("CITY");
    public final Field COUNTRY_CODE = field("COUNTRY_CODE");
    public final Field EMAIL_ADDRESS = field("EMAIL_ADDRESS");
    public final Field MOBILE_NO = field("MOBILE_NO");
    public final Field MOBILE_NO_VERIFIED = field("MOBILE_NO_VERIFIED");
    public final Field CREATED_TIME = field("CREATED_TIME");
    public final Field LAST_LOGIN_TIME = field("LAST_LOGIN_TIME");

    CustomerTable(String alias) {
        super("CUSTOMER", alias, CustomerTable::new);
    }

    CustomerTable() {
        this("C");
    }
}
