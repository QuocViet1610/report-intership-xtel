use project1;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    timestamp DATE
);

CREATE TABLE Role (
    name VARCHAR(255) PRIMARY KEY,
    description VARCHAR(255)
);

CREATE TABLE user_role (
    user_id BIGINT,
    role_name VARCHAR(255),
    PRIMARY KEY (user_id, role_name),
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_name) REFERENCES Role(name) ON DELETE CASCADE
);

ALTER TABLE users
CHANGE userName user_name VARCHAR(50);
