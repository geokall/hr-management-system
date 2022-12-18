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

--changeset update-user-numbers:4
alter table management.hua_user
    add work_number varchar(20);

alter table management.hua_user
    add home_number varchar(20);

alter table management.hua_user
    add employee_number bigint;

--changeset update-user-division:5
create table management.HUA_DIVISION
(
    ID   int8 generated by default as identity,
    name varchar(255),
    primary key (ID)
);
alter table if exists management.HUA_USER add column division_id int8;
alter table if exists management.HUA_USER add constraint FKt24r5ps8o4e5jnevnq9tvm4cc foreign key (division_id) references management.HUA_DIVISION;

--changeset update-user-location:6
create table management.HUA_LOCATION
(
    ID   int8 generated by default as identity,
    name varchar(255),
    primary key (ID)
);
alter table if exists management.HUA_USER add column location_id int8;
alter table if exists management.HUA_USER add constraint FKt24r5ps8o4e5jnevnq9tvm4qq foreign key (location_id) references management.HUA_LOCATION;

--changeset update-user-manager-direct:7
create table management.HUA_DIRECT_REPORT
(
    user_id int8 not null,
    primary key (user_id)
);
create table management.HUA_MANAGER
(
    user_id int8 not null,
    primary key (user_id)
);
create table management.user_direct_reports
(
    user_id          int8 not null,
    direct_report_id int8 not null,
    primary key (user_id, direct_report_id)
);
create table management.user_managers
(
    user_id    int8 not null,
    manager_id int8 not null,
    primary key (user_id, manager_id)
);
alter table if exists management.HUA_DIRECT_REPORT add constraint FK6rkjd1lvrqnhlpqonc8dn1uws foreign key (user_id) references management.HUA_USER;
alter table if exists management.HUA_MANAGER add constraint FKe7weq3urx1vj9uf85gokh21n4 foreign key (user_id) references management.HUA_USER;
alter table if exists management.user_direct_reports add constraint FKf1qncy5qr961592hi0v21s2ql foreign key (direct_report_id) references management.HUA_DIRECT_REPORT;
alter table if exists management.user_direct_reports add constraint FK2ddxiobfl9a2cxwn50epaa8ew foreign key (user_id) references management.HUA_USER;
alter table if exists management.user_managers add constraint FKae56ptx2xxdhsdgnbk381xc1a foreign key (manager_id) references management.HUA_MANAGER;
alter table if exists management.user_managers add constraint FK2icf4k95scrhjam83th2r9v1u foreign key (user_id) references management.HUA_USER;

--changeset update-user-education-and-personal:8
create table management.HUA_EDUCATION
(
    id             int8 generated by default as identity,
    college        varchar(255),
    degree         varchar(255),
    gpa            float8,
    specialization varchar(255),
    study_from     date,
    study_to       date,
    user_id        int8,
    primary key (id)
);
alter table if exists management.hua_user
    rename column email to business_email;
alter table if exists management.HUA_USER add column city varchar (255);
alter table if exists management.HUA_USER add column country varchar (255);
alter table if exists management.HUA_USER add column facebook_url varchar (255);
alter table if exists management.HUA_USER add column linkedin_url varchar (255);
alter table if exists management.HUA_USER add column personal_email varchar (35);
alter table if exists management.HUA_USER add column postalCode varchar (255);
alter table if exists management.HUA_USER add column province varchar (255);
alter table if exists management.HUA_USER add column street1 varchar (255);
alter table if exists management.HUA_USER add column street2 varchar (255);
alter table if exists management.HUA_USER add column twitter_url varchar (255);
alter table if exists management.HUA_USER drop constraint if exists UK_m712w6mk621bjebxx0qo8a946;
alter table if exists management.HUA_USER add constraint UK_m712w6mk621bjebxx0qo8a946 unique (personal_email);
alter table if exists management.HUA_EDUCATION add constraint FKcl08qq2ugplmblf0kvdcm4967 foreign key (user_id) references management.HUA_USER;

