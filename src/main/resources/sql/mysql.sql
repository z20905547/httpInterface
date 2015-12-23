/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50619
Source Host           : localhost:3306
Source Database       : base-framework

Target Server Type    : MYSQL
Target Server Version : 50619
File Encoding         : 65001

Date: 2014-10-26 23:49:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_data_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `tb_data_dictionary`;
CREATE TABLE `tb_data_dictionary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) NOT NULL,
  `remark` text,
  `type` varchar(32) NOT NULL,
  `value` varchar(8) NOT NULL,
  `fk_category_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_layhfd1butuigsscgucmp2okd` (`fk_category_id`),
  CONSTRAINT `fk_layhfd1butuigsscgucmp2okd` FOREIGN KEY (`fk_category_id`) REFERENCES `tb_dictionary_category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_data_dictionary
-- ----------------------------
INSERT INTO `tb_data_dictionary` VALUES ('1', '启用', '', 'java.lang.Integer', '1', '1');
INSERT INTO `tb_data_dictionary` VALUES ('2', '禁用', '', 'java.lang.Integer', '2', '1');
INSERT INTO `tb_data_dictionary` VALUES ('3', '删除', '', 'java.lang.Integer', '3', '1');
INSERT INTO `tb_data_dictionary` VALUES ('10', '菜单类型', '', 'java.lang.Integer', '1', '2');
INSERT INTO `tb_data_dictionary` VALUES ('11', '安全类型', '', 'java.lang.Integer', '2', '2');

-- ----------------------------
-- Table structure for tb_dictionary_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_dictionary_category`;
CREATE TABLE `tb_dictionary_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(16) NOT NULL,
  `name` varchar(32) NOT NULL,
  `remark` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_9qkei4dxobl1lm4oa0ys8c3nr` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_dictionary_category
-- ----------------------------
INSERT INTO `tb_dictionary_category` VALUES ('1', 'state', '状态', null);
INSERT INTO `tb_dictionary_category` VALUES ('2', 'resource-type', '资源类型', null);

-- ----------------------------
-- Table structure for tb_group
-- ----------------------------
DROP TABLE IF EXISTS `tb_group`;
CREATE TABLE `tb_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) NOT NULL,
  `remark` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_byw2jrrrxrueqimkmgj3o842j` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_group
-- ----------------------------
INSERT INTO `tb_group` VALUES ('1', '普通用户', '');
INSERT INTO `tb_group` VALUES ('2', '运维人员', '');
INSERT INTO `tb_group` VALUES ('3', '超级管理员', '');

-- ----------------------------
-- Table structure for tb_group_resource
-- ----------------------------
DROP TABLE IF EXISTS `tb_group_resource`;
CREATE TABLE `tb_group_resource` (
  `fk_resource_id` bigint(20) NOT NULL,
  `fk_group_id` bigint(20) NOT NULL,
  PRIMARY KEY (`fk_resource_id`,`fk_group_id`),
  KEY `fk_q82fpmfh128qxoeyymrkg71e2` (`fk_group_id`),
  CONSTRAINT `fk_3tjs4wt3vvoibo1fvcvog5srd` FOREIGN KEY (`fk_resource_id`) REFERENCES `tb_resource` (`id`),
  CONSTRAINT `fk_q82fpmfh128qxoeyymrkg71e2` FOREIGN KEY (`fk_group_id`) REFERENCES `tb_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_group_resource
