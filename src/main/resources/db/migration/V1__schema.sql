CREATE TABLE if not exists tb_movement_type (
                                         id int8 NOT NULL,
                                         movement_type text NULL,
                                         CONSTRAINT tb_movement_type_pkey PRIMARY KEY (id)
);


CREATE TABLE if not exists tb_profile (
                                   id int8 NOT NULL,
                                   role_name varchar(35) NOT NULL,
                                   CONSTRAINT tb_profile_pkey PRIMARY KEY (id),
                                   CONSTRAINT uk_cpm019m5ygu7bgh5iiwkos9c8 UNIQUE (role_name)
);

CREATE TABLE if not exists tb_user (
                                id uuid NOT NULL,
                                email varchar(100) NOT NULL,
                                "name" varchar(70) NOT NULL,
                                "password" text NOT NULL,
                                register_date timestamp NULL,
                                update_date timestamp NULL,
                                CONSTRAINT tb_user_pkey PRIMARY KEY (id),
                                CONSTRAINT tb_user_email_unique UNIQUE (email)
);


CREATE TABLE if not exists tb_bank_account (
                                        id uuid NOT NULL,
                                        balance numeric(19, 2) NOT NULL,
                                        "name" varchar(100) NOT NULL,
                                        user_id uuid NOT NULL,
                                        CONSTRAINT tb_bank_account_pkey PRIMARY KEY (id),
                                        CONSTRAINT tb_bank_account_tb_user_fkey FOREIGN KEY (user_id) REFERENCES tb_user(id)
);


CREATE TABLE if not exists tb_movement (
                                    id uuid NOT NULL,
                                    movement_date timestamp NOT NULL,
                                    movement_type int4 NOT NULL,
                                    value numeric(19, 2) NOT NULL,
                                    bank_account_id uuid NOT NULL,
                                    CONSTRAINT tb_movement_pkey PRIMARY KEY (id),
                                    CONSTRAINT tb_movement_tb_bank_account_fkey FOREIGN KEY (bank_account_id) REFERENCES tb_bank_account(id)
);

CREATE TABLE if not exists tb_user_profiles (
                                         user_id uuid NOT NULL,
                                         profile_id int8 NOT NULL,
                                         CONSTRAINT tb_user_profiles_pkey PRIMARY KEY (user_id, profile_id),
                                         CONSTRAINT tb_user_profiles_tb_user_fkey FOREIGN KEY (user_id) REFERENCES tb_user(id),
                                         CONSTRAINT tb_user_profiles_tb_profile_fkey FOREIGN KEY (profile_id) REFERENCES tb_profile(id)
);



insert into tb_profile (id, role_name) values (1, 'ROLE_ADMIN') on conflict do nothing;
insert into tb_profile (id, role_name) values (2, 'ROLE_USER') on conflict do nothing;

create table if not exists tb_movement_type (id bigint primary key, movement_type text) ;

insert into tb_movement_type(id, movement_type) values (1, 'INPUT') on conflict do nothing;
insert into tb_movement_type(id, movement_type) values (2, 'OUTPUT') on conflict do nothing;
insert into tb_user(id, email, "name", "password", register_date, update_date) values ('917b0023-157f-4eff-be1d-61ddb067eddc', 'admin@uneeddevs.com', 'U Need devs', '$2a$12$1HsFgSCDpUfx7bypUySgAuE6De/zKxaQ9RKbaZxlds1/kocmGGNjG', current_date , current_date ) on conflict do nothing;

insert into tb_user_profiles(user_id, profile_id) values ('917b0023-157f-4eff-be1d-61ddb067eddc', 1) on conflict do nothing;
insert into tb_user_profiles(user_id, profile_id) values ('917b0023-157f-4eff-be1d-61ddb067eddc', 2) on conflict do nothing;