--Create some test data

--clear data
TRUNCATE TABLE tasks RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;

-- Create 2 users
INSERT INTO users (username, password, email) VALUES
('alice', 'password123', 'alice@example.com'),
('bob', 'password123', 'bob@example.com');

-- Create 3 tasks for user 1
INSERT INTO tasks (title, description, priority, status, user_id) VALUES
('Finish report', 'Complete the quarterly report', 'HIGH', 'NOT_STARTED', 1),
('Setup database', 'Initialize Postgres with seed data', 'MEDIUM', 'IN_PROGRESS', 1),
('Code review', 'Review PR #42', 'LOW', 'COMPLETED', 2);