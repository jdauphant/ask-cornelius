# questions schema

# --- !Ups
CREATE SEQUENCE questions_id_seq START WITH 20000;
CREATE TABLE questions (
    id integer NOT NULL DEFAULT nextval('questions_id_seq'),
    title varchar(255),
    choice_A varchar(255),
    choice_B varchar(255),
    author_id integer,
    total_A integer,
    total_B integer,
    creation_date date,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE questions;
DROP SEQUENCE questions_id_seq;