TRUNCATE
    "public".lessons,
    "public".semester_period,
    "public".periods,
    "public".semester_day,
    "public".semesters,
    "public".groups,
    "public".teachers,
    "public".subjects
    RESTART IDENTITY
    CASCADE;


insert into "public".subjects(id, name) VALUES
(4, 'Biology'),
(5, 'History'),
(6, 'Astronomy');

insert into "public".teachers(id, name, patronymic, "position", surname) VALUES
(4, 'Ivan', 'Ivanovych', 'docent', 'Ivanov'),
(5, 'Petro', 'Petrovych', 'doctor','Petrov'),
(6, 'Dmytro', 'Dmytrovych','aspirant', 'Dmytryk'),
(7, 'Pavlo', 'Pavlovych', 'aspirant', 'Pavlov');

insert into "public".groups(id, title) values
(4,'111'),
(5,'222'),
(6,'333'),
(7,'444');

insert into "public".semesters(id, current_semester, default_semester, description, end_day, start_day, "year", disable) VALUES
(4, true , false, '1 semester', '2020-02-20', '2020/01/20', 2020, false),
(5, false, false, '2 semester', '2020-04-20', '2020/01/20', 2020, false),
(6, false, true , '3 semester', '2020-06-20', '2020/01/20', 2020, false),
(7, false, false, '4 semester', '2020-08-20', '2020/01/20', 2020, false);

insert into "public".semester_day (semester_id, "day") values
(4, 'MONDAY'),
(5, 'FRIDAY'),
(6, 'TUESDAY'),
(7, 'FRIDAY');

insert into "public".periods(id, end_time, name, start_time) VALUES
(4, '2020/04/15 02:00', '1 para', '2020/04/15 01:00'),
(5, '2020/04/15 04:00', '2 para', '2020/04/15 03:00'),
(6, '2020/04/15 06:00', '3 para', '2020/04/15 05:00'),
(7, '2020/04/15 08:00', '4 para', '2020/04/15 07:00');

insert into "public".semester_period (semester_id, period_id) values
(4, 7),
(5, 6),
(6, 5),
(7, 4);

insert into "public".lessons(id, hours, lessontype, subject_for_site, link_to_meeting, semester_id, group_id, subject_id, teacher_id) VALUES
(4, 1, 'LECTURE', '', '', 7, 5, 5, 5),
(5, 1, 'LABORATORY', '', '', 6, 4, 4, 4),
(6, 2, 'LABORATORY', '', '', 5, 4, 5, 6),
(7, 2, 'PRACTICAL', '', '', 4, 6, 6, 6),
(8, 1, 'LABORATORY', '', '', 7, 6, 4, 4),
(9, 1, 'LECTURE', '', '', 7, 4, 5, 5),
(10, 1, 'LECTURE', '', '', 7, 4, 4, 5),
(11, 1, 'PRACTICAL', '', '', 7, 5, 5, 5),
(12, 1, 'PRACTICAL', '', '', 5, 5, 5, 5);

insert into "public".lessons(id, hours, lessontype, subject_for_site, link_to_meeting, semester_id, group_id, subject_id, teacher_id, grouped) VALUES
(13, 1, 'LECTURE', 'Biology 1', '', 5, 4, 4, 5, true),
(14, 1, 'LECTURE', 'Biology 1', '', 5, 5, 4, 5, true),
(15, 1, 'LECTURE', 'Biology 2', '', 5, 6, 4, 5, true);