-- Предположим, глава с id = 1 (из insert выше)
INSERT INTO lessons (name, description, content, "order", created_time, updated_time, chapter_id)
VALUES
    (
        'Lecture: If-Else in Java',
        'Theory part of conditional statements in Java.',
        'In Java, "if-else" is used to perform conditional logic. Syntax:\n\nif (condition) {\n  // code\n} else {\n  // code\n}',
        1,
        NOW(), NOW(),
        1
    ),
    (
        'Practice: If-Else Tasks',
        'Exercises for practicing conditional logic.',
        '1. Write a program that checks if a number is even.\n2. Write a program that checks whether a number is positive, negative, or zero.',
        2,
        NOW(), NOW(),
        1
    );
