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
package io.github.torand.fastersql.domainmodel;

import java.util.Optional;

public class Person {
    private final int id;
    private final String name;
    private final String ssn;
    private final Optional<String> phoneNo;

    public Person(int id, String name, String ssn, Optional<String> phoneNo) {
        this.id = id;
        this.name = name;
        this.ssn = ssn;
        this.phoneNo = phoneNo;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSsn() {
        return ssn;
    }

    public Optional<String> getPhoneNo() {
        return phoneNo;
    }
}
