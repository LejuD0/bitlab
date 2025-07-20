CREATE TABLE chapters (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          "order" INT,
                          created_time TIMESTAMP NOT NULL,
                          updated_time TIMESTAMP NOT NULL,
                          course_id BIGINT NOT NULL,
                          CONSTRAINT fk_chapter_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);
