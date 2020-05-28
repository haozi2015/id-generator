-- ----------------------------
-- Table structure for t_sequence
-- ----------------------------
DROP TABLE IF EXISTS `t_sequence`;
CREATE TABLE `t_sequence` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `sequence_key` varchar(50) NOT NULL COMMENT '序列KEY',
  `offset` bigint(20) NOT NULL COMMENT '当前值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_sequence_key` (`sequence_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='ID生成表';

-- ----------------------------
-- Table structure for t_sequence_rule
-- ----------------------------
DROP TABLE IF EXISTS `t_sequence_rule`;
CREATE TABLE `t_sequence_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `key` varchar(50) NOT NULL COMMENT '唯一KEY',
  `increment` int(11) NOT NULL DEFAULT '1' COMMENT '自增步长',
  `memory_capacity` int(11) NOT NULL DEFAULT '500' COMMENT '内存存储ID量',
  `reload_threshold` int(11) NOT NULL DEFAULT '50' COMMENT '再次内存加载阈值',
  `prefix` varchar(100) NOT NULL DEFAULT '' COMMENT '前缀',
  `digits` tinyint(4) NOT NULL DEFAULT '0' COMMENT '位数，位数不足补0（小于等于0不补）',
  `reset_rule` varchar(20) NOT NULL DEFAULT '' COMMENT '复位规则',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态(0:暂停,1:启用)',
  `last_update_time` datetime NOT NULL COMMENT '最后更新时间',
  `initial_value` int(11) NOT NULL COMMENT '初始值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_key` (`key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='ID生成规则表';
