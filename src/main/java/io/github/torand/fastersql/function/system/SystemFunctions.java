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
package io.github.torand.fastersql.function.system;

public final class SystemFunctions {
    private SystemFunctions() {}

    public static CurrentTimestamp currentTimestamp() {
        return new CurrentTimestamp(null);
    }

    public static CurrentDate currentDate() {
        return new CurrentDate(null);
    }

    public static CurrentTime currentTime() {
        return new CurrentTime(null);
    }
}
