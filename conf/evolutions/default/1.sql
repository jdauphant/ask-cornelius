# questions schema

# --- !Ups
CREATE SEQUENCE users_id_seq START WITH 30000;
CREATE SEQUENCE questions_id_seq START WITH 20000;

CREATE TABLE users (
    id integer NOT NULL DEFAULT nextval('users_id_seq'),
    registration_date date,
    PRIMARY KEY (id)
);

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

CREATE TABLE answers (
    question_id integer NOT NULL,
    user_id integer NOT NULL,
    choice char,
    creation_date date,
    PRIMARY KEY (question_id,user_id)
);
CREATE INDEX answers_user_id_index ON answers (user_id)

# --- !Downs

DROP TABLE answers;
DROP TABLE users;
DROP TABLE questions;
DROP SEQUENCE users_id_seq;
DROP SEQUENCE questions_id_seq;