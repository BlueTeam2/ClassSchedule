TRUNCATE
    "public".rooms,
    "public".room_types
    RESTART IDENTITY
    CASCADE;

insert into "public".room_types(id, description) values
(4, 'small auditory'),
(5, 'medium auditory'),
(6, 'big auditory');