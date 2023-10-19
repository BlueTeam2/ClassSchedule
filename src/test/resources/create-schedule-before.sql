TRUNCATE
    "public".schedules,
    "public".lessons,
    "public".semester_period,
    "public".semester_day,
    "public".semesters,
    "public".groups,
    "public".teachers,
    "public".subjects,
    "public".periods,
    "public".rooms,
    "public".room_types
    RESTART IDENTITY
    CASCADE;

insert into "public".room_types(id, description) values
(4, 'small auditory'),
(5, 'medium auditory'),
(6, 'big auditory');

insert into "public".rooms(id, name, room_type_id) values
(4, 'Laboratory', 4),
(5, 'Practical room', 5),
(6, 'Lecture room', 6);

insert into "public".periods(id, end_time, name, start_time) VALUES
(4, '2020-04-15 02:00', '1 para', '2020-04-15 01:00'),
(5, '2020-04-15 04:00', '2 para', '2020-04-15 03:00'),
(6, '2020-04-15 06:00', '3 para', '2020-04-15 05:00'),
(7, '2020-04-15 08:00', '4 para', '2020-04-15 07:00');

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
(6,'333');

insert into "public".semesters(id, current_semester, default_semester, description, end_day, start_day, "year", disable) VALUES
(4, true , false, '1 semester', '2020-02-20', '2020-01-20', 2020, false),
(5, false, false, '2 semester', '2020-04-20', '2020-03-20', 2020, false),
(6, false, true , '3 semester', '2020-06-20', '2020-05-20', 2020, false),
(7, false, false, '4 semester', '2020-08-20', '2020-07-20', 2020, false);

insert into "public".semester_day (semester_id, "day") values
(4, 'MONDAY'),
(5, 'FRIDAY'),
(5, 'TUESDAY');

insert into "public".semester_period (semester_id, period_id) values
(4, 4),
(4, 5),
(5, 4);

insert into "public".lessons(id, hours, lessontype, subject_for_site, link_to_meeting, semester_id, group_id, subject_id, teacher_id, grouped) VALUES
(4, 1, 'LECTURE', '', '', 4, 4, 4, 4, false),
(5, 1, 'LABORATORY', '', '', 5, 5, 5, 5, false),
(6, 2, 'LABORATORY', '', '', 4, 4, 5, 6, false),
(7, 2, 'PRACTICAL', '', '', 6, 6, 6, 6, false),
(8, 2, 'LABORATORY', 'Biology 1', '', 6, 4, 4, 6, true),
(9, 2, 'LABORATORY', 'Biology 1', '', 6, 5, 4, 6, true);

insert into "public".schedules(id, day_of_week, evenodd, lesson_id, period_id, room_id) VALUES
(4, 'MONDAY', 'EVEN', 4, 4, 4),
(5, 'MONDAY', 'ODD', 5, 5, 5),
(6, 'MONDAY', 'ODD', 4, 5, 6),
(7, 'MONDAY', 'ODD', 6, 6, 6);
