
-- 导出  表 boot.data_source 结构
CREATE TABLE IF NOT EXISTS `data_source` (
  `name` varchar(100) NOT NULL,
  `url` varchar(400) NOT NULL,
  `type` varchar(30) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `password` varchar(50) DEFAULT NULL,
  `note` varchar(500) DEFAULT NULL,
  `enabled` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 正在导出表  boot.data_source 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `data_source` DISABLE KEYS */;
/*!40000 ALTER TABLE `data_source` ENABLE KEYS */;

-- 导出  表 boot.db_build_column 结构
CREATE TABLE IF NOT EXISTS `db_build_column` (
  `id` varchar(32) NOT NULL,
  `connection_name` varchar(100) NOT NULL,
  `table_name` varchar(100) NOT NULL,
  `column_name` varchar(100) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `enum_code` varchar(50) DEFAULT NULL COMMENT '常量CODE',
  `multi_select` tinyint(4) DEFAULT NULL COMMENT '是否多选',
  `is_attachment` tinyint(4) DEFAULT NULL COMMENT '是否附件',
  `attachment_count` int(11) DEFAULT NULL COMMENT '附件数量',
  `required` tinyint(4) DEFAULT NULL COMMENT '是否必填',
  `tableable` tinyint(4) DEFAULT NULL COMMENT '是否列表显示',
  `editable` tinyint(4) DEFAULT NULL COMMENT '是否可编辑',
  `addable` tinyint(4) DEFAULT NULL COMMENT '是否新增',
  `large_text` tinyint(4) DEFAULT NULL COMMENT '是否大文本',
  `max_length` int(11) DEFAULT NULL COMMENT '最大长度',
  `regular_expression` varchar(200) DEFAULT NULL COMMENT '正则验证表达式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 正在导出表  boot.db_build_column 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `db_build_column` DISABLE KEYS */;
/*!40000 ALTER TABLE `db_build_column` ENABLE KEYS */;

-- 导出  表 boot.db_build_table 结构
CREATE TABLE IF NOT EXISTS `db_build_table` (
  `id` varchar(32) NOT NULL,
  `connection_name` varchar(100) NOT NULL,
  `table_name` varchar(100) NOT NULL,
  `table_title` varchar(100) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 正在导出表  boot.db_build_table 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `db_build_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `db_build_table` ENABLE KEYS */;

-- 导出  表 boot.db_connection 结构
CREATE TABLE IF NOT EXISTS `db_connection` (
  `name` varchar(100) NOT NULL,
  `url` varchar(400) NOT NULL,
  `type` varchar(30) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `password` varchar(50) DEFAULT NULL,
  `note` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 正在导出表  boot.db_connection 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `db_connection` DISABLE KEYS */;
/*!40000 ALTER TABLE `db_connection` ENABLE KEYS */;

-- 导出  表 boot.org_agency 结构
CREATE TABLE IF NOT EXISTS `org_agency` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(120) NOT NULL COMMENT '单位名称',
  `type` tinyint(4) NOT NULL COMMENT '单位类型',
  `parent_id` int(11) DEFAULT NULL COMMENT '上级单位',
  `contact` varchar(50) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `order_no` int(11) NOT NULL DEFAULT '1' COMMENT '排序号',
  `create_time` datetime NOT NULL,
  `create_by` varchar(32) NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(32) DEFAULT NULL,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 正在导出表  boot.org_agency 的数据：~9 rows (大约)
/*!40000 ALTER TABLE `org_agency` DISABLE KEYS */;
INSERT INTO `org_agency` (`id`, `name`, `type`, `parent_id`, `contact`, `contact_phone`, `order_no`, `create_time`, `create_by`, `update_time`, `update_by`, `deleted`) VALUES
	(1, '第一人民医院', 1, NULL, '秦河谷', '0512-57777777', 1, '2021-07-26 10:08:51', 'ad313d38fe9447ce863fe8584743a010', '2021-07-26 10:08:51', 'ad313d38fe9447ce863fe8584743a010', b'0'),
	(2, '友谊分院', 1, 1, '游道思', '0512-57777777', 2, '2021-07-26 10:15:59', 'ad313d38fe9447ce863fe8584743a010', '2021-07-26 10:15:59', 'ad313d38fe9447ce863fe8584743a010', b'0'),
	(3, '本院', 1, 1, '易簿思', '0512-57777771', 1, '2021-07-26 10:17:00', 'ad313d38fe9447ce863fe8584743a010', '2021-07-26 10:30:47', 'ad313d38fe9447ce863fe8584743a010', b'0'),
	(4, '亿达集团', 2, NULL, '柯思冲', '0512-51111111', 2, '2021-07-26 10:24:59', 'ad313d38fe9447ce863fe8584743a010', '2021-07-26 10:24:59', 'ad313d38fe9447ce863fe8584743a010', b'0'),
	(5, '昆山分部', 2, 4, '柯思冲', '', 1, '2021-07-26 10:38:51', 'ad313d38fe9447ce863fe8584743a010', '2021-07-26 10:41:39', 'ad313d38fe9447ce863fe8584743a010', b'0'),
	(6, '对方水电费', 1, NULL, '防守打法', '232', 3, '2021-07-26 10:39:00', 'ad313d38fe9447ce863fe8584743a010', '2021-07-26 10:40:50', 'ad313d38fe9447ce863fe8584743a010', b'1'),
	(7, '南京分部', 2, 4, '柯思冲', '', 2, '2021-07-26 10:41:59', 'ad313d38fe9447ce863fe8584743a010', '2021-07-26 10:41:59', 'ad313d38fe9447ce863fe8584743a010', b'0'),
	(8, '阿狸妈咪集团', 2, NULL, '马赟', '0123-12345678', 3, '2021-07-26 10:43:50', 'ad313d38fe9447ce863fe8584743a010', '2021-07-26 10:43:50', 'ad313d38fe9447ce863fe8584743a010', b'0'),
	(9, '罔布南有限公司', 2, NULL, '朱荣酒', '0123-12345679', 4, '2021-07-26 10:45:32', 'ad313d38fe9447ce863fe8584743a010', '2021-07-26 10:45:32', 'ad313d38fe9447ce863fe8584743a010', b'0');
/*!40000 ALTER TABLE `org_agency` ENABLE KEYS */;

-- 导出  表 boot.org_menu 结构
CREATE TABLE IF NOT EXISTS `org_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(50) NOT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT 'url',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `parent_id` int(11) DEFAULT NULL COMMENT '父ID',
  `is_admin` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否系统管理员权限',
  `order_no` int(11) NOT NULL DEFAULT '0' COMMENT '列表顺序',
  `grantable` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否可授予',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3041 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 正在导出表  boot.org_menu 的数据：~7 rows (大约)
/*!40000 ALTER TABLE `org_menu` DISABLE KEYS */;
INSERT INTO `org_menu` (`id`, `name`, `url`, `icon`, `parent_id`, `is_admin`, `order_no`, `grantable`) VALUES
	(1000, '系统管理', NULL, NULL, NULL, b'1', 0, 1),
	(1010, '角色管理', '/common/org/role/index', NULL, 1000, b'1', 1, 1),
	(1020, '登录日志', '/common/sys/logger/login/index', NULL, 1000, b'1', 998, 1),
	(1030, '操作日志', '/common/sys/logger/operate/index', NULL, 1000, b'1', 999, 1),
	(2000, '组织管理', NULL, NULL, NULL, b'1', 1, 1),
	(2010, '人员管理', '/demo/org/personnel/index', NULL, 2000, b'1', 1, 1),
	(2020, '机构管理', '/demo/org/agency/index', NULL, 2000, b'1', 1, 1);
/*!40000 ALTER TABLE `org_menu` ENABLE KEYS */;

-- 导出  表 boot.org_permission 结构
CREATE TABLE IF NOT EXISTS `org_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(50) NOT NULL COMMENT '权限名称',
  `code` varchar(200) NOT NULL COMMENT '表现内容',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '权限类型',
  `description` varchar(500) DEFAULT NULL COMMENT '权限描述',
  `parent_id` int(11) DEFAULT NULL COMMENT '父ID',
  `is_admin` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否系统管理员权限',
  `order_no` int(11) NOT NULL DEFAULT '0' COMMENT '列表顺序',
  `grantable` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否可授予',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2027 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 正在导出表  boot.org_permission 的数据：~7 rows (大约)
/*!40000 ALTER TABLE `org_permission` DISABLE KEYS */;
INSERT INTO `org_permission` (`id`, `name`, `code`, `type`, `description`, `parent_id`, `is_admin`, `order_no`, `grantable`) VALUES
	(1000, '系统管理', 'sys', 1, NULL, NULL, b'1', 0, 1),
	(1010, '角色管理', 'sys:role', 1, '角色管理相关权限', 1000, b'1', 997, 1),
	(1012, '新增角色', 'sys:role:save', 2, '新增一个角色', 1010, b'1', 3, 1),
	(1013, '修改角色', 'sys:role:update', 2, '修改一个角色', 1010, b'1', 4, 1),
	(1014, '权限授权', 'sys:role:grant', 2, '给角色授权', 1010, b'1', 5, 1),
	(2000, '组织管理', 'org', 1, '人员组织相关权限', NULL, b'1', 1, 1),
	(2010, '人员管理', 'org:personnel', 1, '人员管理', 2000, b'1', 1, 1),
	(2013, '新增人员', 'org:personnel:save', 2, NULL, 2010, b'1', 3, 1),
	(2014, '修改人员', 'org:personnel:update', 2, NULL, 2010, b'1', 4, 1),
	(2015, '删除人员', 'org:personnel:delete', 2, NULL, 2010, b'1', 5, 1),
	(2016, '导出人员', 'org:personnel:export', 2, NULL, 2010, b'1', 6, 1),
	(2020, '机构管理', 'org:agency', 1, NULL, 2000, b'1', 2, 1),
	(2023, '新增机构', 'org:agency:save', 2, NULL, 2020, b'1', 3, 1),
	(2024, '修改机构', 'org:agency:update', 2, NULL, 2020, b'1', 4, 1),
	(2025, '删除机构', 'org:agency:delete', 2, NULL, 2020, b'1', 5, 1),
	(2026, '导出机构', 'org:agency:export', 2, NULL, 2020, b'1', 6, 1);
/*!40000 ALTER TABLE `org_permission` ENABLE KEYS */;

-- 导出  表 boot.org_personnel 结构
CREATE TABLE IF NOT EXISTS `org_personnel` (
  `id` varchar(32) NOT NULL,
  `account` varchar(50) DEFAULT NULL COMMENT '账号',
  `agency_id` int(11) NOT NULL DEFAULT '0' COMMENT '所属机构',
  `identification_type` tinyint(4) NOT NULL COMMENT '证件类型',
  `identification_no` varchar(32) NOT NULL COMMENT '证件号码',
  `name` varchar(20) DEFAULT NULL COMMENT '姓名',
  `pinyin_name` varchar(20) DEFAULT NULL COMMENT '姓名的拼音首字符',
  `sex` tinyint(4) DEFAULT NULL COMMENT '性别',
  `birthday` datetime DEFAULT NULL COMMENT '出生日期',
  `cellphone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `office_phone` varchar(20) DEFAULT NULL COMMENT '电话号码',
  `nation` smallint(6) DEFAULT NULL COMMENT '民族',
  `resume` text COMMENT '简历',
  `attachment` varchar(200) DEFAULT NULL COMMENT '附件',
  `roles` varchar(200) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `create_by` varchar(32) NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(32) DEFAULT NULL,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 正在导出表  boot.org_personnel 的数据：~4 rows (大约)
/*!40000 ALTER TABLE `org_personnel` DISABLE KEYS */;
INSERT INTO `org_personnel` (`id`, `account`, `agency_id`, `identification_type`, `identification_no`, `name`, `pinyin_name`, `sex`, `birthday`, `cellphone`, `office_phone`, `nation`, `resume`, `attachment`, `roles`, `create_time`, `create_by`, `update_time`, `update_by`, `deleted`) VALUES
	('3FQAKB7yLfeJkiPRO3SA2g', 'xiaoyoutai', 9, 1, '55555', '肖犹太', 'xyt', 0, '2010-07-27 00:00:00', '', '', 9, '<p><span style="color: rgb(64, 64, 64); font-family: Arial, &quot;Hiragino Sans GB&quot;, STHeiti, &quot;Helvetica Neue&quot;, Helvetica, &quot;Microsoft Yahei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 18px; text-align: justify; text-indent: 36px;">今天（27日）上午10时30分，南京市举行第七场新冠肺炎疫情防控新闻发布会，通报疫情防控最新情况。据通报，2021年7月26日0到24时，南京市新增本土新冠肺炎确诊病例31例，其中7例无症状感染者转为确诊病例，</span><strong style="box-sizing: inherit; color: rgb(64, 64, 64); font-family: Arial, &quot;Hiragino Sans GB&quot;, STHeiti, &quot;Helvetica Neue&quot;, Helvetica, &quot;Microsoft Yahei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 18px; text-align: justify; text-indent: 36px; white-space: normal;">新增报告病例数仍然主要集中在重点管控区域</strong><span style="color: rgb(64, 64, 64); font-family: Arial, &quot;Hiragino Sans GB&quot;, STHeiti, &quot;Helvetica Neue&quot;, Helvetica, &quot;Microsoft Yahei&quot;, &quot;WenQuanYi Micro Hei&quot;, sans-serif; font-size: 18px; text-align: justify; text-indent: 36px;">，相关密切接触者和次密均已按照有关规定进行甄别管理。</span></p>', '', '2', '2021-07-27 11:17:16', 'ad313d38fe9447ce863fe8584743a010', '2021-07-27 13:50:47', 'ad313d38fe9447ce863fe8584743a010', b'0'),
	('cwoHyUw5vIfqp8L57_GhKA', 'wangbo', 4, 1, '34422', '王波', 'wb', 1, NULL, '', '', NULL, '', NULL, '3', '2021-07-27 17:08:54', 'ad313d38fe9447ce863fe8584743a010', '2021-07-27 17:08:54', 'ad313d38fe9447ce863fe8584743a010', b'0'),
	('v7KCmXqikowEuzoekaqupA', 'heyifan', 8, 1, '3927', '何一凡', 'hyf', 1, '2013-07-26 00:00:00', '13584950666', '', 3, '', NULL, '3', '2021-07-26 17:39:06', 'ad313d38fe9447ce863fe8584743a010', '2021-07-26 17:39:06', 'ad313d38fe9447ce863fe8584743a010', b'0'),
	('Yu3dcTbdcCZehmwQrC-Nfw', 'suhe000', 1, 1, '132222', '苏荷', 'sh', 1, NULL, '', '', NULL, '', NULL, '2,3', '2021-07-27 17:17:39', 'ad313d38fe9447ce863fe8584743a010', '2021-07-27 17:17:39', 'ad313d38fe9447ce863fe8584743a010', b'0');
/*!40000 ALTER TABLE `org_personnel` ENABLE KEYS */;

-- 导出  表 boot.org_role 结构
CREATE TABLE IF NOT EXISTS `org_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_name` varchar(32) NOT NULL COMMENT '角色名称',
  `role_level` tinyint(4) NOT NULL COMMENT '角色等级（考核权限等级）',
  `role_desc` varchar(500) DEFAULT NULL COMMENT '角色说明',
  `enable` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 正在导出表  boot.org_role 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `org_role` DISABLE KEYS */;
INSERT INTO `org_role` (`id`, `role_name`, `role_level`, `role_desc`, `enable`, `create_time`, `create_by`, `update_time`, `update_by`) VALUES
	(1, '应用管理员', 9, NULL, b'1', '2021-06-07 16:19:59', 'admin', '2021-07-29 14:00:09', 'ad313d38fe9447ce863fe8584743a010'),
	(2, '机构管理员', 2, NULL, b'1', '2021-06-07 16:20:24', 'admin', '2021-07-29 14:00:16', 'ad313d38fe9447ce863fe8584743a010'),
	(3, '个人', 1, NULL, b'1', '2021-07-26 17:07:29', 'admin', '2021-07-26 17:07:31', 'admin');
/*!40000 ALTER TABLE `org_role` ENABLE KEYS */;

-- 导出  表 boot.org_role_menu 结构
CREATE TABLE IF NOT EXISTS `org_role_menu` (
  `role_id` int(11) NOT NULL DEFAULT '0' COMMENT '角色ID',
  `menu_id` int(11) NOT NULL DEFAULT '0' COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 正在导出表  boot.org_role_menu 的数据：~11 rows (大约)
/*!40000 ALTER TABLE `org_role_menu` DISABLE KEYS */;
INSERT INTO `org_role_menu` (`role_id`, `menu_id`) VALUES
	(1, 1000),
	(1, 1010),
	(1, 1020),
	(1, 1030),
	(1, 2000),
	(1, 2010),
	(1, 2020),
	(2, 2000),
	(2, 2010),
	(2, 2020),
	(3, 2010);
/*!40000 ALTER TABLE `org_role_menu` ENABLE KEYS */;

-- 导出  表 boot.org_role_permission 结构
CREATE TABLE IF NOT EXISTS `org_role_permission` (
  `role_id` int(11) NOT NULL DEFAULT '0' COMMENT '角色ID',
  `permission_id` int(11) NOT NULL DEFAULT '0' COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`permission_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 正在导出表  boot.org_role_permission 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `org_role_permission` DISABLE KEYS */;
INSERT INTO `org_role_permission` (`role_id`, `permission_id`) VALUES
	(1, 1000),
	(1, 1010),
	(1, 1012),
	(1, 1013),
	(1, 1014),
	(1, 2000),
	(1, 2010),
	(1, 2013),
	(1, 2014),
	(1, 2015),
	(1, 2016),
	(1, 2020),
	(1, 2023),
	(1, 2024),
	(1, 2025),
	(1, 2026),
	(2, 2010),
	(2, 2013),
	(2, 2014),
	(2, 2015),
	(2, 2016);
/*!40000 ALTER TABLE `org_role_permission` ENABLE KEYS */;

-- 导出  表 boot.sys_attachment 结构
CREATE TABLE IF NOT EXISTS `sys_attachment` (
  `id` varchar(32) NOT NULL,
  `store_type` varchar(20) NOT NULL COMMENT '存储方式',
  `name` varchar(100) NOT NULL,
  `suffix` varchar(10) DEFAULT NULL,
  `size` bigint(20) unsigned NOT NULL,
  `relative_path` varchar(200) NOT NULL,
  `thumbnail_relative_path` varchar(200) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `operate_by` varchar(50) DEFAULT NULL COMMENT '哪个用户创建',
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `operate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 正在导出表  boot.sys_attachment 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `sys_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_attachment` ENABLE KEYS */;

-- 导出  表 boot.sys_constant 结构
CREATE TABLE IF NOT EXISTS `sys_constant` (
  `type` varchar(32) NOT NULL COMMENT '分类',
  `code` varchar(11) NOT NULL COMMENT '编码ID',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `order_no` int(11) DEFAULT NULL COMMENT '排序号',
  PRIMARY KEY (`type`,`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 正在导出表  boot.sys_constant 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `sys_constant` DISABLE KEYS */;
INSERT INTO `sys_constant` (`type`, `code`, `name`, `order_no`) VALUES
	('agency-type', '1', '公立机构', 1),
	('agency-type', '2', '私人企业', 2),
	('agency-type', '3', '社会团体', 3),
	('boolean-type', '0', '否', 2),
	('boolean-type', '1', '是', 1),
	('identification-type', '1', '身份证', 1),
	('identification-type', '2', '居民户口簿', 2),
	('identification-type', '3', '护照', 3),
	('identification-type', '4', '军官证', 4),
	('identification-type', '5', '驾驶执照', 5),
	('identification-type', '6', '港澳居民来往内地通行证', 6),
	('identification-type', '7', '台湾居民来往内地通行证', 7),
	('identification-type', '8', '其他', 8),
	('nation-type', '1', '汉族', 1),
	('nation-type', '10', '朝鲜族', 10),
	('nation-type', '11', '满族', 11),
	('nation-type', '12', '侗族', 12),
	('nation-type', '13', '瑶族', 13),
	('nation-type', '14', '白族', 14),
	('nation-type', '15', '土家族', 15),
	('nation-type', '16', '哈尼族', 16),
	('nation-type', '17', '哈萨克族', 17),
	('nation-type', '18', '傣族', 18),
	('nation-type', '19', '黎族', 19),
	('nation-type', '2', '蒙古族', 2),
	('nation-type', '20', '僳僳族', 20),
	('nation-type', '21', '佤族', 21),
	('nation-type', '22', '畲族', 22),
	('nation-type', '23', '高山族', 23),
	('nation-type', '24', '拉祜族', 24),
	('nation-type', '25', '水族', 25),
	('nation-type', '26', '东乡族', 26),
	('nation-type', '27', '纳西族', 27),
	('nation-type', '28', '景颇族', 28),
	('nation-type', '29', '柯尔克孜族', 29),
	('nation-type', '3', '回族', 3),
	('nation-type', '30', '土族', 30),
	('nation-type', '31', '达斡尔族', 31),
	('nation-type', '32', '仫佬族', 32),
	('nation-type', '33', '羌族', 33),
	('nation-type', '34', '布朗族', 34),
	('nation-type', '35', '撤拉族', 35),
	('nation-type', '36', '毛南族', 36),
	('nation-type', '37', '仡佬族', 37),
	('nation-type', '38', '锡伯族', 38),
	('nation-type', '39', '阿昌族', 39),
	('nation-type', '4', '藏族', 4),
	('nation-type', '40', '普米族', 40),
	('nation-type', '41', '塔吉克族', 41),
	('nation-type', '42', '怒族', 42),
	('nation-type', '43', '乌孜别克族', 43),
	('nation-type', '44', '俄罗斯族', 44),
	('nation-type', '45', '鄂温克族', 45),
	('nation-type', '46', '崩龙族', 46),
	('nation-type', '47', '保安族', 47),
	('nation-type', '48', '裕固族', 48),
	('nation-type', '49', '京族', 49),
	('nation-type', '5', '维吾尔族', 5),
	('nation-type', '50', '塔塔尔族', 50),
	('nation-type', '51', '独龙族', 51),
	('nation-type', '52', '鄂伦春族', 52),
	('nation-type', '53', '赫哲族', 53),
	('nation-type', '54', '门巴族', 54),
	('nation-type', '55', '珞巴族', 55),
	('nation-type', '56', '基诺族', 56),
	('nation-type', '57', '外籍', 57),
	('nation-type', '58', '不详', 58),
	('nation-type', '6', '苗族', 6),
	('nation-type', '7', '彝族', 7),
	('nation-type', '8', '壮族', 8),
	('nation-type', '9', '布依族', 9),
	('notice-type', '0', '公告', 0),
	('notice-type', '1', '文件', 1),
	('permission-type', '1', '分类', 1),
	('permission-type', '2', '功能', 2),
	('role-level-type', '1', '个人', 1),
	('role-level-type', '2', '机构', 2),
	('role-level-type', '9', '管理员', 3),
	('sex-type', '0', '女', 2),
	('sex-type', '1', '男', 1);
/*!40000 ALTER TABLE `sys_constant` ENABLE KEYS */;

-- 导出  表 boot.sys_logger_login 结构
CREATE TABLE IF NOT EXISTS `sys_logger_login` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(50) NOT NULL,
  `login_type` tinyint(4) NOT NULL COMMENT '登录方式',
  `account` varchar(50) NOT NULL COMMENT '登录账号',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `user_type` tinyint(4) NOT NULL COMMENT '用户类型',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 正在导出表  boot.sys_logger_login 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `sys_logger_login` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_logger_login` ENABLE KEYS */;

-- 导出  表 boot.sys_logger_operate 结构
CREATE TABLE IF NOT EXISTS `sys_logger_operate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `model_name` varchar(30) NOT NULL COMMENT '模块名称',
  `operate_name` varchar(50) NOT NULL COMMENT '操作名称',
  `class_name` varchar(100) NOT NULL COMMENT '类名称',
  `method_name` varchar(40) NOT NULL COMMENT '方法名称',
  `is_success` bit(1) NOT NULL COMMENT '是否成功',
  `error_message` varchar(255) DEFAULT NULL COMMENT '错误信息',
  `operate_by` varchar(32) NOT NULL COMMENT '操作人',
  `operate_by_name` varchar(50) DEFAULT NULL COMMENT '操作人名称',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `operate_duration` int(11) DEFAULT NULL COMMENT '操作时长',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 正在导出表  boot.sys_logger_operate 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `sys_logger_operate` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_logger_operate` ENABLE KEYS */;

-- 导出  表 boot.sys_user 结构
CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `account` varchar(30) NOT NULL COMMENT '账号',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `salt` varchar(32) NOT NULL COMMENT '盐',
  `user_id` varchar(32) NOT NULL COMMENT '用户信息表ID',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户类型',
  `state` tinyint(4) NOT NULL DEFAULT '1' COMMENT '账号状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_account` (`account`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- 正在导出表  boot.sys_user 的数据：~5 rows (大约)
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` (`id`, `account`, `password`, `salt`, `user_id`, `type`, `state`, `create_time`, `last_login_time`, `update_time`) VALUES
	('20626e82bd21640ccb088c5029cda162', 'wangbo', 'a463d6f271f4eea1b3ce83cb1dbe4b37', 'a458ba437396d74f01239b0cd0eb2e59', 'cwoHyUw5vIfqp8L57_GhKA', 3, 1, '2021-07-27 17:08:54', NULL, NULL),
	('49783fca94d258c3c0157a3f10ecd3d3', 'heyifan', 'f6d5dbe6926407fdae5c25638e4a509b', '633c7b88455a4fafd83b153c9275976e', 'v7KCmXqikowEuzoekaqupA', 3, 1, '2021-07-26 17:39:06', NULL, NULL),
	('87f547dbc3e696fb2d2368fe6a2556a4', 'xiaoyoutai', 'd5b2503dd6637979c890960b92eecb14', '99a93587b5a4651fa636aad7150052f4', '3FQAKB7yLfeJkiPRO3SA2g', 3, 1, '2021-07-27 11:17:16', NULL, NULL),
	('88d52983e25bc8be962009c04765a4bc', 'suhe000', '3d2ab1c8f284d66ee366ee5cabcaecea', 'fe0b1095188bcc47e65b606eec2c7cc0', 'Yu3dcTbdcCZehmwQrC-Nfw', 3, 1, '2021-07-27 17:17:39', NULL, NULL),
	('ad313d38fe9447ce863fe8584743a010', 'admin', 'e22c088055a5ada418f9a52be6676535', '3baf18b030ea37785a0a112a9f180c9d', '', 1, 1, '2021-06-07 16:02:54', '2021-06-07 16:02:39', '2021-06-07 16:02:44');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;

-- 导出  表 boot.sys_visit_cache 结构
CREATE TABLE IF NOT EXISTS `sys_visit_cache` (
  `id` varchar(32) NOT NULL,
  `ip` varchar(50) NOT NULL COMMENT 'IP',
  `code` varchar(20) NOT NULL COMMENT 'IP',
  `value` varchar(400) NOT NULL COMMENT '内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;


