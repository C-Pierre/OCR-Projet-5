TRUNCATE TABLE comments, posts, subscriptions, users, subjects RESTART IDENTITY CASCADE;

INSERT INTO users (user_name, email, password)
VALUES
    ('UserOne', 'user.one@mail.com', '$2a$12$L0ckABtnJfZncWkbAPP2G.dSEvZ2.AduLO8aq2VfJ7nFrSOklLRi6'),
    ('UserTwo', 'user.two@mail.com', '$2a$12$L0ckABtnJfZncWkbAPP2G.dSEvZ2.AduLO8aq2VfJ7nFrSOklLRi6');

INSERT INTO subjects (name, description)
VALUES
('JavaScript', 'Langage de programmation côté client'),
('Angular', 'Framework frontend basé sur TypeScript'),
('Python', 'Langage de programmation polyvalent');

INSERT INTO posts (title, content, subject_id, author_id)
VALUES
    ('Introduction to JS', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 1, 1),
    ('Angular Components', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 2, 2),
    ('Python Tips', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 3, 2),
    ('RxJS Observables', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 2, 1);

INSERT INTO subscriptions (user_id, subject_id)
VALUES
(1, 2),
(2, 1),
(1, 3),
(2, 3);

INSERT INTO comments (content, post_id, author_id)
VALUES
    ('Très bon article', 1, 2),
    ('Merci pour les explications', 1, 1),
    ('Angular devient plus clair maintenant', 2, 1),
    ('Article très utile', 4, 2);
