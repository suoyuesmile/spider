/*
Navicat MySQL Data Transfer

Source Server         : suo
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : webspider

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2016-12-28 18:22:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for keyword
-- ----------------------------
DROP TABLE IF EXISTS `keyword`;
CREATE TABLE `keyword` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `keyword` varchar(255) DEFAULT NULL,
  `kRank` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of keyword
-- ----------------------------
INSERT INTO `keyword` VALUES ('1', '中国', '15');
INSERT INTO `keyword` VALUES ('4', '第7城市', '1');
INSERT INTO `keyword` VALUES ('5', '百度', '1');
INSERT INTO `keyword` VALUES ('10', '海南大学', '1');
INSERT INTO `keyword` VALUES ('11', '武汉纺织大学', '1');
INSERT INTO `keyword` VALUES ('12', '科技处', '1');

-- ----------------------------
-- Table structure for managelog
-- ----------------------------
DROP TABLE IF EXISTS `managelog`;
CREATE TABLE `managelog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `opera` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  `about` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of managelog
-- ----------------------------
INSERT INTO `managelog` VALUES ('1', '查找', '[范围1到5的记录]', '2016-12-23 12:50:33', null);
INSERT INTO `managelog` VALUES ('2', '修改', '[增加www.china.com网站的排名23]', '2016-12-23 13:10:57', null);
INSERT INTO `managelog` VALUES ('3', '查找', '[范围1到5的记录]', '2016-12-23 13:13:01', null);
INSERT INTO `managelog` VALUES ('4', '修改', '[增加www.baidu网站的排名1]', '2016-12-23 14:01:13', null);
INSERT INTO `managelog` VALUES ('5', '修改', '[增加www.china.com网站的排名2]', '2016-12-23 14:02:29', null);
INSERT INTO `managelog` VALUES ('6', '修改', '[增加www.china.com网站的排名20]', '2016-12-23 14:12:52', null);
INSERT INTO `managelog` VALUES ('7', '查找', '[范围1到50的记录]', '2016-12-23 14:13:57', null);
INSERT INTO `managelog` VALUES ('8', '查找', '[范围1到5的记录]', '2016-12-28 15:42:59', null);
INSERT INTO `managelog` VALUES ('9', '修改', '[增加www.baidu.com网站的排名23]', '2016-12-28 15:43:35', null);
INSERT INTO `managelog` VALUES ('10', '删除', '[范围4到5的记录]', '2016-12-28 15:48:01', null);
INSERT INTO `managelog` VALUES ('11', '删除', '[范围1到1的记录]', '2016-12-28 15:48:25', null);
INSERT INTO `managelog` VALUES ('12', '查找', '[范围1到5的记录]', '2016-12-28 15:48:55', null);
INSERT INTO `managelog` VALUES ('13', '查找', '[范围1到5的记录]', '2016-12-28 15:49:07', null);
INSERT INTO `managelog` VALUES ('14', '删除', '[范围1到3的记录]', '2016-12-28 15:49:22', null);

-- ----------------------------
-- Table structure for webkey
-- ----------------------------
DROP TABLE IF EXISTS `webkey`;
CREATE TABLE `webkey` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `webId` int(11) unsigned DEFAULT NULL,
  `keyId` int(11) unsigned DEFAULT NULL,
  `wkRank` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `key` (`keyId`),
  KEY `web` (`webId`),
  CONSTRAINT `key` FOREIGN KEY (`keyId`) REFERENCES `keyword` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `web` FOREIGN KEY (`webId`) REFERENCES `website` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of webkey
-- ----------------------------
INSERT INTO `webkey` VALUES ('1', '1', '1', '3');
INSERT INTO `webkey` VALUES ('3', '4', '4', '1');
INSERT INTO `webkey` VALUES ('4', '5', '5', '1');
INSERT INTO `webkey` VALUES ('6', '10', '10', '1');
INSERT INTO `webkey` VALUES ('7', '11', '11', '1');
INSERT INTO `webkey` VALUES ('8', '12', '12', '1');

-- ----------------------------
-- Table structure for website
-- ----------------------------
DROP TABLE IF EXISTS `website`;
CREATE TABLE `website` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `url` varchar(255) DEFAULT NULL,
  `wRank` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of website
-- ----------------------------
INSERT INTO `website` VALUES ('1', 'www.china.com', '21');
INSERT INTO `website` VALUES ('4', 'http://www.th7.cn/Program/java/201510/661827.shtml', '1');
INSERT INTO `website` VALUES ('5', 'www.baidu.com', '24');
INSERT INTO `website` VALUES ('10', 'http://www.hainu.edu.cn/', '1');
INSERT INTO `website` VALUES ('11', 'http://www.wtu.edu.cn/html/wwwindex.html', '1');
INSERT INTO `website` VALUES ('12', 'http://kyc.wtu.edu.cn', '1');

-- ----------------------------
-- View structure for 三表连接查询
-- ----------------------------
DROP VIEW IF EXISTS `三表连接查询`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `三表连接查询` AS select `webkey`.`id` AS `id`,`keyword`.`keyword` AS `keyword`,`website`.`url` AS `url`,`webkey`.`wkRank` AS `wkRank` from ((`keyword` join `webkey`) join `website`) where ((`webkey`.`keyId` = `keyword`.`id`) and (`webkey`.`webId` = `website`.`id`)) ;

-- ----------------------------
-- Procedure structure for addRank
-- ----------------------------
DROP PROCEDURE IF EXISTS `addRank`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `addRank`(INOUT `Rank` int)
BEGIN
	#Routine body goes here...

END
;;
DELIMITER ;
