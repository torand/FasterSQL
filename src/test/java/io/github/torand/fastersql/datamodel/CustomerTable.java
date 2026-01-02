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
package io.github.torand.fastersql.datamodel;

import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.model.TableModel;

public class CustomerTable extends TableModel<CustomerTable> {
    public final Column ID = column("ID");
    public final Column FIRST_NAME = column("FIRST_NAME");
    public final Column LAST_NAME = column("LAST_NAME");
    public final Column STREET_ADDRESS = column("STREET_ADDRESS");
    public final Column ZIP_CODE = column("ZIP_CODE");
    public final Column CITY = column("CITY");
    public final Column COUNTRY_CODE = column("COUNTRY_CODE");
    public final Column EMAIL_ADDRESS = column("EMAIL_ADDRESS");
    public final Column MOBILE_NO = column("MOBILE_NO");
    public final Column MOBILE_NO_VERIFIED = column("MOBILE_NO_VERIFIED");
    public final Column CREATED_TIME = column("CREATED_TIME");
    public final Column LAST_LOGIN_TIME = column("LAST_LOGIN_TIME");

    CustomerTable(String alias) {
        super("CUSTOMER", alias, CustomerTable::new);
    }
}
