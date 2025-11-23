-- Create users if they don't exist
INSERT INTO users (username, password, email) VALUES
('alice', 'password123', 'alice@example.com')
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password, email) VALUES
('bob', 'password123', 'bob@example.com')
ON CONFLICT (username) DO NOTHING;

-- Insert tasks for Alice (user_id = 1) if they don't already exist
INSERT INTO tasks (title, description, priority, status, user_id) VALUES
('Finish report', 'Complete the quarterly report', 'HIGH', 'NOT_STARTED', 1)
ON CONFLICT (title, user_id) DO NOTHING;

INSERT INTO tasks (title, description, priority, status, user_id) VALUES
('Setup database', 'Initialize Postgres with seed data', 'MEDIUM', 'IN_PROGRESS', 1)
ON CONFLICT (title, user_id) DO NOTHING;

INSERT INTO tasks (title, description, priority, status, user_id) VALUES
('Code review', 'Review PR #42', 'LOW', 'COMPLETED', 1)
ON CONFLICT (title, user_id) DO NOTHING;

-- Insert tasks for Bob (user_id = 2) if they don't already exist
INSERT INTO tasks (title, description, priority, status, user_id) VALUES
('Prepare presentation', 'Slides for Monday meeting', 'HIGH', 'IN_PROGRESS', 2)
ON CONFLICT (title, user_id) DO NOTHING;

INSERT INTO tasks (title, description, priority, status, user_id) VALUES
('Write documentation', 'Document API endpoints', 'MEDIUM', 'NOT_STARTED', 2)
ON CONFLICT (title, user_id) DO NOTHING;