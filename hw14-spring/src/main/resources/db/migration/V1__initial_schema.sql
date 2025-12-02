DROP TABLE IF EXISTS phone CASCADE;
DROP TABLE IF EXISTS address CASCADE;
DROP TABLE IF EXISTS client CASCADE;


DROP SEQUENCE IF EXISTS client_id_seq CASCADE;
DROP SEQUENCE IF EXISTS address_id_seq CASCADE;
DROP SEQUENCE IF EXISTS phone_id_seq CASCADE;


create table client (
                        id bigserial not null primary key,
                        name varchar(50)
);

create table address (
                         id bigserial not null primary key,
                         street varchar(50),
                         client_id bigint references client(id) on delete cascade
);

create table phone
(
    id              bigserial not null primary key,
    number          varchar(50) not null,
    client_id       bigint,
    client_key    int not null,
    constraint  fk_phone_client foreign key (client_id) references client (id) on delete cascade
);