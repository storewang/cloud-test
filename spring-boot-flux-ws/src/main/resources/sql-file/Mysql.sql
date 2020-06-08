CREATE TABLE `gtway_ws_message` (
  `id` VARCHAR(64) NOT NULL COMMENT '主键id',
  `sender` VARCHAR(100) DEFAULT NULL COMMENT '消息发送者',
  `receiver` VARCHAR(100) DEFAULT NULL COMMENT '消息接收者',
  `msg` VARCHAR(1000) DEFAULT NULL COMMENT '消息内容',
  `status` TINYINT(1) DEFAULT '0' COMMENT '状态(0-未发送,1-已发送,未读,2-已读)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_to` (`receiver`) USING BTREE,
  KEY `index_from` (`sender`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='消息发送记录表';