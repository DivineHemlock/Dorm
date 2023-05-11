CREATE TABLE IF NOT EXISTS manager (
    id varchar(50) NOT NULL,
    full_name varchar(255),
    profile_id varchar(50),
    username varchar(255) unique,
    password varchar(255),
    role varchar(50),
    email varchar(255),
    PRIMARY KEY (id)
);

INSERT INTO manager
(id, username, password, role, full_name, email)
SELECT '1', 'Manager', 'Manager', 'MANAGER', 'مدیر', ' '
    WHERE
    NOT EXISTS (
        SELECT id FROM manager WHERE id = '1'
    );

CREATE TABLE IF NOT EXISTS category (
    id varchar(50) NOT NULL,
    name varchar(255) NOT NULL,
    type varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO category
(id, name, type)
SELECT '1', 'سرپرست', 'Personnel'
    WHERE
    NOT EXISTS (
        SELECT id FROM category WHERE id = '1'
    );

