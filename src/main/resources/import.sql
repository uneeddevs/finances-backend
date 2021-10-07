insert into tb_profile (id, role_name) values (1, 'ROLE_ADMIN');
insert into tb_profile (id, role_name) values (2, 'ROLE_USER');

create table if not exists tb_movement_type (id bigint primary key, movement_type text);

insert into tb_movement_type(id, movement_type) values (1, 'INPUT');
insert into tb_movement_type(id, movement_type) values (2, 'OUTPUT');