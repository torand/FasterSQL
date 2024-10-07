package org.github.torand.fastersql.datamodel;

import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.Table;

public class PersonTable extends Table<PersonTable> {
    public Field ID = field("ID");
    public Field NAME = field("NAME");
    public Field SSN = field("SSN");
    public Field PHONE_NO = field("PHONE_NO");

    PersonTable(String alias) {
        super("PERSON", alias, PersonTable::new);
    }

    PersonTable() {
        super("PERSON", "P", PersonTable::new);
    }
}
