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
package io.github.torand.fastersql.model;

/**
 * Defines a factory to create instances of a database table model.
 * @param <T> the table model class.
 */
@FunctionalInterface
public interface TableModelFactory<T extends TableModel> {
    /**
     * Creates a new instance of a database table model.
     * @param alias the table alias.
     * @return the new table model instance.
     */
    T newInstance(String alias);
}