--changeset update-user-numbers:9
alter table if exists management.HUA_USER add column mobile_number varchar (20);
alter table if exists management.HUA_USER add column work_number varchar (20);
alter table if exists management.HUA_USER add column home_number varchar (20);

--changeset update-user-bonus-status-business-email:10
create table management.HUA_BONUS
(
    id         int8 generated by default as identity,
    amount     float8,
    bonus_date date,
    comment    varchar(255),
    user_id    int8,
    primary key (id)
);
create table management.HUA_EMPLOYMENT_STATUS
(
    id                int8 generated by default as identity,
    comment           varchar(255),
    effective_date    date,
    employment_status varchar(255),
    user_id           int8,
    primary key (id)
);
alter table if exists management.HUA_USER drop constraint if exists UK_dfgyswg2jeprhg21u14qiyl9q;
alter table if exists management.HUA_USER add constraint UK_dfgyswg2jeprhg21u14qiyl9q unique (business_email);
alter table if exists management.HUA_BONUS add constraint FK9ivha192ocgq81pa8kxvph6iv foreign key (user_id) references management.HUA_USER;
alter table if exists management.HUA_EMPLOYMENT_STATUS add constraint FK3yah5y5qn07sgou2kx2c24hax foreign key (user_id) references management.HUA_USER;

--changeset update-job-description-job-information:11
create table management.HUA_JOB_INFORMATION
(
    id             int8 generated by default as identity,
    division       varchar(255),
    effective_date date,
    job_title      varchar(255),
    location       varchar(255),
    manager_id     int8,
    user_id        int8,
    primary key (id)
);
alter table if exists management.HUA_USER add column job_description oid;
alter table if exists management.HUA_JOB_INFORMATION add constraint FKp9infk0k282eeqapv3qubrwn1 foreign key (manager_id) references management.HUA_MANAGER (user_id);
alter table if exists management.HUA_JOB_INFORMATION add constraint FK1llx1ksaglcb4n4p5tx7nnj5 foreign key (user_id) references management.HUA_USER;

--changeset update-user-ethnicity-job-category:12
alter table if exists management.HUA_USER add column ethnicity varchar (40);
alter table if exists management.HUA_USER add column job_category varchar (40);

--changeset update-user-enums-more-chars:13
alter table management.hua_user
alter
column job_category type varchar(70) using job_category::varchar(70);

alter table management.hua_user
alter
column ethnicity type varchar(70) using ethnicity::varchar(70);

alter table management.hua_user
alter
column job_description type text using job_description::text;

--changeset update-work-info:14
create table management.HUA_WORK_INFORMATION
(
    id             int8 generated by default as identity,
    effective_date date,
    job_title      varchar(255),
    division_id    int8,
    location_id    int8,
    manager_id     int8,
    user_id        int8,
    primary key (id)
);
alter table if exists management.HUA_WORK_INFORMATION add constraint FKke0t2xs5var4fm981cgasydc foreign key (division_id) references management.HUA_DIVISION;
alter table if exists management.HUA_WORK_INFORMATION add constraint FKbnr08lo0xg0d6mcwhj9dgl9pv foreign key (location_id) references management.HUA_LOCATION;
alter table if exists management.HUA_WORK_INFORMATION add constraint FKqao3xa6t7ed6u309oo7xslnhe foreign key (manager_id) references management.HUA_USER;
alter table if exists management.HUA_WORK_INFORMATION add constraint FKp3wxj2wsg15vq8nj5vgk47607 foreign key (user_id) references management.HUA_USER;

--changeset remove-unused-tables:15
drop table if exists management.hua_direct_report cascade;

drop table if exists management.user_managers cascade;;

drop table if exists management.hua_manager cascade;

drop table if exists management.user_direct_reports cascade;

drop table if exists management.hua_job_information cascade;