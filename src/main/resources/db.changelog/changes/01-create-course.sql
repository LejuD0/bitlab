CREATE TABLE courses (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description TEXT,
                         created_time TIMESTAMP NOT NULL,
                         updated_time TIMESTAMP NOT NULL
);
