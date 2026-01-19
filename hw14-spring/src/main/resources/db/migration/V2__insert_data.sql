INSERT INTO client (name)
VALUES ('Ruslan'),('Sasha');

INSERT INTO address (street, client_id)
VALUES  ('Омская', 1), ('Ленина', 2);

INSERT INTO phone (number, client_id,client_key)
VALUES ('899999999', 1,0), ('811111111', 1,1);