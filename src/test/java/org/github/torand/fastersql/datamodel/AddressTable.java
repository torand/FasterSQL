package org.github.torand.fastersql.datamodel;

import org.github.torand.fastersql.Field;
import org.github.torand.fastersql.Table;

public class AddressTable extends Table<AddressTable> {
    public Field PERSON_ID = field("PERSON_ID");
    public Field STREET = field("STREET");
    public Field ZIP = field("ZIP");
    public Field COUNTRY = field("COUNTRY");
    public Field VERIFIED = field("VERIFIED");

    AddressTable(String alias) {
        super("ADDRESS", alias, AddressTable::new);
    }

    AddressTable() {
        super("ADDRESS", "A", AddressTable::new);
    }
}
