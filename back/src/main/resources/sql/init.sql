SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE mdd.subscription;
TRUNCATE TABLE mdd.comment;
TRUNCATE TABLE mdd.post;
TRUNCATE TABLE mdd.subject;
TRUNCATE TABLE mdd.user;
SET FOREIGN_KEY_CHECKS = 1;

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
    ('Introduction to JS', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 1, 1),
    ('Angular Components', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 2, 2),
    ('Python Tips', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 3, 2),
    ('RxJS Observables', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 2, 1);

INSERT INTO mdd.subscription (user_id, subject_id)
VALUES
(1, 2),
(2, 1),
(1, 3),
(2, 3);

INSERT INTO mdd.comment (content, post_id, author_id)
VALUES
    ('Très bon article', 1, 2),
    ('Merci pour les explications', 1, 1),
    ('Angular devient plus clair maintenant', 2, 1),
    ('Article très utile', 4, 2);
