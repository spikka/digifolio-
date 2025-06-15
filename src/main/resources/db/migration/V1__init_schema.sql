-- роли и пользователи
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    group_name VARCHAR(100),
    faculty VARCHAR(100),
    avatar_path VARCHAR(255),
    bio TEXT,
    visibility VARCHAR(10) DEFAULT 'PUBLIC'
);

CREATE TABLE user_roles (
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id INT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY(user_id, role_id)
);

-- достижения
CREATE TABLE achievements (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL REFERENCES users(id),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    type VARCHAR(50),
    date DATE,
    tags VARCHAR(500),
    file_path VARCHAR(255)
);

CREATE TABLE comments (
    id SERIAL PRIMARY KEY,
    achievement_id INT NOT NULL REFERENCES achievements(id) ON DELETE CASCADE,
    author_id INT NOT NULL REFERENCES users(id),
    text TEXT,
    created_at DATE DEFAULT CURRENT_DATE
);

CREATE TABLE ratings (
    id SERIAL PRIMARY KEY,
    achievement_id INT NOT NULL REFERENCES achievements(id) ON DELETE CASCADE,
    author_id INT NOT NULL REFERENCES users(id),
    stars INT CHECK (stars BETWEEN 1 AND 5),
    created_at DATE DEFAULT CURRENT_DATE
);

-- расширения
CREATE TABLE education_history (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL REFERENCES users(id),
    institution VARCHAR(255),
    city VARCHAR(100),
    from_date DATE,
    to_date DATE
);

CREATE TABLE work_history (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL REFERENCES users(id),
    company VARCHAR(255),
    position VARCHAR(255),
    from_date DATE,
    to_date DATE
);

CREATE TABLE certificates (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL REFERENCES users(id),
    name VARCHAR(255),
    issuer VARCHAR(255),
    date DATE,
    file_path VARCHAR(255)
);

CREATE TABLE projects (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL REFERENCES users(id),
    title VARCHAR(255),
    description TEXT,
    link VARCHAR(500),
    file_path VARCHAR(255)
);

CREATE TABLE skills (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL REFERENCES users(id),
    name VARCHAR(100)
);

CREATE TABLE interests (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL REFERENCES users(id),
    name VARCHAR(100)
);

-- индексы
CREATE INDEX idx_achievements_student ON achievements(student_id);
CREATE INDEX idx_comments_achv ON comments(achievement_id);
CREATE INDEX idx_ratings_achv ON ratings(achievement_id);
