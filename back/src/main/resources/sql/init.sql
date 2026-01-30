DROP DATABASE IF EXISTS mdd;

CREATE DATABASE IF NOT EXISTS mdd CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mdd;

CREATE TABLE IF NOT EXISTS `user` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `subject` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `post` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    subject_id INT NOT NULL,
    author_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (subject_id) REFERENCES mdd.subject(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES mdd.user(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `subscription` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    subject_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES mdd.user(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES mdd.subject(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `comment` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    post_id INT NOT NULL,
    author_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_comment_post
    FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_author
    FOREIGN KEY (author_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB;

ALTER TABLE mdd.subject
    ADD CONSTRAINT uq_subject_name UNIQUE (name);

INSERT IGNORE INTO mdd.user (user_name, email, password)
VALUES
    ('UserOne', 'user.one@mail.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq'),
    ('UserTwo', 'user.two@mail.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq');

INSERT INTO mdd.subject (name, description)
VALUES
('JavaScript', 'Langage de programmation côté client'),
('Angular', 'Framework frontend basé sur TypeScript'),
('Python', 'Langage de programmation polyvalent');

INSERT INTO mdd.post (title, content, subject_id, author_id)
VALUES
    ('Introduction to JS', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 1, 1),
    ('Angular Components', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 2, 2),
    ('Python Tips', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 3, 2),
    ('RxJS Observables', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 2, 1);

INSERT INTO mdd.subscription (user_id, subject_id)
VALUES
(1, 2),
(2, 1),
(1, 3),
(2, 3);

INSERT INTO comment (content, post_id, author_id)
VALUES
    ('Très bon article', 1, 2),
    ('Merci pour les explications', 1, 1),
    ('Angular devient plus clair maintenant', 2, 1),
    ('Article très utile', 4, 2);
