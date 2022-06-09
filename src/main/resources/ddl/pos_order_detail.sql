CREATE TABLE `pos_order_detail` (
  `orderDetailId` bigint NOT NULL,
  `orderNo` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `account` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `artNo` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `size` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `indexImgUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `skuNum` int DEFAULT NULL,
  `changePrice` double(10,2) DEFAULT NULL,
  `skuLevelPrice` double(10,2) DEFAULT NULL,
  `actualPrice` double(10,2) DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `ymd` date DEFAULT NULL,
  `unitPrice` double(10,2) DEFAULT NULL,
  `type` tinyint DEFAULT NULL,
  `orderType` tinyint DEFAULT NULL,
  `skuCancelNum` int DEFAULT NULL,
  `showNum` int DEFAULT NULL,
  UNIQUE KEY `idx_no_detailid` (`orderDetailId`,`orderNo`,`account`) USING BTREE,
  KEY `account` (`account`),
  KEY `ymd` (`ymd`),
  KEY `idx_detail` (`orderDetailId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;