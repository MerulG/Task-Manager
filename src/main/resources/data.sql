-- Users (idempotent via unique constraints)
INSERT INTO users (username, password, email, role)
VALUES ('alice', 'password123', 'alice@example.com', 'USER')
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password, email, role)
VALUES ('bob', 'password123', 'bob@example.com', 'USER')
ON CONFLICT (username) DO NOTHING;

-- Tasks for Alice (no unique constraint, so we guard with NOT EXISTS)
INSERT INTO tasks (title, description, priority, status, user_id)
SELECT
  'Finish report',
  'Complete the quarterly report',
  'HIGH',
  'NOT_STARTED',
  u.id
FROM users u
WHERE u.username = 'alice'
AND NOT EXISTS (
  SELECT 1
  FROM tasks t
  WHERE t.user_id = u.id
    AND t.title = 'Finish report'
    AND t.description = 'Complete the quarterly report'
    AND t.priority = 'HIGH'
    AND t.status = 'NOT_STARTED'
);

INSERT INTO tasks (title, description, priority, status, user_id)
SELECT
  'Setup database',
  'Initialize Postgres with seed data',
  'MEDIUM',
  'IN_PROGRESS',
  u.id
FROM users u
WHERE u.username = 'alice'
AND NOT EXISTS (
  SELECT 1
  FROM tasks t
  WHERE t.user_id = u.id
    AND t.title = 'Setup database'
    AND t.description = 'Initialize Postgres with seed data'
    AND t.priority = 'MEDIUM'
    AND t.status = 'IN_PROGRESS'
);

INSERT INTO tasks (title, description, priority, status, user_id)
SELECT
  'Code review',
  'Review PR #42',
  'LOW',
  'COMPLETED',
  u.id
FROM users u
WHERE u.username = 'alice'
AND NOT EXISTS (
  SELECT 1
  FROM tasks t
  WHERE t.user_id = u.id
    AND t.title = 'Code review'
    AND t.description = 'Review PR #42'
    AND t.priority = 'LOW'
    AND t.status = 'COMPLETED'
);

-- Tasks for Bob
INSERT INTO tasks (title, description, priority, status, user_id)
SELECT
  'Prepare presentation',
  'Slides for Monday meeting',
  'HIGH',
  'IN_PROGRESS',
  u.id
FROM users u
WHERE u.username = 'bob'
AND NOT EXISTS (
  SELECT 1
  FROM tasks t
  WHERE t.user_id = u.id
    AND t.title = 'Prepare presentation'
    AND t.description = 'Slides for Monday meeting'
    AND t.priority = 'HIGH'
    AND t.status = 'IN_PROGRESS'
);

INSERT INTO tasks (title, description, priority, status, user_id)
SELECT
  'Write documentation',
  'Document API endpoints',
  'MEDIUM',
  'NOT_STARTED',
  u.id
FROM users u
WHERE u.username = 'bob'
AND NOT EXISTS (
  SELECT 1
  FROM tasks t
  WHERE t.user_id = u.id
    AND t.title = 'Write documentation'
    AND t.description = 'Document API endpoints'
    AND t.priority = 'MEDIUM'
    AND t.status = 'NOT_STARTED'
);