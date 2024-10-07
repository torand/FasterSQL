package org.github.torand.fastersql.domainmodel;

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
