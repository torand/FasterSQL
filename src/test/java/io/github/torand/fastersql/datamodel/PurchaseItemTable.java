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
package io.github.torand.fastersql.datamodel;

import io.github.torand.fastersql.model.Column;
import io.github.torand.fastersql.model.TableModel;

public class PurchaseItemTable extends TableModel<PurchaseItemTable> {
    public final Column ID = column("ID");
    public final Column PURCHASE_ID = column("PURCHASE_ID");
    public final Column PRODUCT_ID = column("PRODUCT_ID");
    public final Column QUANTITY = column("QUANTITY");

    PurchaseItemTable(String alias) {
        super("PURCHASE_ITEM", alias, PurchaseItemTable::new);
    }
}
