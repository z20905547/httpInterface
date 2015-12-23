-- 删除所有表
DROP TABLE tb_data_dictionary if EXISTS;
DROP TABLE tb_dictionary_category if EXISTS;
DROP TABLE tb_group if EXISTS;
DROP TABLE tb_group_resource if EXISTS;
DROP TABLE tb_group_user if EXISTS;
DROP TABLE tb_resource if EXISTS;
DROP TABLE tb_user if EXISTS;

-- 创建系统字典表
CREATE TABLE tb_data_dictionary (id BIGINT NOT NULL IDENTITY PRIMARY KEY,name VARCHAR(16) NOT NULL,remark VARCHAR(512),type VARCHAR(32) NOT NULL,value VARCHAR(8) NOT NULL,fk_category_id BIGINT NOT NULL);
CREATE TABLE tb_dictionary_category (id BIGINT NOT NULL IDENTITY PRIMARY KEY,code VARCHAR(16) NOT NULL,name VARCHAR(32) NOT NULL,remark VARCHAR(512));

-- 创建权限表
CREATE TABLE tb_group (id BIGINT NOT NULL IDENTITY PRIMARY KEY,name VARCHAR(16) NOT NULL,remark VARCHAR(512),);
CREATE TABLE tb_group_resource (fk_resource_id BIGINT,fk_group_id BIGINT);
CREATE TABLE tb_group_user (fk_group_id BIGINT,fk_user_id BIGINT);
CREATE TABLE tb_resource (id BIGINT NOT NULL IDENTITY PRIMARY KEY,permission VARCHAR(64), remark VARCHAR(512),sort INTEGER(11) NOT NULL,name VARCHAR(16) NOT NULL,type INTEGER(2) NOT NULL,value VARCHAR(256),fk_parent_id BIGINT,icon VARCHAR(32));
CREATE TABLE tb_user (id BIGINT NOT NULL IDENTITY PRIMARY KEY,email VARCHAR(64),password char(32) NOT NULL,nickname VARCHAR(16) NOT NULL,state INTEGER(2) NOT NULL,username VARCHAR(16) NOT NULL);

--创建所有表关联
ALTER TABLE tb_dictionary_category ADD CONSTRAINT uk_9qkei4dxobl1lm4oa0ys8c3nr UNIQUE (code);
ALTER TABLE tb_data_dictionary ADD CONSTRAINT fk_layhfd1butuigsscgucmp2okd FOREIGN KEY (fk_category_id) REFERENCES tb_dictionary_category;

ALTER TABLE tb_group ADD CONSTRAINT uk_byw2jrrrxrueqimkmgj3o842j UNIQUE (name);

ALTER TABLE tb_resource ADD CONSTRAINT uk_aunvlvm32xb4e6590jc9oooq UNIQUE (name);
ALTER TABLE tb_resource ADD CONSTRAINT fk_k2heqvi9muk4cjyyd53r9y37x FOREIGN KEY (fk_parent_id) REFERENCES tb_resource;

ALTER TABLE tb_user ADD CONSTRAINT uk_4wv83hfajry5tdoamn8wsqa6x UNIQUE (username);
ALTER TABLE tb_user ADD CONSTRAINT uk_dkk3p2idks89402kdlzidp4i2 UNIQUE (email);

ALTER TABLE tb_group_resource ADD CONSTRAINT fk_q82fpmfh128qxoeyymrkg71e2 FOREIGN KEY (fk_group_id) REFERENCES tb_group;
ALTER TABLE tb_group_resource ADD CONSTRAINT fk_3tjs4wt3vvoibo1fvcvog5srd FOREIGN KEY (fk_resource_id) REFERENCES tb_resource;

ALTER TABLE tb_group_user ADD CONSTRAINT fk_7k068ltfepa1q75qtmvxuawk FOREIGN KEY (fk_user_id) REFERENCES tb_user;
ALTER TABLE tb_group_user ADD CONSTRAINT fk_rgmkki7dggfag6ow6eivljmwv FOREIGN KEY (fk_group_id) REFERENCES tb_group;


-- 插入表数据
INSERT INTO tb_dictionary_category(id, code, name, remark) VALUES (1, 'state', '状态', '');
INSERT INTO tb_dictionary_category(id, code, name, remark) VALUES (2, 'resource-type', '资源类型', '');

INSERT INTO tb_data_dictionary(id, name, remark, type, value, fk_category_id) VALUES (1, '启用', '', 'java.lang.Integer', '1', 1);
INSERT INTO tb_data_dictionary(id, name, remark, type, value, fk_category_id) VALUES (2, '禁用', '', 'java.lang.Integer', '2', 1);
INSERT INTO tb_data_dictionary(id, name, remark, type, value, fk_category_id) VALUES (3, '删除', '', 'java.lang.Integer', '3', 1);
INSERT INTO tb_data_dictionary(id, name, remark, type, value, fk_category_id) VALUES (10, '菜单类型', '', 'java.lang.Integer', '1', 2);
INSERT INTO tb_data_dictionary(id, name, remark, type, value, fk_category_id) VALUES (11, '资源类型', '', 'java.lang.Integer', '2', 2);

