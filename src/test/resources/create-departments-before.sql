TRUNCATE
    "public".teachers,
    "public".department
    RESTART IDENTITY
    CASCADE;

insert into department(id, name, disable) values
(4, 'Department4', false),
(5, 'Department5', true);

insert into teachers(id, name, patronymic, position, surname, department_id)
values (4, 'Ivan', 'Ivanovych', 'docent', 'Ivanov', 4)