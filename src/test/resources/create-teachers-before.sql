TRUNCATE
    "public".lessons,
    "public".teachers,
    "public".department,
    "public".users
    RESTART IDENTITY
    CASCADE;

insert into department(id, name) values (DEFAULT, 'Department1');
insert into department(id, name) values (DEFAULT, 'Department2');

insert into users(id, email, password) values (DEFAULT, 'teacher@gmail.com', 'Pass1233!');
insert into users(id, email, password) values (DEFAULT, 'Seven@test.com', 'Pass1233!');

insert into teachers(id, name, patronymic, position, surname, department_id, user_id)
values (10, 'Ivan', 'Ivanovych', 'docent', 'Ivanov', 1, 1);

insert into teachers(id, name, patronymic, position, surname, department_id, disable)
values (20, 'Ivan', 'Ivanovych', 'docent', 'Ivanov', 1, true);

insert into teachers(id, name, patronymic, position, surname, department_id, user_id)
values (DEFAULT, 'SecondName', 'SecondPatronymic', 'SecondPosition', 'SecondSurname', null, null);
insert into teachers(id, name, patronymic, position, surname, department_id, user_id)
values (DEFAULT, 'ThirdName', 'ThirdPatronymic', 'ThirdPosition', 'ThirdSurname', 1, null);
insert into teachers(id, name, patronymic, position, surname, department_id, user_id)
values (DEFAULT, 'Five', 'Five', 'Five', 'Five', null, null);
insert into teachers(id, name, patronymic, position, surname, department_id, user_id)
values (DEFAULT, 'Six', 'Six', 'Six', 'Six', 1, null);
insert into teachers(id, name, patronymic, position, surname, department_id, user_id)
values (DEFAULT, 'Seven', 'Seven', 'Seven', 'Seven', 1, 2);

