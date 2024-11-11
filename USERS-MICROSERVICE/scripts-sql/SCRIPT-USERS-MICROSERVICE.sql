INSERT INTO UsersDB.faculty (faculty_name) VALUES 
('Ciencias agropecuarias'),
('Ciencias'),
('Ciencias de la educación'),
('Ciencias económicas y administrativas'),
('Ciencias de la salud'),
('Derecho'),
('Ingeniería'),
('Estudios a distancia'),
('Chiquinquirá'),
('Duitama'),
('Sogamoso');

-- Ciencias agropecuarias
INSERT INTO UsersDB.program (program_name, faculty_id) VALUES 
('Ingeniería Agronómica', (SELECT id FROM UsersDB.faculty WHERE faculty_name = 'Ciencias agropecuarias')),
('Zootecnia', (SELECT id FROM UsersDB.faculty WHERE faculty_name = 'Ciencias agropecuarias'));

-- Ciencias
INSERT INTO UsersDB.program (program_name, faculty_id) VALUES 
('Biología', (SELECT id FROM UsersDB.faculty WHERE faculty_name = 'Ciencias')),
('Química', (SELECT id FROM UsersDB.faculty WHERE faculty_name = 'Ciencias'));

-- Ciencias de la educación
INSERT INTO UsersDB.program (program_name, faculty_id) VALUES 
('Licenciatura en Matemáticas', (SELECT id FROM UsersDB.faculty WHERE faculty_name = 'Ciencias de la educación')),
('Licenciatura en Español e Inglés', (SELECT id FROM UsersDB.faculty WHERE faculty_name = 'Ciencias de la educación'));

-- Ciencias económicas y administrativas
INSERT INTO UsersDB.program (program_name, faculty_id) VALUES 
('Economía', (SELECT id FROM UsersDB.faculty WHERE faculty_name = 'Ciencias económicas y administrativas')),
('Administración de Empresas', (SELECT id FROM UsersDB.faculty WHERE faculty_name = 'Ciencias económicas y administrativas'));

-- Ciencias de la salud
INSERT INTO UsersDB.program (program_name, faculty_id) VALUES 
('Medicina', (SELECT id FROM UsersDB.faculty WHERE faculty_name = 'Ciencias de la salud')),
('Enfermería', (SELECT id FROM UsersDB.faculty WHERE faculty_name = 'Ciencias de la salud'));

-- Derecho
INSERT INTO UsersDB.program (program_name, faculty_id) VALUES 
('Derecho', (SELECT id FROM UsersDB.faculty WHERE faculty_name = 'Derecho'));

-- Ingeniería
INSERT INTO UsersDB.program (program_name, faculty_id) VALUES 
('Ingeniería Civil', (SELECT id FROM UsersDB.faculty WHERE faculty_name = 'Ingeniería')),
('Ingeniería Electrónica', (SELECT id FROM UsersDB.faculty WHERE faculty_name = 'Ingeniería')),
('Ingeniería de Sistemas', (SELECT id FROM UsersDB.faculty WHERE faculty_name = 'Ingeniería'));

-- Estudios a distancia
INSERT INTO UsersDB.program (program_name, faculty_id) VALUES 
('Administración Pública', (SELECT id FROM UsersDB.faculty WHERE faculty_name = 'Estudios a distancia'));

-- Chiquinquirá
INSERT INTO UsersDB.program (program_name, faculty_id) VALUES 
('Licenciatura en Educación Básica con Énfasis en Humanidades e Idiomas', (SELECT id FROM UsersDB.faculty WHERE faculty_name = 'Chiquinquirá'));

-- Duitama
INSERT INTO UsersDB.program (program_name, faculty_id) VALUES 
('Ingeniería Mecánica', (SELECT id FROM UsersDB.faculty WHERE faculty_name = 'Duitama'));

-- Sogamoso
INSERT INTO UsersDB.program (program_name, faculty_id) VALUES 
('Ingeniería de Minas', (SELECT id FROM UsersDB.faculty WHERE faculty_name = 'Sogamoso')),
('Ingeniería Geológica', (SELECT id FROM UsersDB.faculty WHERE faculty_name = 'Sogamoso'));
