-- Clear data
TRUNCATE TABLE tasks RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;

-- Create 2 users
INSERT INTO users (username, password, email) VALUES
('alice', 'password123', 'alice@example.com'),
('bob', 'password123', 'bob@example.com');

-- Create tasks for user 1 (alice)
INSERT INTO tasks (title, description, priority, status, user_id) VALUES
('Finish report', 'Complete the quarterly report', 'HIGH', 'NOT_STARTED', 1),
('Setup database', 'Initialize Postgres with seed data', 'MEDIUM', 'IN_PROGRESS', 1),
('Code review', 'Review PR #42', 'LOW', 'COMPLETED', 1);

-- Create tasks for user 2 (bob)
INSERT INTO tasks (title, description, priority, status, user_id) VALUES
('Prepare presentation', 'Slides for Monday meeting', 'HIGH', 'IN_PROGRESS', 2),
('Write documentation', 'Document API endpoints', 'MEDIUM', 'NOT_STARTED', 2);