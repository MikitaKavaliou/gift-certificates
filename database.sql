CREATE DATABASE IF NOT EXISTS `gift_certificates`;
USE `gift_certificates`;

DROP TABLE IF EXISTS `gift_certificate`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gift_certificate`
(
    `gift_certificate_id` int(11)   NOT NULL AUTO_INCREMENT,
    `name`                varchar(50)    DEFAULT NULL,
    `description`         varchar(100)   DEFAULT NULL,
    `price`               decimal(8, 2)  DEFAULT NULL,
    `create_date`         timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `last_update_date`    timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `duration`            int(5)         DEFAULT NULL,
    PRIMARY KEY (`gift_certificate_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 14
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gift_certificate`
--

LOCK TABLES `gift_certificate` WRITE;
/*!40000 ALTER TABLE `gift_certificate`
    DISABLE KEYS */;
INSERT INTO `gift_certificate`
VALUES (9, 'pizza_certificate', '50% discount for 5 pizzas', 10.50, '2020-01-18 15:53:21', '2020-01-18 15:53:21', 4),
       (10, 'certificate_for_rent_car', 'car-sharing-5$-free', 3.00, '2020-01-18 15:54:26', '2020-01-18 15:57:18', 4),
       (11, 'ninja_sushi_certificate', 'every second sushi is free', 3.00, '2020-01-18 15:58:24', '2020-01-18 15:58:24',
        2),
       (12, 'healty_food_certificate', 'every salad 25% discount', 2.00, '2020-01-18 15:59:14', '2020-01-18 15:59:14',
        1),
       (13, 'kfc_delivery_ceritificate', 'free delivery over 10$ ', 2.50, '2020-01-18 16:00:52', '2020-01-18 16:00:52',
        10);
/*!40000 ALTER TABLE `gift_certificate`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag`
(
    `tag_id` int(11) NOT NULL AUTO_INCREMENT,
    `name`   varchar(50) DEFAULT NULL,
    PRIMARY KEY (`tag_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 17
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag`
    DISABLE KEYS */;
INSERT INTO `tag`
VALUES (6, 'pizza'),
       (7, 'discount'),
       (8, 'certificate'),
       (9, 'car'),
       (10, 'rent'),
       (11, 'for_rent'),
       (12, 'sushi'),
       (13, 'food'),
       (14, 'delivery'),
       (15, 'salad'),
       (16, 'kfc');
/*!40000 ALTER TABLE `tag`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag_gift_certificate`
--

DROP TABLE IF EXISTS `tag_gift_certificate`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag_gift_certificate`
(
    `tag_id`              int(11) NOT NULL,
    `gift_certificate_id` int(11) NOT NULL,
    KEY `fk_tag_gift_certificate_gift_certificate_gift_certificate_i_idx` (`gift_certificate_id`),
    KEY `fk_tag_gift_certificate_gift_tag_tag_id_idx` (`tag_id`),
    CONSTRAINT `fk_tag_gift_certificate_gift_certificate_gift_certificate_id` FOREIGN KEY (`gift_certificate_id`) REFERENCES `gift_certificate` (`gift_certificate_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_tag_gift_certificate_gift_tag_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`tag_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag_gift_certificate`
--

LOCK TABLES `tag_gift_certificate` WRITE;
/*!40000 ALTER TABLE `tag_gift_certificate`
    DISABLE KEYS */;
INSERT INTO `tag_gift_certificate`
VALUES (6, 9),
       (7, 9),
       (8, 9),
       (9, 10),
       (10, 10),
       (11, 10),
       (12, 11),
       (13, 11),
       (14, 11),
       (15, 12),
       (13, 12),
       (14, 12),
       (16, 13),
       (13, 13),
       (14, 13);
/*!40000 ALTER TABLE `tag_gift_certificate`
    ENABLE KEYS */;
UNLOCK TABLES;