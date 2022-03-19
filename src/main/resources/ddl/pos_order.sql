CREATE TABLE `pos_order` (
  `orderId` varchar(18) COLLATE utf8mb4_unicode_ci NOT NULL,
  `orderNo` varchar(18) COLLATE utf8mb4_unicode_ci NOT NULL,
  `customerName` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `operatorName` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `originPrice` double(10,2) DEFAULT NULL,
  `changePrice` double(10,2) DEFAULT NULL,
  `totalNum` int DEFAULT NULL,
  `getNum` int DEFAULT NULL,
  `returnNum` int DEFAULT NULL,
  `getPrice` double(10,2) DEFAULT NULL,
  `returnPrice` double(10,2) DEFAULT NULL,
  `ctime` varchar(25) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ymd` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`orderId`,`orderNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;