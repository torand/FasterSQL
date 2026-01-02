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

public class PurchaseTable extends TableModel<PurchaseTable> {
    public final Column ID = column("ID");
    public final Column CUSTOMER_ID = column("CUSTOMER_ID");
    public final Column STATUS = column("STATUS");
    public final Column CREATED_TIME = column("CREATED_TIME");
    public final Column SHIPPED_TIME = column("SHIPPED_TIME");
    public final Column NOTES = column("NOTES");

    PurchaseTable(String alias) {
        super("PURCHASE", alias, PurchaseTable::new);
    }
}
