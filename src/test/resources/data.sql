CREATE TABLE IF NOT EXISTS movies(id SERIAL PRIMARY KEY, name_russian VARCHAR(200) UNIQUE NOT NULL, name_native VARCHAR(200) UNIQUE NOT NULL, year_of_release INTEGER, description VARCHAR(1000), rating DOUBLE PRECISION, price DOUBLE PRECISION, picture_path VARCHAR(500) NOT NULL);
