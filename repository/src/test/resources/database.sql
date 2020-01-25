DROP TABLE IF EXISTS `tag_gift_certificate`;
DROP TABLE IF EXISTS `gift_certificate`;
DROP TABLE IF EXISTS `tag`;
CREATE TABLE IF NOT EXISTS `gift_certificate`
(
    `gift_certificate_id` int(11)   NOT NULL AUTO_INCREMENT,
    `name`                varchar(50)    DEFAULT NULL,
    `description`         varchar(100)   DEFAULT NULL,
    `price`               decimal(8, 2)  DEFAULT NULL,
    `create_date`         timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `last_update_date`    timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `duration`            int(5)         DEFAULT NULL,
    PRIMARY KEY (`gift_certificate_id`)
) AUTO_INCREMENT = 14;

INSERT INTO `gift_certificate` (`gift_certificate_id`, `name`, `description`, `price`, `create_date`,
                                `last_update_date`, `duration`)
VALUES (9, 'pizza_certificate', '50% discount for 5 pizzas', 10.50, '2020-01-18 18:53:21', '2020-01-18 18:53:21', 4),
       (10, 'certificate_for_rent_car', 'car-sharing-5$-free', 3.00, '2020-01-18 18:54:26', '2020-01-18 18:57:18', 4),
       (11, 'ninja_sushi_certificate', 'every second sushi is free', 3.00, '2020-01-18 18:58:24', '2020-01-18 18:58:24',
        2),
       (12, 'healty_food_certificate', 'every salad 25% discount', 2.00, '2020-01-18 18:59:14', '2020-01-18 18:59:14',
        1),
       (13, 'kfc_delivery_ceritificate', 'free delivery over 10$', 2.50, '2020-01-18 19:00:52', '2020-01-18 19:00:52',
        10);


CREATE TABLE IF NOT EXISTS `tag`
(
    `tag_id` int(11) NOT NULL AUTO_INCREMENT,
    `name`   varchar(50) DEFAULT NULL,
    PRIMARY KEY (`tag_id`)
) AUTO_INCREMENT = 17;

/*!40000 ALTER TABLE `tag`
    DISABLE KEYS */;
INSERT INTO `tag` (`tag_id`, `name`)
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


CREATE TABLE IF NOT EXISTS `tag_gift_certificate`
(
    `tag_id`              int(11) NOT NULL,
    `gift_certificate_id` int(11) NOT NULL,
    foreign key (tag_id) references tag (tag_id) ON DELETE CASCADE,
    foreign key (gift_certificate_id) references gift_certificate (gift_certificate_id) ON DELETE CASCADE
);

INSERT INTO `tag_gift_certificate` (`tag_id`, `gift_certificate_id`)
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


