-- liquibase formatted sql

--changeset update-user-vat-number:1
alter table management.hua_user
    add mobile_number varchar(20);

alter table management.hua_user
    add vat_number varchar(20);