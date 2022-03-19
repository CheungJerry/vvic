CREATE TABLE `cron` (
  `cron_id` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cron` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`cron_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `vvic`.`cron` (`cron_id`, `cron`) VALUES ('vvic', '0 1 0 * * ?');