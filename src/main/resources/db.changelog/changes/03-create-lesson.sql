CREATE TABLE lessons (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description TEXT,
                         content TEXT,
                         "order" INT,
                         created_time TIMESTAMP NOT NULL,
                         updated_time TIMESTAMP NOT NULL,
                         chapter_id BIGINT NOT NULL,
                         CONSTRAINT fk_lesson_chapter FOREIGN KEY (chapter_id) REFERENCES chapters(id) ON DELETE CASCADE
);
