CREATE TABLE Person (
    id SERIAL PRIMARY KEY,
    full_name varchar(100) NOT NULL UNIQUE,
    year_of_birth int NOT NULL
);

CREATE TABLE Book (
    id SERIAL PRIMARY KEY,
    title varchar(100) NOT NULL,
    author varchar(100) NOT NULL,
    year int NOT NULL,
    person_id int REFERENCES Person(id) ON DELETE SET NULL -- для списания с пользователя книги
);