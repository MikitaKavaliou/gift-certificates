DROP DATABASE IF exists `gift_certificates_test`;
CREATE DATABASE `gift_certificates_test`;
USE `gift_certificates_test`;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;


DROP TABLE IF EXISTS `gift_certificate`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gift_certificate`
(
    `gift_certificate_id` int(11)   NOT NULL AUTO_INCREMENT,
    `name`                varchar(50)    DEFAULT NULL,
    `description`         varchar(200)   DEFAULT NULL,
    `price`               decimal(8, 2)  DEFAULT NULL,
    `create_date`         timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `last_update_date`    timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `duration`            int(5)         DEFAULT NULL,
    PRIMARY KEY (`gift_certificate_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 79
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `gift_certificate` WRITE;
/*!40000 ALTER TABLE `gift_certificate`
    DISABLE KEYS */;
INSERT INTO `gift_certificate`
VALUES (9, 'pizza_certificate', '50% discount for 5 pizzas', 10.50, '2020-01-18 15:53:21', '2020-01-18 15:53:21', 4),
       (11, 'ninja_sushi_certificate', 'every second sushi is free', 3.00, '2020-01-18 15:58:24', '2020-01-18 15:58:24',
        2),
       (12, 'salad', 'every salad 25% discount', 2.00, '2020-01-18 15:59:14', '2020-01-19 18:49:53', 1),
       (13, 'kfc_delivery_ceritificate', 'free delivery over 10$ ', 2.50, '2020-01-18 16:00:52', '2020-01-18 16:00:52',
        10),
       (47, 'certificate_rent_car', 'car', 18.50, '2020-01-20 21:36:26', '2020-02-16 22:23:46', 10),
       (77, 'kfc_delivery_ceritificate', 'free delivery over 10$ ', 2.50, '2020-02-16 12:12:00', '2020-02-16 12:12:00',
        10),
       (78, 'certificate_for_rent_food', 'free delivery over 10$ ', 10.50, '2020-02-16 12:46:10', '2020-02-16 13:12:42',
        10);
/*!40000 ALTER TABLE `gift_certificate`
    ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user`
(
    `user_id`  int(11)               NOT NULL AUTO_INCREMENT,
    `username` varchar(20)           NOT NULL,
    `password` varchar(100)          NOT NULL,
    `role`     enum ('USER','ADMIN') NOT NULL,
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user`
    DISABLE KEYS */;
INSERT INTO `user`
VALUES (1, 'username', '$2a$10$uTTs/3vynz3s2hmPAj3dsOcumoY2SVTUqWkmK9Wae6OA6V0xOskpq', 'USER'),
       (8, 'username2', '$2a$10$YVCX6JTy7osdVOxH/n0mneb0NFZjcVs/g711uoMwW.JjNPGGEBTpa', 'ADMIN'),
       (9, 'username3', '$2a$10$trsVPM9wZEiCKEpcFgpQGuPBCEr2IBfVvqwxe7jrpA3qscAMkEKH6', 'USER'),
       (10, 'username35', '$2a$10$pOTgvgHzg3zIjm5qF7hof.Kvm.zcFL9m60sYNSff2I9.JLDR2.GYu', 'USER');
/*!40000 ALTER TABLE `user`
    ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `purchase`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase`
(
    `purchase_id`         int(11)       NOT NULL AUTO_INCREMENT,
    `user_id`             int(11)       NOT NULL,
    `gift_certificate_id` int(11)            DEFAULT NULL,
    `cost`                decimal(8, 2) NOT NULL,
    `purchase_date`       timestamp     NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`purchase_id`),
    KEY `fk_purchase_gift_certificate_gift_certificate_id_idx` (`gift_certificate_id`),
    KEY `fk_purchase_users_user_id_idx` (`user_id`),
    CONSTRAINT `fk_purchase_gift_certificate_gift_certificate_id` FOREIGN KEY (`gift_certificate_id`) REFERENCES `gift_certificate` (`gift_certificate_id`) ON DELETE SET NULL,
    CONSTRAINT `fk_purchase_users_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `purchase` WRITE;
/*!40000 ALTER TABLE `purchase`
    DISABLE KEYS */;
INSERT INTO `purchase`
VALUES (1, 8, 47, 10.50, '2020-02-16 09:38:32'),
       (2, 8, 47, 10.50, '2020-02-16 09:38:32'),
       (3, 1, 13, 4.00, '2020-02-16 09:38:32'),
       (4, 8, 12, 2.00, '2020-02-16 09:38:32'),
       (5, 9, 12, 2.00, '2020-02-16 09:38:32');
/*!40000 ALTER TABLE `purchase`
    ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag`
(
    `tag_id` int(11)     NOT NULL AUTO_INCREMENT,
    `name`   varchar(50) NOT NULL,
    PRIMARY KEY (`tag_id`),
    UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 111
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag`
    DISABLE KEYS */;
INSERT INTO `tag`
VALUES (9, 'car'),
       (8, 'certificate'),
       (14, 'delivery'),
       (109, 'delviery'),
       (13, 'food'),
       (110, 'for_rent'),
       (16, 'kfc'),
       (6, 'pizza'),
       (53, 'rent'),
       (15, 'salad'),
       (12, 'sushi'),
       (21, 'tasty');
/*!40000 ALTER TABLE `tag`
    ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `tag_gift_certificate`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag_gift_certificate`
(
    `tag_id`              int(11) NOT NULL,
    `gift_certificate_id` int(11) NOT NULL,
    PRIMARY KEY (`tag_id`, `gift_certificate_id`),
    KEY `fk_tag_gift_certificate_gift_certificate_gift_certificate_i_idx` (`gift_certificate_id`),
    KEY `fk_tag_gift_certificate_gift_tag_tag_id_idx` (`tag_id`),
    CONSTRAINT `fk_tag_gift_certificate_gift_certificate_gift_certificate_id` FOREIGN KEY (`gift_certificate_id`) REFERENCES `gift_certificate` (`gift_certificate_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_tag_gift_certificate_gift_tag_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`tag_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `tag_gift_certificate` WRITE;
/*!40000 ALTER TABLE `tag_gift_certificate`
    DISABLE KEYS */;
INSERT INTO `tag_gift_certificate`
VALUES (6, 9),
       (8, 9),
       (12, 11),
       (13, 11),
       (14, 11),
       (13, 12),
       (14, 12),
       (15, 12),
       (21, 12),
       (13, 13),
       (14, 13),
       (16, 13),
       (8, 47),
       (9, 47),
       (13, 77),
       (14, 77),
       (16, 77),
       (13, 78),
       (16, 78);
/*!40000 ALTER TABLE `tag_gift_certificate`
    ENABLE KEYS */;
UNLOCK TABLES;