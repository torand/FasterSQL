-- noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE customer (
    id CHAR(36) NOT NULL PRIMARY KEY,
    first_name TEXT,
    last_name TEXT NOT NULL,
    street_address TEXT,
    zip_code TEXT,
    city TEXT,
    country_code CHAR(3),
    email_address TEXT NOT NULL,
    mobile_no TEXT,
    mobile_no_verified NUMERIC(1),
    created_time DATETIME,
    last_login_time DATETIME
);

INSERT INTO customer
VALUES('9df03cd1-245f-4257-95e2-85cb5bd39ad8', 'Ola', 'Nordmann', 'Karl Johansgt 12', 'N-0123', 'Oslo', 'NOR', 'ola.nordmann@email.no', '4798765432', 1, '2020-09-10 14:10:10', null);

INSERT INTO customer
VALUES('7f6bffb9-c65f-40b3-b007-b291b128472d', 'Jens', 'Hansen', 'Nørrebrogade 13', 'DK-1360', 'København', 'DEN', 'jens.hansen@email.dk', '4522334455', 1, '2021-10-11 15:11:11', null);

INSERT INTO customer
VALUES('ef17ec93-88a4-40dd-8a91-41e11d54d896', 'Björn', 'Svensson', 'Drottninggatan 14', 'SE-111 36', 'Stockholm', 'SWE', 'bjorn.svensson@email.se', '46755667788', 0, '2022-11-12 16:12:12', null);

CREATE TABLE product (
    id CHAR(36) NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    category TEXT NOT NULL,
    price NUMERIC(9,2) NOT NULL,
    stock_count NUMERIC(5) NOT NULL
);

INSERT INTO product
VALUES ('fdd89ce1-db38-4deb-9767-0324e91d4933', 'Ekornes Stressless resting chair', '', 'FURNITURE', 5433.50, 7);

INSERT INTO product
VALUES ('92bfca8e-2898-408c-8dd3-2b3f9d362044', 'Louis Poulsen Panthella 160 table lamp', '', 'LAMP', 3778.45, 3);

INSERT INTO product
VALUES ('c50df894-ed38-49bc-831e-8de6453cd6f6', 'Electrolux 800 UltraCare washing machine', '', 'APPLIANCE', 7122.09, 5);

INSERT INTO product
VALUES ('dba9f942-c24f-4b6a-89b6-881236ff5438', 'Apple iPad Pro tablet', '', 'ELECTRONICS', 14999.00, 21);

INSERT INTO product
VALUES ('7a4b3e96-afee-4284-8ccd-f7461bcd602b', 'Samsung Galaxy S25 Ultra mobile phone', '', 'ELECTRONICS', 17567.25, 17);

CREATE TABLE purchase (
    id CHAR(36) NOT NULL PRIMARY KEY,
    customer_id CHAR(36) NOT NULL,
    status TEXT NOT NULL,
    created_time DATETIME NOT NULL,
    shipped_time DATETIME,
    notes TEXT,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

INSERT INTO purchase
VALUES ('da47cb5b-0fd2-491f-acec-9726af1f32aa', '9df03cd1-245f-4257-95e2-85cb5bd39ad8', 'PROCESSING', '2023-12-01 09:34:45', null, 'TBD');

INSERT INTO purchase
VALUES ('df168f1d-ed4a-4cc4-b93a-51956d9a9c56', '7f6bffb9-c65f-40b3-b007-b291b128472d', 'DISPATCHED', '2024-01-17 11:55:09', '2024-02-02 07:12:55', null);

CREATE TABLE purchase_item (
    id CHAR(36) NOT NULL PRIMARY KEY,
    purchase_id CHAR(36) NOT NULL,
    product_id CHAR(36) NOT NULL,
    quantity NUMERIC(5) NOT NULL,
    FOREIGN KEY (purchase_id) REFERENCES purchase(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

INSERT INTO purchase_item
VALUES ('6d35986a-04ee-4ba0-a3f6-d421e98139db', 'da47cb5b-0fd2-491f-acec-9726af1f32aa', 'fdd89ce1-db38-4deb-9767-0324e91d4933', 1);

INSERT INTO purchase_item
VALUES ('a6fd519a-3baf-4892-b435-d11f7a080385', 'df168f1d-ed4a-4cc4-b93a-51956d9a9c56', '92bfca8e-2898-408c-8dd3-2b3f9d362044', 3);

