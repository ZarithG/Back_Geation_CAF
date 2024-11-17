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

-- Departments
INSERT INTO UsersDB.departments (name)
VALUES
	('ANTIOQUIA'),
	('ATLÁNTICO'),
	('BOGOTÁ, D.C.'),
	('BOLÍVAR'),
	('BOYACÁ'),
	('CALDAS'),
	('CAQUETÁ'),
	('CAUCA'),
	('CESAR'),
	('CÓRDOBA'),
	('CUNDINAMARCA'),
	('CHOCÓ'),
	('HUILA'),
	('LA GUAJIRA'),
	('MAGDALENA'),
	('META'),
	('NARIÑO'),
	('NORTE DE SANTANDER'),
	('QUINDIO'),
	('RISARALDA'),
	('SANTANDER'),
	('SUCRE'),
	('TOLIMA'),
	('VALLE DEL CAUCA'),
	('ARAUCA'),
	('CASANARE'),
	('PUTUMAYO'),
	('ARCHIPIÉLAGO DE SAN ANDRÉS, PROVIDENCIA Y SANTA CATALINA'),
	('AMAZONAS'),
	('GUAINÍA'),
	('GUAVIARE'),
	('VAUPÉS'),
	('VICHADA');

-- Boyaca cities
INSERT INTO UsersDB.city (name, department_id)
VALUES 
('Tununguá', 5),
('Motavita', 5),
('Ciénega', 5),
('Tunja', 5),
('Almeida', 5),
('Aquitania', 5),
('Arcabuco', 5),
('Berbeo', 5),
('Betéitiva', 5),
('Boavita', 5),
('Boyacá', 5),
('Briceño', 5),
('Buena Vista', 5),
('Busbanzá', 5),
('Caldas', 5),
('Campohermoso', 5),
('Cerinza', 5),
('Chinavita', 5),
('Chiquinquirá', 5),
('Chiscas', 5),
('Chita', 5),
('Chitaraque', 5),
('Chivatá', 5),
('Cómbita', 5),
('Coper', 5),
('Corrales', 5),
('Covarachía', 5),
('Cubará', 5),
('Cucaita', 5),
('Cuítiva', 5),
('Chíquiza', 5),
('Chivor', 5),
('Duitama', 5),
('El Cocuy', 5),
('El Espino', 5),
('Firavitoba', 5),
('Floresta', 5),
('Gachantivá', 5),
('Gameza', 5),
('Garagoa', 5),
('Guacamayas', 5),
('Guateque', 5),
('Guayatá', 5),
('Güicán', 5),
('Iza', 5),
('Jenesano', 5),
('Jericó', 5),
('Labranzagrande', 5),
('La Capilla', 5),
('La Victoria', 5),
('Macanal', 5),
('Maripí', 5),
('Miraflores', 5),
('Mongua', 5),
('Monguí', 5),
('Moniquirá', 5),
('Muzo', 5),
('Nobsa', 5),
('Nuevo Colón', 5),
('Oicatá', 5),
('Otanche', 5),
('Pachavita', 5),
('Páez', 5),
('Paipa', 5),
('Pajarito', 5),
('Panqueba', 5),
('Pauna', 5),
('Paya', 5),
('Pesca', 5),
('Pisba', 5),
('Puerto Boyacá', 5),
('Quípama', 5),
('Ramiriquí', 5),
('Ráquira', 5),
('Rondón', 5),
('Saboyá', 5),
('Sáchica', 5),
('Samacá', 5),
('San Eduardo', 5),
('San Mateo', 5),
('Santana', 5),
('Santa María', 5),
('Santa Sofía', 5),
('Sativanorte', 5),
('Sativasur', 5),
('Siachoque', 5),
('Soatá', 5),
('Socotá', 5),
('Socha', 5),
('Sogamoso', 5),
('Somondoco', 5),
('Sora', 5),
('Sotaquirá', 5),
('Soracá', 5),
('Susacón', 5),
('Sutamarchán', 5),
('Sutatenza', 5),
('Tasco', 5),
('Tenza', 5),
('Tibaná', 5),
('Tinjacá', 5),
('Tipacoque', 5),
('Toca', 5),
('Tópaga', 5),
('Tota', 5),
('Turmequé', 5),
('Tutazá', 5),
('Umbita', 5),
('Ventaquemada', 5),
('Viracachá', 5),
('Zetaquira', 5),
('Togüí', 5),
('Villa de Leyva', 5),
('Paz de Río', 5),
('Santa Rosa de Viterbo', 5),
('San Pablo de Borbur', 5),
('San Luis de Gaceno', 5),
('San José de Pare', 5),
('San Miguel de Sema', 5),
('Tuta', 5),
('Tibasosa', 5),
('La Uvita', 5),
('Belén', 5);

