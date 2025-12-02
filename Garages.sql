-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: localhost    Database: garages
-- ------------------------------------------------------
-- Server version	8.0.43
DROP DATABASE IF EXISTS guarderia_web;
CREATE DATABASE guarderia_web;
USE guarderia_web;

CREATE TABLE `persona` (
    `ID_persona` int NOT NULL AUTO_INCREMENT,
    `DNI` int NOT NULL,
    `nombre` varchar(50) NOT NULL,
    `direccion` varchar(100) NOT NULL,
    `telefono` varchar(25) NOT NULL,
    `contrasenia` varchar(25) NOT NULL,
    `tipo_persona` ENUM('administrador', 'empleado', 'socio') NOT NULL,
    PRIMARY KEY (`ID_persona`)
);

-- Adminsitradores
INSERT INTO `persona`
(`DNI`, `nombre`, `direccion`, `telefono`, `contrasenia`, `tipo_persona`)
VALUES
(202565789, 'Carlos Garcia', 'Charlone 797', '1163778899', 'CarGar789', 'administrador'),
(987654321, 'Ana Rodriguez', 'Av. Corrientes 123', '1154321098', 'AnaRod432', 'administrador');

-- Empleados 
INSERT INTO `persona`
(`DNI`, `nombre`, `direccion`, `telefono`, `contrasenia`, `tipo_persona`)
VALUES
(25567642, 'Mario Mendieta', 'Mayor Irusta 133', '1188922999', 'Muydificil1', 'empleado'),
(30123456, 'Carlos Gómez', 'Avenida Siempre Viva 742', '1145678901', 'CarlosGo_742!', 'empleado'),
(35987654, 'Pedro López', 'Calle Falsa 123', '1123456789', 'P3dr0_10p3z', 'empleado'),
(40333222, 'Hugo Pérez', 'Boulevard de los Sueños 55', '1160001111', 'HugoPerez55$', 'empleado'),
(22111000, 'Javier Ruiz', 'Pasaje Secreto 4B', '1177778888', 'JRuiz_4B', 'empleado'),
(38555444, 'Miguel Martínez', 'Ruta 9 Kilómetro 100', '1132109876', 'M1guelMart', 'empleado'),
(42999888, 'David Díaz', 'Plaza Mayor 22', '1154321098', 'Dav_Diaz22.', 'empleado'),
(28765432, 'Andrés Fernández', 'Callejón Oscuro 1A', '1191234567', 'Andr3sFdz!', 'empleado'),
(33000999, 'Roberto Castro', 'Montevideo 1500', '1180009000', 'RobC_1500', 'empleado'),
(19876543, 'Pablo Sanz', 'Calle del Sol Naciente 88', '1120203030', 'PSanz_88!', 'empleado');

-- Socios
INSERT INTO `persona`
(`DNI`, `nombre`, `direccion`, `telefono`, `contrasenia`, `tipo_persona`)
VALUES
(33456885, 'Robeto Juarez', 'Av Roca 1335', '1198234353', 'Rojuarez444', 'socio'),
(28112345, 'Valeria Paz', 'Calle San Martín 500', '1156781234', 'ValPaz_500', 'socio'),
(39765432, 'Alejandro Soto', 'Las Heras 2200', '1134567890', 'ASoto2200!', 'socio'),
(41009876, 'María Elena Rico', 'Juncal 100', '1178905432', 'MElenaRico', 'socio'),
(24505105, 'Federico Guzmán', 'Salta 45', '1165432109', 'FedeGuzman45', 'socio'),
(30303030, 'Laura Castro', 'Rivadavia 1800', '1140003000', 'LCastro_1800', 'socio'),
(43876543, 'Martín Nievas', 'Defensa 90', '1190908080', 'Martin90D', 'socio');


CREATE TABLE `administrador` (
  `ID_persona` int PRIMARY KEY ,
  FOREIGN KEY (`ID_persona`) REFERENCES `persona` (`ID_persona`)
);

INSERT INTO `administrador` VALUES 
(1),
(2);


CREATE TABLE `especialidad` (
  `ID_especialidad` int NOT NULL AUTO_INCREMENT,
  `tipoEspecialidad` varchar(100) NOT NULL,
  PRIMARY KEY (`ID_especialidad`)
);

