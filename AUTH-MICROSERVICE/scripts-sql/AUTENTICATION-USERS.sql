INSERT INTO AuthenticationDB.role (role_name)
VALUES 
("ROLE_USER"),
("ROLE_ADMIN"),
("ROLE_CAF_COORDINATOR"),
("ROLE_SPORTSMAN"),
("ROLE_WELLBEING_DIRECTOR");

INSERT INTO AuthenticationDB.auth_user (is_active, is_user_verified, password, user_name) VALUES 
(1, 0, "DEFAULT", "laura@uptc.edu.co"),
(1, 0, "DEFAULT", "carlos@uptc.edu.co"),
(1, 0, "DEFAULT", "maria@uptc.edu.co"),
(1, 0, "DEFAULT", "andres@uptc.edu.co"),
(1, 0, "DEFAULT", "luisa@uptc.edu.co"),
(1, 0, "DEFAULT", "diana@uptc.edu.co"),
(1, 0, "DEFAULT", "jorge@uptc.edu.co"),
(1, 0, "DEFAULT", "camilo@uptc.edu.co"),
(1, 0, "DEFAULT", "sofia@uptc.edu.co"),
(1, 0, "DEFAULT", "diego@uptc.edu.co"),
(1, 0, "DEFAULT", "deportista@uptc.edu.co"),
(1, 0, "DEFAULT", "admin@uptc.edu.co"),
(1, 0, "DEFAULT", "coordinador@uptc.edu.co"),
(1, 0, "DEFAULT", "director@uptc.edu.co");