-- ----------------------------
INSERT INTO `tb_group_resource` VALUES ('1', '1');
INSERT INTO `tb_group_resource` VALUES ('2', '1');
INSERT INTO `tb_group_resource` VALUES ('6', '1');
INSERT INTO `tb_group_resource` VALUES ('7', '1');
INSERT INTO `tb_group_resource` VALUES ('9', '1');
INSERT INTO `tb_group_resource` VALUES ('11', '1');
INSERT INTO `tb_group_resource` VALUES ('13', '1');
INSERT INTO `tb_group_resource` VALUES ('15', '1');
INSERT INTO `tb_group_resource` VALUES ('16', '1');
INSERT INTO `tb_group_resource` VALUES ('18', '1');
INSERT INTO `tb_group_resource` VALUES ('20', '1');
INSERT INTO `tb_group_resource` VALUES ('22', '1');
INSERT INTO `tb_group_resource` VALUES ('15', '2');
INSERT INTO `tb_group_resource` VALUES ('16', '2');
INSERT INTO `tb_group_resource` VALUES ('17', '2');
INSERT INTO `tb_group_resource` VALUES ('18', '2');
INSERT INTO `tb_group_resource` VALUES ('19', '2');
INSERT INTO `tb_group_resource` VALUES ('20', '2');
INSERT INTO `tb_group_resource` VALUES ('21', '2');
INSERT INTO `tb_group_resource` VALUES ('22', '2');
INSERT INTO `tb_group_resource` VALUES ('23', '2');
INSERT INTO `tb_group_resource` VALUES ('1', '3');
INSERT INTO `tb_group_resource` VALUES ('2', '3');
INSERT INTO `tb_group_resource` VALUES ('3', '3');
INSERT INTO `tb_group_resource` VALUES ('4', '3');
INSERT INTO `tb_group_resource` VALUES ('5', '3');
INSERT INTO `tb_group_resource` VALUES ('6', '3');
INSERT INTO `tb_group_resource` VALUES ('7', '3');
INSERT INTO `tb_group_resource` VALUES ('8', '3');
INSERT INTO `tb_group_resource` VALUES ('9', '3');
INSERT INTO `tb_group_resource` VALUES ('10', '3');
INSERT INTO `tb_group_resource` VALUES ('11', '3');
INSERT INTO `tb_group_resource` VALUES ('12', '3');
INSERT INTO `tb_group_resource` VALUES ('13', '3');
INSERT INTO `tb_group_resource` VALUES ('14', '3');
INSERT INTO `tb_group_resource` VALUES ('15', '3');
INSERT INTO `tb_group_resource` VALUES ('16', '3');
INSERT INTO `tb_group_resource` VALUES ('17', '3');
INSERT INTO `tb_group_resource` VALUES ('18', '3');
INSERT INTO `tb_group_resource` VALUES ('19', '3');
INSERT INTO `tb_group_resource` VALUES ('20', '3');
INSERT INTO `tb_group_resource` VALUES ('21', '3');
INSERT INTO `tb_group_resource` VALUES ('22', '3');
INSERT INTO `tb_group_resource` VALUES ('23', '3');

-- ----------------------------
-- Table structure for tb_group_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_group_user`;
CREATE TABLE `tb_group_user` (
  `fk_group_id` bigint(20) NOT NULL,
  `fk_user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`fk_group_id`,`fk_user_id`),
  KEY `fk_7k068ltfepa1q75qtmvxuawk` (`fk_user_id`),
  CONSTRAINT `fk_7k068ltfepa1q75qtmvxuawk` FOREIGN KEY (`fk_user_id`) REFERENCES `tb_user` (`id`),
  CONSTRAINT `fk_rgmkki7dggfag6ow6eivljmwv` FOREIGN KEY (`fk_group_id`) REFERENCES `tb_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_group_user
-- ----------------------------
INSERT INTO `tb_group_user` VALUES ('1', '3');
INSERT INTO `tb_group_user` VALUES ('1', '4');
INSERT INTO `tb_group_user` VALUES ('3', '5');

-- ----------------------------
-- Table structure for tb_resource
-- ----------------------------
DROP TABLE IF EXISTS `tb_resource`;
CREATE TABLE `tb_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permission` varchar(64) DEFAULT NULL,
  `remark` text,
  `sort` int(11) NOT NULL,
  `name` varchar(16) NOT NULL,
  `type` int(2) NOT NULL,
  `value` varchar(256) DEFAULT NULL,
  `fk_parent_id` bigint(20) DEFAULT NULL,
  `icon` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_aunvlvm32xb4e6590jc9oooq` (`name`),
  KEY `fk_k2heqvi9muk4cjyyd53r9y37x` (`fk_parent_id`),
  CONSTRAINT `fk_k2heqvi9muk4cjyyd53r9y37x` FOREIGN KEY (`fk_parent_id`) REFERENCES `tb_resource` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_resource