INSERT INTO `especialidad` VALUES 
(1,'Lavador'),
(2,'Tecnico'),
(3,'Playero');


CREATE TABLE empleado (
  ID_persona INT PRIMARY KEY,
  ID_especialidad INT NOT NULL,
  FOREIGN KEY (ID_persona) REFERENCES persona(ID_persona),
  FOREIGN KEY (ID_especialidad) REFERENCES especialidad(ID_especialidad)
);

INSERT INTO `empleado` VALUES 
(3,1),
(4,2),
(5,3),
(6,3),
(7,2),
(8,1),
(9,1),
(10,2),
(11,3),
(12,2);


CREATE TABLE socio (
    ID_persona INT NOT NULL,
    fechaIngreso DATE NOT NULL,
    PRIMARY KEY (ID_persona),
    CONSTRAINT FK_socio_persona FOREIGN KEY (ID_persona) REFERENCES persona(ID_persona)
);


INSERT INTO socio (ID_persona, fechaIngreso) VALUES
(13, '2025-11-27'),
(14, '2025-11-27'),
(15, '2025-11-27'),
(16, '2025-11-27'),
(17, '2025-11-27'),
(18, '2025-11-27'),
(19, '2025-11-27');

CREATE TABLE `mantenimiento` (
  `ID_mantenimiento` int NOT NULL AUTO_INCREMENT,
  `tipoMantenimiento` varchar(50) NOT NULL,
  `tipoEspecialidad` varchar(50) NOT NULL,
  PRIMARY KEY (`ID_mantenimiento`)
);

INSERT INTO `mantenimiento` VALUES 
(1,'Cambio de frenos','Mantenimiento de frenos'),
(2,'Cambio de liquido de frenos','Mantenimiento de frenos'),
(3,'Carga de bateria','Cargar vehiculo'),
(4,'Carga de tanque','Cargar vehiculo'),
(5,'Lavado Profundo','Lavar vehiculo'),
(6,'Lavado general','Lavar vehiculo');





