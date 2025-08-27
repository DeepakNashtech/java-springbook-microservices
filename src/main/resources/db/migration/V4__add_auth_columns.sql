ALTER TABLE users
    ADD COLUMN IF NOT EXISTS password VARCHAR(255),
    ADD COLUMN IF NOT EXISTS roles VARCHAR(255) DEFAULT 'ROLE_USER',
    ADD COLUMN IF NOT EXISTS enabled BOOLEAN NOT NULL DEFAULT TRUE;

-- Optional: make email the credential username
CREATE UNIQUE INDEX IF NOT EXISTS ux_users_email ON users(email);