-- ----------------------------
INSERT INTO `tb_resource` VALUES ('1', '', '', '1', '权限管理', '1', '', null, 'glyphicon glyphicon-briefcase');
INSERT INTO `tb_resource` VALUES ('2', 'perms[user:list]', '', '2', '用户管理', '1', '/account/user/list/**', '1', 'glyphicon glyphicon-user');
INSERT INTO `tb_resource` VALUES ('3', 'perms[user:insert],authc', '', '3', '创建用户', '2', '/account/user/insert/**', '2', '');
INSERT INTO `tb_resource` VALUES ('4', 'perms[user:update],authc', '', '4', '修改用户', '2', '/account/user/update/**', '2', '');
INSERT INTO `tb_resource` VALUES ('5', 'perms[user:delete],authc', '', '5', '删除用户', '2', '/account/user/delete/**', '2', '');
INSERT INTO `tb_resource` VALUES ('6', 'perms[user:edit],authc', '', '6', '查看用户', '2', '/account/user/edit/**', '2', '');
INSERT INTO `tb_resource` VALUES ('7', 'perms[group:list]', '', '7', '组管理', '1', '/account/group/list/**', '1', 'glyphicon glyphicon-briefcase');
INSERT INTO `tb_resource` VALUES ('8', 'perms[group:save],authc', '', '8', '创建和编辑组', '2', '/account/group/save/**', '7', '');
INSERT INTO `tb_resource` VALUES ('9', 'perms[group:edit],authc', '', '9', '查看组', '2', '/account/group/edit/**', '7', '');
INSERT INTO `tb_resource` VALUES ('10', 'perms[group:delete],authc', '', '10', '删除组', '2', '/account/group/delete/**', '7', '');
INSERT INTO `tb_resource` VALUES ('11', 'perms[resource:list]', '', '11', '资源管理', '1', '/account/resource/list/**', '1', 'glyphicon glyphicon-link');
INSERT INTO `tb_resource` VALUES ('12', 'perms[resource:save],authc', '', '12', '创建和编辑资源', '2', '/account/resource/save/**', '11', '');
INSERT INTO `tb_resource` VALUES ('13', 'perms[resource:edit],authc', '', '13', '查看资源', '2', '/account/resource/edit/**', '11', '');
INSERT INTO `tb_resource` VALUES ('14', 'perms[resource:delete],authc', '', '14', '删除资源', '2', '/account/resource/delete/**', '11', '');
INSERT INTO `tb_resource` VALUES ('15', '', '', '15', '系统管理', '1', '', null, 'glyphicon glyphicon-cog');
INSERT INTO `tb_resource` VALUES ('16', 'perms[data-dictionary:list]', '', '16', '数据字典管理', '1', '/variable/data-dictionary/list/**', '15', 'glyphicon glyphicon-list-alt');
INSERT INTO `tb_resource` VALUES ('17', 'perms[data-dictionary:save],authc', '', '17', '创建和编辑数据字典', '2', '/variable/data-dictionary/save/**', '16', '');
INSERT INTO `tb_resource` VALUES ('18', 'perms[data-dictionary:edit],authc', '', '18', '查看数据字典', '2', '/variable/data-dictionary/edit/**', '16', '');
INSERT INTO `tb_resource` VALUES ('19', 'perms[data-dictionary:delete],authc', '', '19', '删除数据字典', '2', '/variable/data-dictionary/delete/**', '16', '');
INSERT INTO `tb_resource` VALUES ('20', 'perms[dictionary-category:list]', '', '20', '字典类别管理', '1', '/variable/dictionary-category/list/**', '15', 'glyphicon glyphicon-folder-close');
INSERT INTO `tb_resource` VALUES ('21', 'perms[dictionary-category:save],authc', '', '21', '创建和编辑字典类别', '2', '/variable/dictionary-category/save/**', '20', '');
INSERT INTO `tb_resource` VALUES ('22', 'perms[dictionary-category:edit],authc', '', '22', '查看字典类别', '2', '/variable/dictionary-category/edit/**', '20', '');
INSERT INTO `tb_resource` VALUES ('23', 'perms[dictionary-category:delete],authc', '', '20', '删除字典类别', '2', '/variable/dictionary-category/delete/**', '20', '');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(64) DEFAULT NULL,
  `password` varchar(32) NOT NULL,
  `nickname` varchar(16) NOT NULL,
  `state` int(2) NOT NULL,
  `username` varchar(16) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_4wv83hfajry5tdoamn8wsqa6x` (`username`,`email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('3', 'maintain@baseframework.com', 'e10adc3949ba59abbe56e057f20f883e', '运维用户', '1', 'maintain');
INSERT INTO `tb_user` VALUES ('4', 'user@baseframework.com', 'e10adc3949ba59abbe56e057f20f883e', '普通用户', '1', 'user');
INSERT INTO `tb_user` VALUES ('5', 'administrator@baseframework.com', 'e10adc3949ba59abbe56e057f20f883e', '超级管理员', '1', 'admin');