CREATE TABLE `vehiculo` (
  `ID_vehiculo` int NOT NULL AUTO_INCREMENT,
  `matricula` varchar(10) unique NOT NULL,
  `marca` varchar(25) NOT NULL,
  `tipo` varchar(25) NOT NULL,
  `ID_mantenimiento` int DEFAULT NULL,
  `ID_socioPropietario` int,
  PRIMARY KEY (`ID_vehiculo`),
  KEY `fk_ID_mantenimiento_idx` (`ID_mantenimiento`),
  KEY `fk_ID_socioPropietario_veh_idx` (`ID_socioPropietario`),
  CONSTRAINT `fk_ID_mantenimiento` FOREIGN KEY (`ID_mantenimiento`) REFERENCES `mantenimiento` (`ID_mantenimiento`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_ID_socioPropietario_veh` FOREIGN KEY (`ID_socioPropietario`) REFERENCES `socio` (`ID_persona`) ON DELETE SET NULL ON UPDATE CASCADE
);

INSERT INTO `vehiculo` VALUES 
(1,'aa125jar','Hyundai','Trailer',5,13),
(2,'MH001JAP','Fiat','Motorhome',1,13),
(3,'CR002ARG','Renault','Casa Rodante de Arrastre',2,13),
(4,'CA003BRZ','Ford','Caravana',3,14),
(5,'TR004USA','Custom','Trailer',4,14),
(6,'MH005GER','Mercedes-Benz','Motorhome',5,15),
(7,'CR006CHL','Al-Ko','Casa Rodante de Arrastre',6,15),
(8,'CA007URY','Hobby','Caravana',1,16),
(9,'TR008MEX','Chasis','Trailer',2,16),
(10,'MH009CAN','Volkswagen','Motorhome',3,17),
(11,'CR010PER','Knaus','Casa Rodante de Arrastre',4,17),
(12,'CA011COL','Adria','Caravana',5,18),
(13,'TR012VEN','Remolque','Trailer',6,18),
(14,'MH013ECU','Iveco','Motorhome',1,19),
(15,'CR014BOL','Swift','Casa Rodante de Arrastre',2,19);

CREATE TABLE `zona` (
  `ID_zona` varchar(1) NOT NULL,
  `tipoVehiculo` varchar(50) NOT NULL,
  `profundidadGarages` float NOT NULL,
  `anchoGarages` float NOT NULL,
  PRIMARY KEY (`ID_zona`)
);

INSERT INTO `zona` VALUES 
('A','Motorhome',15.5,4.5),
('B','Casa Rodante de Arrastre',13,3.8),
('C','Caravana',10.5,3.3),
('D','Trailer',18,3);

CREATE TABLE `garage` (
  `ID_garage` int NOT NULL AUTO_INCREMENT,
  `contadorLuz` int DEFAULT NULL,
  `fechaInicioRenta` date DEFAULT NULL,
  `fechaFinRenta` date DEFAULT NULL,
  `fechaCompra` date DEFAULT NULL,
  `ID_zona` varchar(1) DEFAULT NULL,
  `ID_vehiculoAsignado` int DEFAULT NULL,
  `ID_socioPropietario` int DEFAULT NULL,
  `ID_socioRentado` int DEFAULT NULL,
  PRIMARY KEY (`ID_garage`),
  KEY `fk_ID_zona_idx` (`ID_zona`),
  KEY `fk_ID_vehiculoAsignado_idx` (`ID_vehiculoAsignado`),
  KEY `fk_ID_socioPropietario_idx` (`ID_socioPropietario`),
  KEY `fk_ID_socioRentado_idx` (`ID_socioRentado`),
  CONSTRAINT `fk_ID_vehiculoAsignadoGar` FOREIGN KEY (`ID_vehiculoAsignado`) REFERENCES `vehiculo` (`ID_vehiculo`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_ID_zona` FOREIGN KEY (`ID_zona`) REFERENCES `zona` (`ID_zona`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_ID_socioPropietario` FOREIGN KEY (`ID_socioPropietario`) REFERENCES `socio` (`ID_persona`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_ID_socioRentado` FOREIGN KEY (`ID_socioRentado`) REFERENCES `socio` (`ID_persona`) ON DELETE SET NULL ON UPDATE CASCADE
);

ALTER TABLE `garage` AUTO_INCREMENT = 16;

INSERT INTO `garage` (contadorLuz, fechaInicioRenta, fechaFinRenta, fechaCompra, ID_zona, ID_vehiculoAsignado, ID_socioPropietario, ID_socioRentado) VALUES 
(50,'2025-10-01','2025-11-01',NULL,'D',NULL,NULL,13),
(70,'2025-10-01','2025-11-01',NULL,'B',3,NULL,13),
(90,'2025-10-01','2025-11-01',NULL,'C',4,NULL,14),
(55,'2025-10-01','2025-11-01',NULL,'B',7,NULL,15),
(58,'2025-10-01','2025-11-01',NULL,'C',8,NULL,16),
(50,'2025-10-01','2025-11-01',NULL,'B',11,NULL,17),
(50,'2025-10-01','2025-11-01',NULL,'C',12,NULL,18),
(50,'2025-10-01','2025-11-01',NULL,'A',14,NULL,19),
(50,'2025-10-01','2025-11-01',NULL,'B',NULL,NULL,13),
(250,NULL,NULL,'2025-04-18','A',2,13,NULL),
(520,NULL,NULL,'2024-11-08','D',5,14,NULL),
(500,NULL,NULL,'2024-12-23','A',6,15,NULL),
(510,NULL,NULL,'2025-01-22','D',9,16,NULL),
(512,NULL,NULL,'2025-07-04','A',10,17,NULL),
(50,NULL,NULL,'2025-03-15','D',13,18,NULL);

CREATE TABLE `empleadoasignacion` (
  `ID_orden` int NOT NULL AUTO_INCREMENT,
  `ID_empAsignacion` int DEFAULT NULL,
  `ID_vehiculoAsignado` int DEFAULT NULL,
  `ID_mantenimientoAsignado` int DEFAULT NULL,
  PRIMARY KEY (`ID_orden`),
  KEY `fk_ID_vehiculoAsignado_idx` (`ID_vehiculoAsignado`),
  KEY `fk_ID_empleadoAsignado_idx` (`ID_empAsignacion`),
  KEY `fk_ID_mantenimientoAsignado_idx` (`ID_mantenimientoAsignado`),
  CONSTRAINT `fk_ID_empleadoAsignado` FOREIGN KEY (`ID_empAsignacion`) REFERENCES `empleado` (`ID_persona`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_ID_mantenimientoAsignado` FOREIGN KEY (`ID_mantenimientoAsignado`) REFERENCES `mantenimiento` (`ID_mantenimiento`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_ID_vehiculoAsignado` FOREIGN KEY (`ID_vehiculoAsignado`) REFERENCES `vehiculo` (`ID_vehiculo`) ON DELETE SET NULL ON UPDATE CASCADE
);

INSERT INTO `empleadoasignacion` VALUES 
(1,3,10,5),
(2,3,11,6),
(3,8,1,5),
(4,8,13,6),
(5,9,2,5),
(6,9,12,6),
(7,4,3,1),
(8,4,4,2),
(9,7,5,1),
(10,7,15,2),
(11,10,6,1),
(12,10,14,2),
(13,12,7,1),
(14,12,9,2),
(15,5,8,3),
(16,5,2,4),
(17,6,7,3),
(18,6,11,4),
(19,11,14,3),
(20,11,5,4);

CREATE TABLE `mantenimientocontratado` (
  `ID_orden` int NOT NULL AUTO_INCREMENT,
  `ID_mantenimiento` int DEFAULT NULL,
  `ID_vehiculo` int DEFAULT NULL,
  PRIMARY KEY (`ID_orden`),
  KEY `fk_ID_mantenimiento_idx` (`ID_mantenimiento`),
  KEY `fk_ID_vehiculo_idx` (`ID_vehiculo`),
  CONSTRAINT `fk_ID_mantenimientoContratado` FOREIGN KEY (`ID_mantenimiento`) REFERENCES `mantenimiento` (`ID_mantenimiento`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_ID_vehiculo` FOREIGN KEY (`ID_vehiculo`) REFERENCES `vehiculo` (`ID_vehiculo`) ON DELETE SET NULL ON UPDATE CASCADE
);

INSERT INTO `mantenimientocontratado` VALUES 
(1,5,10),(2,6,11),(3,5,1),(4,6,13),(5,5,2),(6,6,12),
(7,1,3),(8,2,4),(9,1,5),(10,2,15),(11,1,6),(12,2,14),
(13,1,7),(14,2,9),(15,3,8),(16,4,2),(17,3,7),(18,4,11),
(19,3,14),(20,4,5);


CREATE TABLE `socioasignacion` (
  `ID_socioAsignacion` int NOT NULL AUTO_INCREMENT,
  `ID_socio` int DEFAULT NULL,
  `ID_garageAsignacion` int DEFAULT NULL,
  `ID_vehiculoAsignado` int DEFAULT NULL,
  `fechaAsignada` date NOT NULL,
  PRIMARY KEY (`ID_socioAsignacion`),
  KEY `fk_ID_garageAsignacion_idx` (`ID_garageAsignacion`),
  KEY `fk_ID_vehiculoAsignacion_idx` (`ID_vehiculoAsignado`),
  KEY `fk_ID_socio_idx` (`ID_socio`),
  CONSTRAINT `fk_ID_garageAsignacion` FOREIGN KEY (`ID_garageAsignacion`) REFERENCES `garage` (`ID_garage`)ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_ID_socio` FOREIGN KEY (`ID_socio`) REFERENCES `socio` (`ID_persona`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_ID_vehiculoAsignacion` FOREIGN KEY (`ID_vehiculoAsignado`) REFERENCES `vehiculo` (`ID_vehiculo`) ON DELETE SET NULL ON UPDATE CASCADE
);

INSERT INTO `socioasignacion` 
(`ID_socioAsignacion`, `ID_socio`, `ID_garageAsignacion`, `ID_vehiculoAsignado`, `fechaAsignada`) VALUES
(1,13,16,1,'2024-01-10'),
(2,13,17,2,'2024-01-15'),
(3,13,18,3,'2024-01-20'),
(4,14,19,4,'2024-02-01'),
(5,14,20,5,'2024-02-10'),
(6,15,21,6,'2024-03-05'),
(7,15,22,7,'2024-03-12'),
(8,16,23,8,'2024-04-01'),
(9,16,24,9,'2024-04-08'),
(10,17,25,10,'2024-05-03'),
(11,17,26,11,'2024-05-10'),
(12,18,27,12,'2024-06-02'),
(13,18,28,13,'2024-06-12'),
(14,19,29,14,'2024-07-01'),
(15,13,30,15,'2024-07-15');