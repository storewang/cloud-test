CREATE TABLE `gtway_route_bind` (
  `id` varchar(64) NOT NULL COMMENT '主键id',
  `route_id` varchar(64) DEFAULT NULL COMMENT '路由ID',
  `route_name` varchar(100) DEFAULT NULL COMMENT '路由名称',
  `route_url` varchar(255) DEFAULT NULL COMMENT '路由URL',
  `order` int(11) DEFAULT '0' COMMENT '优先级顺序',
  `data_id` varchar(64) NOT NULL COMMENT '关联数据类型相对应的数据ID',
  `data_type` tinyint(4) DEFAULT '1' COMMENT '关联的数据类型 1: 路由匹配策略 2: 路由过滤策略',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态(0-无效,1-正常,2-冻结)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_data_id` (`data_id`) USING BTREE,
  KEY `index_route_id` (`route_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='路由绑定表';

CREATE TABLE `gtway_route_filter` (
  `id` varchar(64) NOT NULL COMMENT '主键id',
  `filter_name` varchar(100) DEFAULT NULL COMMENT '过滤器名称',
  `route_filter` varchar(255) DEFAULT NULL COMMENT '路由过滤器参数值',
  `order` int(11) DEFAULT '0' COMMENT '优先级顺序',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态(0-无效,1-正常,2-冻结)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_data_name` (`filter_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='路由过滤器表';

CREATE TABLE `gtway_route_predicate` (
  `id` varchar(64) NOT NULL COMMENT '主键id',
  `predicate_name` varchar(100) DEFAULT NULL COMMENT '策略名称',
  `route_predicate` varchar(255) DEFAULT NULL COMMENT '路由策略参数值',
  `order` int(11) DEFAULT '0' COMMENT '优先级顺序',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态(0-无效,1-正常,2-冻结)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_data_name` (`predicate_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='路由策略表';


