TRUNCATE
    "public".lessons,
    "public".subjects
    RESTART IDENTITY
    CASCADE;

insert into "public".subjects(id, name) VALUES
(4, 'Biology'),
(5, 'History'),
(6, 'Astronomy');