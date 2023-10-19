TRUNCATE
    "public".semester_day,
    "public".semester_period,
    "public".periods
    RESTART IDENTITY
    CASCADE;

insert into "public".periods(id, end_time, name, start_time) VALUES
(4, '2020-04-15 02:00', '1 para', '2020-04-15 01:00'),
(5, '2020-04-15 04:00', '2 para', '2020-04-15 03:00'),
(6, '2020-04-15 06:00', '3 para', '2020-04-15 05:00'),
(7, '2020-04-15 08:00', '4 para', '2020-04-15 07:00');