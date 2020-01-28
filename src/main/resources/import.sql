/* Populate tables */
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('Marcos', 'Bertolotti', 'marcos@mail.com', '2020-08-01', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('John', 'Doe', 'john.doe@gmail.com', '2020-08-02', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('Linus', 'Torvalds', 'linus.torvalds@gmail.com', '2020-08-03', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('Jane', 'Doe', 'jane.doe@gmail.com', '2020-08-04', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('Rasmus', 'Lerdorf', 'rasmus.lerdorf@gmail.com', '2020-08-05', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('Erich', 'Gamma', 'erich.gamma@gmail.com', '2020-08-06', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('Richard', 'Helm', 'richard.helm@gmail.com', '2020-08-07', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('Ralph', 'Johnson', 'ralph.johnson@gmail.com', '2020-08-08', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('John', 'Vlissides', 'john.vlissides@gmail.com', '2020-08-09', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('James', 'Gosling', 'james.gosling@gmail.com', '2020-08-010', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('Bruce', 'Lee', 'bruce.lee@gmail.com', '2020-08-11', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('Johnny', 'Doe', 'johnny.doe@gmail.com', '2020-08-12', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('John', 'Roe', 'john.roe@gmail.com', '2020-08-13', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('Jane', 'Roe', 'jane.roe@gmail.com', '2020-08-14', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('Richard', 'Doe', 'richard.doe@gmail.com', '2020-08-15', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('Janie', 'Doe', 'janie.doe@gmail.com', '2020-08-16', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('Phillip', 'Webb', 'phillip.webb@gmail.com', '2020-08-17', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('Stephane', 'Nicoll', 'stephane.nicoll@gmail.com', '2020-08-18', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('Sam', 'Brannen', 'sam.brannen@gmail.com', '2020-08-19', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('Juergen', 'Hoeller', 'juergen.Hoeller@gmail.com', '2020-08-20', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('Janie', 'Roe', 'janie.roe@gmail.com', '2020-08-21', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('John', 'Smith', 'john.smith@gmail.com', '2020-08-22', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('Joe', 'Bloggs', 'joe.bloggs@gmail.com', '2020-08-23', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('John', 'Stiles', 'john.stiles@gmail.com', '2020-08-24', '');
INSERT INTO clients (first_name, last_name, email, create_at, photo) VALUES('Richard', 'Roe', 'stiles.roe@gmail.com', '2020-08-25', '');

/* Populate tabla productos */
INSERT INTO products (name, price, create_at) VALUES('Panasonic Monitor LCD', 259990, NOW());
INSERT INTO products (name, price, create_at) VALUES('Sony Camera digital DSC-W320B', 123490, NOW());
INSERT INTO products (name, price, create_at) VALUES('Apple iPod shuffle', 1499990, NOW());
INSERT INTO products (name, price, create_at) VALUES('Sony Notebook Z110', 37990, NOW());
INSERT INTO products (name, price, create_at) VALUES('Hewlett Packard Multifuntional F2280', 69990, NOW());
INSERT INTO products (name, price, create_at) VALUES('Bianchi Bike Aro 26', 69990, NOW());
INSERT INTO products (name, price, create_at) VALUES('Mica comfortable 5 drawers', 299990, NOW());

/* Creamos algunas facturas */
INSERT INTO bills (description, observation, client_id, create_at) VALUES('Team bill of office', null, 1, NOW());
INSERT INTO items_bills (quantity, bill_id, product_id) VALUES(1, 1, 1);
INSERT INTO items_bills (quantity, bill_id, product_id) VALUES(2, 1, 4);
INSERT INTO items_bills (quantity, bill_id, product_id) VALUES(1, 1, 5);
INSERT INTO items_bills (quantity, bill_id, product_id) VALUES(1, 1, 7);

INSERT INTO bills (description, observation, client_id, create_at) VALUES('Bike bill', 'Some important note!', 1, NOW());
INSERT INTO items_bills (quantity, bill_id, product_id) VALUES(3, 2, 6);