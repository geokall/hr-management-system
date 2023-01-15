-- schema is created from docker-compose and liquibase cannot create schema except PUBLIC

-- liquibase formatted sql

-- changeset first-tables:1
create table management.HUA_ROLE
(
    id   int8 generated by default as identity,
    name varchar(30) not null,
    primary key (id)
);
create table management.HUA_USER
(
    id                     int8 generated by default as identity,
    birth_date             date,
    created_date           timestamp    not null,
    email                  varchar(25)  not null,
    last_modification_date timestamp,
    name                   varchar(20),
    password               varchar(255) not null,
    surname                varchar(20),
    username               varchar(25)  not null,
    primary key (id)
);
create table management.USER_ROLE
(
    user_id int8 not null,
    role_id int8 not null,
    primary key (user_id, role_id)
);
alter table if exists management.HUA_USER
    add constraint UK_foifti1tkmfx6civn1hhpy9mw unique (email);
alter table if exists management.HUA_USER
    add constraint UK_6sj80w5kgd7s32pyekacvm0bs unique (username);
alter table if exists management.USER_ROLE
    add constraint FK4dtax8dqn5q8erfpwpsfmlefy foreign key (role_id) references management.HUA_ROLE;
alter table if exists management.USER_ROLE
    add constraint FK9h21kuptkh88qcvyjil91efe0 foreign key (user_id) references management.HUA_USER;

--changeset restart-identity:2
TRUNCATE TABLE management.HUA_USER, management.HUA_ROLE, management.USER_ROLE RESTART IDENTITY;

-- changeset set-roles:3
INSERT INTO management.HUA_ROLE (name)
values ('ADMIN'),
       ('READER');

-- changeset extension-pg-crypto:4
CREATE EXTENSION if not exists pgcrypto;

--changeset create-admin-user:5
INSERT INTO management.HUA_USER(name, surname, birth_date, created_date, email, last_modification_date,
                                password, username)
VALUES ('Harokopio', 'University', '1990-05-03', '2022-01-01 08:25:12.000000', 'hua@itp.com', null,
        crypt('admin', gen_salt('bf')),
        'hua');
INSERT INTO management.USER_ROLE
VALUES (1, 1);