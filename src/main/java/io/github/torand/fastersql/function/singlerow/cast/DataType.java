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
package io.github.torand.fastersql.function.singlerow.cast;

/**
 * Describes a concrete data type for the AS part of a CAST expression.
 */
public class DataType {
    private final IsoDataType isoDataType;
    private final int[] args;

    DataType(IsoDataType isoDataType, int... args) {
        this.isoDataType = isoDataType;
        this.args = args;
    }

    /**
     * Gets the ISO data type.
     * @return the ISO data type.
     */
    public IsoDataType getIsoDataType() {
        return isoDataType;
    }

    /**
     * Gets the data type arguments, if any.
     * @return the data type arguments.
     */
    public int[] getArgs() {
        return args;
    }
}
