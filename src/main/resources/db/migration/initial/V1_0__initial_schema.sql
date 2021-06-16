CREATE TABLE IF NOT EXISTS genres
(
    id  SERIAL PRIMARY KEY,
    name      VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS countries
(
    id  SERIAL PRIMARY KEY,
    name        VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS movies
(
    id    SERIAL PRIMARY KEY,
    name_russian VARCHAR(200) UNIQUE NOT NULL,
    name_native  VARCHAR(200) UNIQUE NOT NULL,
    year_of_release INTEGER,
    description VARCHAR(1000),
    rating      DOUBLE PRECISION,
    price       DOUBLE PRECISION,
    picture_path VARCHAR(500) NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id  SERIAL PRIMARY KEY,
    nickname VARCHAR(200) UNIQUE NOT NULL,
    email    VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(200) NOT NULL,
    salt     VARCHAR(500) UNIQUE NOT NULL,
    role     VARCHAR(50)  NOT NULL
);

CREATE TABLE IF NOT EXISTS movies_genres
(
    movie_id bigint not null
        constraint movie_genre_movies__fk
            references movies,
    genre_id bigint not null
        constraint movie_genre_genres__fk
            references genres
);

INSERT INTO genres (name) VALUES ('драма');
INSERT INTO genres (name) VALUES ('криминал');
INSERT INTO genres (name) VALUES ('фэнтези');
INSERT INTO genres (name) VALUES ('детектив');
INSERT INTO genres (name) VALUES ('мелодрама');
INSERT INTO genres (name) VALUES ('биография');
INSERT INTO genres (name) VALUES ('комедия');
INSERT INTO genres (name) VALUES ('фантастика');
INSERT INTO genres (name) VALUES ('боевик');
INSERT INTO genres (name) VALUES ('триллер');
INSERT INTO genres (name) VALUES ('приключения');
INSERT INTO genres (name) VALUES ('аниме');
INSERT INTO genres (name) VALUES ('мультфильм');
INSERT INTO genres (name) VALUES ('семейный');
INSERT INTO genres (name) VALUES ('вестерн');

INSERT INTO countries (name) VALUES ('США');
INSERT INTO countries (name) VALUES ('Франция');
INSERT INTO countries (name) VALUES ('Великобритания');
INSERT INTO countries (name) VALUES ('Италия');
INSERT INTO countries (name) VALUES ('Германия');
INSERT INTO countries (name) VALUES ('Япония');
INSERT INTO countries (name) VALUES ('Испания');






