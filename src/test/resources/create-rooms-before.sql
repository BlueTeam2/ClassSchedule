TRUNCATE
    "public".rooms,
    "public".room_types
    RESTART IDENTITY
    CASCADE;

insert into "public".room_types(id, description) values
(4,'small auditory'),
(5,'medium auditory'),
(6, 'big auditory');

insert into "public".rooms(id, name, sort_order, room_type_id) values
(4, 'Laboratory', 1, 4),
(5, 'Practical room', 2, 5),
(6, 'Lecture room', 3, 6);