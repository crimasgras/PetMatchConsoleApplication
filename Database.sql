
-- PetMatch — Pet Adoption Database

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    email   VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE pets (
    pet_id   SERIAL PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    species  VARCHAR(50) NOT NULL,
    adopted  BOOLEAN DEFAULT FALSE
);

CREATE TABLE adoptions (
    adoption_id SERIAL PRIMARY KEY,
    user_id     INT NOT NULL,
    pet_id      INT NOT NULL UNIQUE,
    adopted_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (pet_id)  REFERENCES pets(pet_id)
);

INSERT INTO users (name, email) VALUES
('Ana Pop', 'ana@test.com'),
('Mihai Ionescu', 'mihai@test.com'),
('Maria Popa', 'maria@test.com');


INSERT INTO pets (name, species) VALUES
('Rex', 'Dog'),
('Whiskers', 'Cat'),
('Buddy', 'Dog'),
('Luna', 'Cat');


INSERT INTO adoptions (user_id, pet_id) VALUES
(1, 1),  -- Ana adopts Rex
(2, 2);  -- Mihai adopts Whiskers


UPDATE pets SET adopted = TRUE
WHERE pet_id IN (1, 2);

-- Query: who adopted which pet
SELECT 
    u.name    AS user_name,
    p.name    AS pet_name,
    p.species,
    a.adopted_at
FROM adoptions a
JOIN users u ON a.user_id = u.user_id
JOIN pets  p ON a.pet_id  = p.pet_id;

-- Add timestamps to existing tables
ALTER TABLE pets  ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE users ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Add additional columns
ALTER TABLE pets  ADD COLUMN age INT;
ALTER TABLE users ADD COLUMN phone VARCHAR(20);

-- Index for faster species search
CREATE INDEX idx_pets_species ON pets(species);

-- Index for faster adoption lookup by user
CREATE INDEX idx_adoptions_user ON adoptions(user_id);


CREATE VIEW adoption_summary AS
SELECT
    u.name     AS user_name,
    u.email,
    p.name     AS pet_name,
    p.species,
    a.adopted_at
FROM adoptions a
JOIN users u ON a.user_id = u.user_id
JOIN pets  p ON a.pet_id  = p.pet_id;

CREATE TABLE quiz_results (
    result_id   SERIAL PRIMARY KEY,
    recommended VARCHAR(50) NOT NULL,
    taken_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


INSERT INTO pets (name, species, age) VALUES
('Fluffy', 'Rabbit', 2),
('Tweety', 'Bird', 1),
('Nemo', 'Fish', 1),
('Bugs', 'Rabbit', 3),
('Parrot', 'Bird', 4);

SELECT * FROM quiz_results;


INSERT INTO users (name, email, phone) VALUES
('Elena Pop', 'elena@test.com', '0721000001'),
('Andrei Ion', 'andrei@test.com', '0721000002'),
('Maria Popa', 'mariaaa@test.com', '0721000003'),
('Alex Marin', 'alex@test.com', '0721000004'),
('Ioana Stan', 'ioana@test.com', '0721000005');


INSERT INTO pets (name, species, age) VALUES
('Max', 'Dog', 3),
('Bella', 'Dog', 1),
('Charlie', 'Dog', 5),
('Milo', 'Cat', 2),
('Luna', 'Cat', 4),
('Simba', 'Cat', 1),
('Coco', 'Rabbit', 2),
('Daisy', 'Rabbit', 1),
('Tweety', 'Bird', 3),
('Rio', 'Bird', 2),
('Nemo', 'Fish', 1),
('Dory', 'Fish', 1);

-- Reset all tables (order matters due to foreign keys)
DELETE FROM quiz_results;
DELETE FROM adoptions;
DELETE FROM pets;
DELETE FROM users;

-- Reset auto-increment sequences
ALTER SEQUENCE users_user_id_seq     RESTART WITH 1;
ALTER SEQUENCE pets_pet_id_seq       RESTART WITH 1;
ALTER SEQUENCE adoptions_adoption_id_seq RESTART WITH 1;
ALTER SEQUENCE quiz_results_result_id_seq RESTART WITH 1;

-- Final seed data
INSERT INTO users (name, email, phone) VALUES
('Ana Pop',       'anapop@gmail.com',          '0721567601'),
('Mihai Ionescu', 'mihaiionescu@gmail.com',     '0721562236'),
('Maria Popa',    'mariapopa@yahoo.com',         '0721078523'),
('Alex Marin',    'alexxx345@gmail.com',         '0721345004'),
('Ioana Stan',    'ioanastan@yahoo.com',          '0721776985'),
('Erika Tamas',   'erikatamas@yahoo.com',         '0721056725'),
('Ionut Coman',   'ionutcoman@yahoo.com',         '0721056726');

INSERT INTO pets (name, species, age) VALUES
('Max',     'Dog',    3),
('Bella',   'Dog',    1),
('Charlie', 'Dog',    5),
('Milo',    'Cat',    2),
('Luna',    'Cat',    4),
('Simba',   'Cat',    1),
('Coco',    'Rabbit', 2),
('Daisy',   'Rabbit', 1),
('Tweety',  'Bird',   3),
('Rio',     'Bird',   2),
('Nemo',    'Fish',   1),
('Dory',    'Fish',   1);
