-- Предположим, курс с id = 1 (из insert выше)
INSERT INTO chapters (name, description, "order", created_time, updated_time, course_id)
VALUES (
           'If-Else Conditions',
           'Control flow statements: if, else, else-if and nested conditions.',
           1,
           NOW(), NOW(),
           1
       );
