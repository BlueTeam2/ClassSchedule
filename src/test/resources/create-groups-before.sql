TRUNCATE
    "public".students,
    "public".users,
    "public".groups
    RESTART IDENTITY
    CASCADE;

insert into "public".groups(id, title, disable, sort_order) values
(4,'444', false, 2),
(5,'555', true, null),
(6, '666', false, 1);

insert into "public" .users(id, email, password, role) values
(1, 'zzz1@gmail.com', 'Zzzzz123!', 'ROLE_USER');

insert into "public".students(id, name, patronymic, surname, group_id, user_id) values
(4, 'Name One', 'Patron One', 'Surname One', 4, 1);
