CREATE TABLE `data_voucher` (
  `id` bigint(20) NOT NULL,
  `create_time` datetime(6) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `voucher` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userididx` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `name` varchar(64) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `phone` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
