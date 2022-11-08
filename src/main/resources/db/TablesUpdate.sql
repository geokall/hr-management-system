-- liquibase formatted sql

--changeset update-user-vat-number:1
alter table management.hua_user
    add mobile_number varchar(20);

alter table management.hua_user
    add vat_number varchar(20);

--changeset update-user-gender-marital:2
alter table management.hua_user
    add gender varchar(20);

alter table management.hua_user
    add marital_status varchar(20);

--changeset update-user-enums-date:3
alter table management.hua_user
    add job_status varchar(20);

alter table management.hua_user
    add employee_status varchar(20);

alter table management.hua_user
    add hire_date date;