INSERT INTO tb_group(id, name, remark) VALUES (1, '普通用户', '');
INSERT INTO tb_group(id, name, remark) VALUES (2, '运维人员', '');
INSERT INTO tb_group(id, name, remark) VALUES (3, '超级管理员', '');

INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (1, '', '', 1, '权限管理', 1, '', null, 'glyphicon glyphicon-briefcase');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (2, 'perms[user:list]', '', 2, '用户管理', 1, '/account/user/list/**', 1, 'glyphicon glyphicon-user');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (3, 'perms[user:insert],authc', '', 3, '创建用户', 2, '/account/user/insert/**', 2, '');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (4, 'perms[user:update],authc', '', 4, '修改用户', 2, '/account/user/update/**', 2, '');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (5, 'perms[user:delete],authc', '', 5, '删除用户', 2, '/account/user/delete/**', 2, '');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (6, 'perms[user:edit],authc', '', 6, '查看用户', 2, '/account/user/edit/**', 2, '');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (7, 'perms[group:list]', '', 7, '组管理', 1, '/account/group/list/**', 1, 'glyphicon glyphicon-briefcase');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (8, 'perms[group:save],authc', '', 8, '创建和编辑组', 2, '/account/group/save/**', 7, '');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (9, 'perms[group:edit],authc', '', 9, '查看组', 2, '/account/group/edit/**', 7, '');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (10, 'perms[group:delete],authc', '', 10, '删除组', 2, '/account/group/delete/**', 7, '');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (11, 'perms[resource:list]', '', 11, '资源管理', 1, '/account/resource/list/**', 1, 'glyphicon glyphicon-link');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (12, 'perms[resource:save],authc', '', 12, '创建和编辑资源', 2, '/account/resource/save/**', 11, '');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (13, 'perms[resource:edit],authc', '', 13, '查看资源', 2, '/account/resource/edit/**', 11, '');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (14, 'perms[resource:delete],authc', '', 14, '删除资源', 2, '/account/resource/delete/**', 11, '');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (15, '', '', 15, '系统管理', 1, '', null, 'glyphicon glyphicon-cog');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (16, 'perms[data-dictionary:list]', '', 16, '数据字典管理', 1, '/variable/data-dictionary/list/**', 15, 'glyphicon glyphicon-list-alt');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (17, 'perms[data-dictionary:save],authc', '', 17, '创建和编辑数据字典', 2, '/variable/data-dictionary/save/**', 16, '');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (18, 'perms[data-dictionary:edit],authc', '', 18, '查看数据字典', 2, '/variable/data-dictionary/edit/**', 16, '');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (19, 'perms[data-dictionary:delete],authc', '', 19, '删除数据字典', 2, '/variable/data-dictionary/delete/**', 16, '');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (20, 'perms[dictionary-category:list]', '', 20, '字典类别管理', 1, '/variable/dictionary-category/list/**', 15, 'glyphicon glyphicon-folder-close');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (21, 'perms[dictionary-category:save],authc', '', 21, '创建和编辑字典类别', 2, '/variable/dictionary-category/save/**', 20, '');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (22, 'perms[dictionary-category:edit],authc', '', 22, '查看字典类别', 2, '/variable/dictionary-category/edit/**', 20, '');
INSERT INTO tb_resource(id, permission, remark, sort, name, type, value, fk_parent_id, icon) VALUES (23, 'perms[dictionary-category:delete],authc', '', 23, '删除字典类别', 2, '/variable/dictionary-category/delete/**', 20, '');

INSERT INTO tb_user (id, email, password, nickname, state, username) VALUES (3, 'maintain@baseframework.com', 'e10adc3949ba59abbe56e057f20f883e', '运维用户', 1, 'maintain');
INSERT INTO tb_user (id, email, password, nickname, state, username) VALUES (4, 'user@baseframework.com', 'e10adc3949ba59abbe56e057f20f883e', '普通用户', 1, 'user');
INSERT INTO tb_user (id, email, password, nickname, state, username) VALUES (5, 'administrator@baseframework.com', 'e10adc3949ba59abbe56e057f20f883e', '超级管理员', 1, 'admin');

INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (1, 1);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (2, 1);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (6, 1);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (7, 1);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (9, 1);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (11, 1);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (13, 1);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (15, 1);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (16, 1);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (18, 1);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (20, 1);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (22, 1);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (15, 2);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (16, 2);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (17, 2);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (18, 2);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (19, 2);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (20, 2);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (21, 2);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (22, 2);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (23, 2);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (1, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (2, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (3, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (4, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (5, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (6, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (7, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (8, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (9, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (10, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (11, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (12, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (13, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (14, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (15, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (16, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (17, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (18, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (19, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (20, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (21, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (22, 3);
INSERT INTO tb_group_resource (fk_resource_id, fk_group_id) VALUES (23, 3);

INSERT INTO tb_group_user (fk_group_id, fk_user_id) VALUES (2, 3);
INSERT INTO tb_group_user (fk_group_id, fk_user_id) VALUES (1, 4);
INSERT INTO tb_group_user (fk_group_id, fk_user_id) VALUES (3, 5);