/*
Navicat MySQL Data Transfer

Source Server         : localhost_root
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : boot

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2020-03-23 11:13:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `data_source`
-- ----------------------------
DROP TABLE IF EXISTS `data_source`;
CREATE TABLE `data_source` (
  `name` varchar(100) NOT NULL,
  `url` varchar(400) NOT NULL,
  `type` varchar(30) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `password` varchar(50) DEFAULT NULL,
  `note` varchar(500) DEFAULT NULL,
  `enabled` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of data_source
-- ----------------------------

-- ----------------------------
-- Table structure for `db_build_column`
-- ----------------------------
DROP TABLE IF EXISTS `db_build_column`;
CREATE TABLE `db_build_column` (
  `id` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `connection_name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `table_name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `column_name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `title` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `enum_code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '常量CODE',
  `multi_select` tinyint(4) DEFAULT NULL COMMENT '是否多选',
  `is_attachment` tinyint(4) DEFAULT NULL COMMENT '是否附件',
  `attachment_count` int(11) DEFAULT NULL COMMENT '附件数量',
  `required` tinyint(4) DEFAULT NULL COMMENT '是否必填',
  `tableable` tinyint(4) DEFAULT NULL COMMENT '是否列表显示',
  `editable` tinyint(4) DEFAULT NULL COMMENT '是否可编辑',
  `addable` tinyint(4) DEFAULT NULL COMMENT '是否新增',
  `large_text` tinyint(4) DEFAULT NULL COMMENT '是否大文本',
  `max_length` int(11) DEFAULT NULL COMMENT '最大长度',
  `regular_expression` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '正则验证表达式',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


-- ----------------------------
-- Table structure for `db_build_table`
-- ----------------------------
DROP TABLE IF EXISTS `db_build_table`;
CREATE TABLE `db_build_table` (
  `id` varchar(32) CHARACTER SET utf8 NOT NULL,
  `connection_name` varchar(100) CHARACTER SET utf8 NOT NULL,
  `table_name` varchar(100) CHARACTER SET utf8 NOT NULL,
  `table_title` varchar(100) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- ----------------------------
-- Table structure for `db_connection`
-- ----------------------------
DROP TABLE IF EXISTS `db_connection`;
CREATE TABLE `db_connection` (
  `name` varchar(100) NOT NULL,
  `url` varchar(400) NOT NULL,
  `type` varchar(30) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `password` varchar(50) DEFAULT NULL,
  `note` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of db_connection
-- ----------------------------
INSERT INTO `db_connection` VALUES ('db_boot', 'jdbc:mysql://localhost:3306/boot?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8', 'MYSQL', 'root', '', '');

-- ----------------------------
-- Table structure for `org_permission`
-- ----------------------------
DROP TABLE IF EXISTS `org_permission`;
CREATE TABLE `org_permission` (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '权限名称',
  `url` varchar(200) DEFAULT NULL COMMENT '表现形式1：URL2：CODE',
  `code` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '表现内容',
  `is_menu` tinyint(4) NOT NULL COMMENT '是否菜单',
  `is_page` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否页面',
  `menu_icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '图标',
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '权限描述',
  `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '父ID',
  `is_admin` tinyint(4) NOT NULL COMMENT '是否系统管理员权限',
  `list_order` int(11) NOT NULL DEFAULT '0' COMMENT '列表顺序',
  `grantable` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否可授予',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of org_permission
-- ----------------------------
INSERT INTO `org_permission` VALUES ('1000', '系统管理', null, 'sys', '1', '0', null, null, null, '1', '0', '1', '2020-01-01 00:00:00', 'admin', '2020-01-01 00:00:00', 'admin', '0');
INSERT INTO `org_permission` VALUES ('1010', '角色管理', '', 'sys:role', '0', '0', '', null, '1000', '1', '997', '1', '2020-01-01 00:00:00', 'admin', '2020-01-01 00:00:00', 'admin', '0');
INSERT INTO `org_permission` VALUES ('1011', '角色管理', '/common/org/role/index', 'sys:role:index', '1', '1', '', null, '1010', '1', '1', '1', '2020-01-01 00:00:00', 'admin', '2020-01-01 00:00:00', 'admin', '0');
INSERT INTO `org_permission` VALUES ('1012', '新增角色', '', 'sys:role:save', '0', '0', null, null, '1010', '0', '3', '1', '2020-01-01 00:00:00', 'admin', '2020-01-01 00:00:00', 'admin', '0');
INSERT INTO `org_permission` VALUES ('1013', '修改角色', '', 'sys:role:update', '0', '0', null, null, '1010', '0', '4', '1', '2020-01-01 00:00:00', 'admin', '2020-01-01 00:00:00', 'admin', '0');
INSERT INTO `org_permission` VALUES ('1014', '权限授权', '', 'sys:role:grant', '0', '0', null, null, '1010', '0', '5', '1', '2020-01-01 00:00:00', 'admin', '2020-01-01 00:00:00', 'admin', '0');
INSERT INTO `org_permission` VALUES ('1110', '登录日志查看', '/common/sys/logger/login/index', 'sys:logger:login:index', '1', '1', null, null, '1000', '1', '998', '1', '2020-01-01 00:00:00', 'admin', '2020-01-01 00:00:00', 'admin', '0');
INSERT INTO `org_permission` VALUES ('1900', '容器管理', '/common/container/index', 'admin:container', '1', '1', null, null, '1000', '1', '999', '0', '2020-01-01 00:00:00', 'admin', '2020-01-01 00:00:00', 'admin', '0');
INSERT INTO `org_permission` VALUES ('2000', '组织管理', null, 'org', '1', '0', null, null, null, '0', '1', '1', '2020-01-01 00:00:00', 'admin', '2020-01-01 00:00:00', 'admin', '0');
INSERT INTO `org_permission` VALUES ('2010', '人员管理', '', 'org:personnel', '0', '0', null, null, '2000', '0', '1', '1', '2020-01-01 00:00:00', 'admin', '2020-01-01 00:00:00', 'admin', '0');
INSERT INTO `org_permission` VALUES ('2011', '人员管理', '/demo/org/personnel/index', 'org:personnel:index', '1', '1', null, null, '2010', '0', '1', '1', '2020-01-01 00:00:00', 'admin', '2020-01-01 00:00:00', 'admin', '0');
INSERT INTO `org_permission` VALUES ('2013', '新增人员', '', 'org:personnel:save', '0', '0', null, null, '2010', '0', '3', '1', '2020-01-01 00:00:00', 'admin', '2020-01-01 00:00:00', 'admin', '0');
INSERT INTO `org_permission` VALUES ('2014', '修改人员', '', 'org:personnel:update', '0', '0', null, null, '2010', '0', '4', '1', '2020-01-01 00:00:00', 'admin', '2020-01-01 00:00:00', 'admin', '0');
INSERT INTO `org_permission` VALUES ('2015', '删除人员', '', 'org:personnel:delete', '0', '0', null, null, '2010', '0', '5', '1', '2020-01-01 00:00:00', 'admin', '2020-01-01 00:00:00', 'admin', '0');
INSERT INTO `org_permission` VALUES ('2016', '导出人员', '', 'org:personnel:export', '0', '0', null, null, '2010', '0', '6', '1', '2020-01-01 00:00:00', 'admin', '2020-01-01 00:00:00', 'admin', '0');
INSERT INTO `org_permission` VALUES ('2020', '机构管理', '', 'org:unit', '0', '0', null, null, '2000', '0', '2', '1', '2020-01-01 00:00:00', 'admin', '2020-01-01 00:00:00', 'admin', '0');
INSERT INTO `org_permission` VALUES ('2021', '机构管理', '/demo/org/unit/index', 'org:unit:index', '1', '1', null, null, '2020', '0', '1', '1', '2020-01-01 00:00:00', 'admin', '2020-01-01 00:00:00', 'admin', '0');
INSERT INTO `org_permission` VALUES ('2023', '新增单位', '', 'org:unit:save', '0', '0', null, null, '2020', '0', '3', '1', '2020-01-01 00:00:00', 'admin', '2020-01-01 00:00:00', 'admin', '0');
INSERT INTO `org_permission` VALUES ('2024', '修改单位', '', 'org:unit:update', '0', '0', null, null, '2020', '0', '4', '1', '2020-01-01 00:00:00', 'admin', '2020-01-01 00:00:00', 'admin', '0');
INSERT INTO `org_permission` VALUES ('2025', '删除单位', '', 'org:unit:delete', '0', '0', null, null, '2020', '0', '5', '1', '2020-01-01 00:00:00', 'admin', '2020-01-01 00:00:00', 'admin', '0');

-- ----------------------------
-- Table structure for `org_personnel`
-- ----------------------------
DROP TABLE IF EXISTS `org_personnel`;
CREATE TABLE `org_personnel` (
  `id` varchar(32) NOT NULL,
  `account` varchar(50) DEFAULT NULL COMMENT '账号',
  `unit_id` varchar(32) NOT NULL COMMENT '所属机构',
  `identification_type` tinyint(4) NOT NULL COMMENT '证件类型',
  `identification_no` varchar(32) NOT NULL COMMENT '证件号码',
  `name` varchar(20) DEFAULT NULL COMMENT '姓名',
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
  `update_time` datetime NOT NULL,
  `update_by` varchar(32) NOT NULL,
  `deleted` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of org_personnel
-- ----------------------------
INSERT INTO `org_personnel` VALUES ('7yqslBkAG-XCjFmtaH4-8Q', 'zhangtianshuo', '6kf6Ds_Wp5wIVM_BFgoUKw', '1', '000000000000004', '张天硕', '1', '2020-03-02 00:00:00', '13584950000', '', null, '', null, 'G96rCbCnoN4tgI3E0FQXHQ', '2020-03-17 10:56:27', 'ad313d38fe9447ce863fe8584743a010', '2020-03-17 10:56:27', 'ad313d38fe9447ce863fe8584743a010', '0');
INSERT INTO `org_personnel` VALUES ('BrocI_IUOi8uvcVesEWEJQ', 'zhouxuwu', 'kBu6Ud4RES25xcHAIxeCJQ', '1', '000000000000001', '周旭武', '1', '1990-03-17 00:00:00', '13584950000', '051257700000', '1', '<p style=\"margin-top: 32px; margin-bottom: 0px; padding: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 18px; line-height: 32px; font-family: &quot;Microsoft Yahei&quot;; text-indent: 2em; color: rgb(64, 64, 64); text-align: justify; white-space: normal; background-color: rgb(255, 255, 255);\">3月13日，本市报告1例从美国输入的确诊病例，黎某，女，37岁，长期定居美国马萨诸塞州。3月14、15日，市疾控中心和中国国际航空公司在北京市新冠肺炎疫情防控工作新闻发布会上通报，黎某在美期间已出现发热、咳嗽等症状，并多次在当地就诊，在美国登机前曾服用退烧药，登机后未如实向乘务人员提供个人健康状况及丈夫、儿子等同行人员情况，给同机人员造成传染风险。</p><p style=\"margin-top: 32px; margin-bottom: 0px; padding: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 18px; line-height: 32px; font-family: &quot;Microsoft Yahei&quot;; text-indent: 2em; color: rgb(64, 64, 64); text-align: center; white-space: normal; background-color: rgb(255, 255, 255);\"><img src=\"/file/20200317/fb5be8fabe534018ae9b8ef16c20a615.jpeg\" title=\"英女皇与她的狗.jpeg\" alt=\"英女皇与她的狗.jpeg\"/></p><p style=\"margin-top: 32px; margin-bottom: 0px; padding: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 18px; line-height: 32px; font-family: &quot;Microsoft Yahei&quot;; text-indent: 2em; color: rgb(64, 64, 64); text-align: justify; white-space: normal; background-color: rgb(255, 255, 255);\">接相关线索后，北京警方迅速开展调查取证。经初步工作，顺义公安分局以涉嫌妨害传染病防治罪，已对黎某立案侦查。目前，黎某正在定点医院接受治疗，其丈夫、儿子接受隔离观察。</p><p style=\"margin-top: 32px; margin-bottom: 0px; padding: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 18px; line-height: 32px; font-family: &quot;Microsoft Yahei&quot;; text-indent: 2em; color: rgb(64, 64, 64); text-align: justify; white-space: normal; background-color: rgb(255, 255, 255);\">当前境外疫情加速扩散蔓延，防范境外疫情输入是防控工作的重中之重，所有入境进京人员应严格遵守相关法律及疫情防控相关规定。对虚报信息、隐瞒病情等拒绝执行疫情防控措施的违法犯罪行为，北京警方将坚决打击处理、依法追究责任。</p><p><br/></p>', '3c3c24303ff2498f8056eb87e5679aee,daf5b54e172e4401aaf4a6fc499fd297', 'wKlw-zXI_VFRbbwsltWM4Q,G96rCbCnoN4tgI3E0FQXHQ,1', '2020-03-17 09:11:26', 'ad313d38fe9447ce863fe8584743a010', '2020-03-18 17:13:15', 'BrocI_IUOi8uvcVesEWEJQ', '0');
INSERT INTO `org_personnel` VALUES ('FrX7JyqZVB3W3u41wBtprw', 'zhouxingxing', '1fT4-742peLu6zcRerRUUQ', '1', '000000000000011', '周星兴', '1', '2016-03-01 00:00:00', '13584950000', '051257700000', '3', '', '5d1ef80e8b7345d8956d1660370dc21d', 'wKlw-zXI_VFRbbwsltWM4Q', '2020-03-17 10:58:30', 'ad313d38fe9447ce863fe8584743a010', '2020-03-19 15:36:50', 'BrocI_IUOi8uvcVesEWEJQ', '0');
INSERT INTO `org_personnel` VALUES ('lHMmloreL_B1rXrDmul8Qw', 'yangyang', '7gGneX0jb792kBFCPcwPag', '1', '000000000000002', '杨洋', '1', '2013-11-05 00:00:00', '13584950001', '051257700000', '2', '<p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px; line-height: 24px; color: rgb(51, 51, 51); text-align: justify; font-family: arial; white-space: normal; background-color: rgb(255, 255, 255);\"><span class=\"bjh-p\" style=\"display: block;\">“孙杨事件”是今年中国体育最劲爆的消息，它同样也让全世界感到惊讶。事情已经过去半个多月，整个事情的舆论风向一波三折。从全体体育迷替其喊冤叫屈，到全民攻击。而如今，事情似乎又要反转，从种种消息曝光来看，<span class=\"bjh-strong\" style=\"font-size: 18px; font-weight: 700;\">孙杨的翻案机会在不断增加。</span></span></p><p><img class=\"large\" src=\"https://pics5.baidu.com/feed/d833c895d143ad4b10d876f54a7104a9a50f068e.jpeg?token=21cb12c1978e7b3ac3a024e48ab22c5a\"/></p><p style=\"margin-top: 26px; margin-bottom: 0px; padding: 0px; line-height: 24px; color: rgb(51, 51, 51); text-align: justify; font-family: arial; white-space: normal; background-color: rgb(255, 255, 255);\"><span class=\"bjh-p\" style=\"display: block;\">由于孙杨团队一直没发声，也没有发起最后一次上诉，大多数人开始猜测孙杨可能会放弃上诉，因为胜诉的概率毕竟只有7%。而且“孙杨事件”刚发生时，孙杨在自己的社交媒体连续发了4条自证清白的动态，有视频，有图片，看似有理有据。但后来CAS公布仲裁报告和视频之后，孙杨把4条动态全删了，<span class=\"bjh-strong\" style=\"font-size: 18px; font-weight: 700;\">有理亏也有心虚的感觉</span>。</span></p><p><img class=\"large\" src=\"https://pics2.baidu.com/feed/f703738da97739121da984e2306ad81e377ae224.jpeg?token=9183c563e050e35dfe9ef3d23f53128f\"/></p><p style=\"margin-top: 26px; margin-bottom: 0px; padding: 0px; line-height: 24px; color: rgb(51, 51, 51); text-align: justify; font-family: arial; white-space: normal; background-color: rgb(255, 255, 255);\"><span class=\"bjh-p\" style=\"display: block;\">随后还没等霍顿等人发声批评，国内的声音就如潮水般袭来。大家开始批评孙杨不懂规则，批评孙杨不守规矩，甚至很多人还怀疑孙杨和兴奋剂有联系，否则不可能抗拒检查。而这两天，美国媒体和国内记者对孙杨的支持，也让舆论的风向有些转变的意味。</span></p><p style=\"margin-top: 22px; margin-bottom: 0px; padding: 0px; line-height: 24px; color: rgb(51, 51, 51); text-align: justify; font-family: arial; white-space: normal; background-color: rgb(255, 255, 255);\"><span class=\"bjh-p\" style=\"display: block;\">先是美国《ABC》的报道力挺孙杨，他们曾列举出澳大利亚选手三次逃避检测只被禁赛12个月，另外还存在三名运动员各种借口逃避药检并没被处罚。就连一家澳大利亚媒体也力挺孙杨，他们认为孙杨案绝对不是我们现在所看到的那么简单。</span></p><p><img class=\"large\" src=\"https://pics0.baidu.com/feed/a5c27d1ed21b0ef44ebef7821ab70fdc80cb3e15.jpeg?token=b5511a60d997d6ace82dcb446399909c\"/></p><p style=\"margin-top: 26px; margin-bottom: 0px; padding: 0px; line-height: 24px; color: rgb(51, 51, 51); text-align: justify; font-family: arial; white-space: normal; background-color: rgb(255, 255, 255);\"><span class=\"bjh-p\" style=\"display: block;\">而最重要的是，今天则是曝光了一条关于规则修改的利好消息。《华盛顿邮报》记者乔纳森-怀特透露：WADA，也就是国际反兴奋剂机构，<span class=\"bjh-strong\" style=\"font-size: 18px; font-weight: 700;\">他们会在2021年1月1日重新制定检测新规则</span>，而在新规则出炉后，孙杨“抗检”这一事可能就是另一种判定了，届时刚刚禁赛一年的他就很有可能解禁。</span></p><p style=\"margin-top: 22px; margin-bottom: 0px; padding: 0px; line-height: 24px; color: rgb(51, 51, 51); text-align: justify; font-family: arial; white-space: normal; background-color: rgb(255, 255, 255);\"><span class=\"bjh-p\" style=\"display: block;\">还有英国媒体《insidethegame》发文力给予孙杨一些支持！这家媒体称认为孙杨在8年事件接受了高达180次的检测，在抗议前2个星期检测了9次，这有点折磨人。同时他们批评CAS的工作人员表现业余，检测人员对孙杨疯狂拍照，他们认为如果CAS不能做到一视同仁，那么判罚结果难以服众。</span></p><p><img class=\"normal\" width=\"500px\" src=\"https://pics5.baidu.com/feed/0b55b319ebc4b745b17167c8078f401188821585.jpeg?token=03892c5bfd10716a61a40c617aa94bc8\"/></p><p style=\"margin-top: 26px; margin-bottom: 0px; padding: 0px; line-height: 24px; color: rgb(51, 51, 51); text-align: justify; font-family: arial; white-space: normal; background-color: rgb(255, 255, 255);\"><span class=\"bjh-p\" style=\"display: block;\">孙杨被禁赛8年，我国体育迷心中充满疑问，也倍感惋惜。毕竟孙杨是我国最出色的游泳运动员，如果他被禁赛，那么我国的游泳在奥运会等国际大赛的竞争力将会大打折扣。我们不仅怀疑CAS是不是有些针对性，还在隐约猜测此事背后的阴谋是否存在。</span></p><p><img class=\"normal\" width=\"526px\" src=\"https://pics3.baidu.com/feed/a2cc7cd98d1001e9bd74f03c707d25ea54e79737.jpeg?token=3e62b834d81228789ed64db68801d815\"/></p><p style=\"margin-top: 26px; margin-bottom: 0px; padding: 0px; line-height: 24px; color: rgb(51, 51, 51); text-align: justify; font-family: arial; white-space: normal; background-color: rgb(255, 255, 255);\"><span class=\"bjh-p\" style=\"display: block;\">就在刚刚，有媒体曝光了东京奥运会的消息。消息称多名奥组委成员已经同意了将东京奥运会推迟两年举办，而如果明年WADA出台新规让孙杨提前解禁复出，孙杨则是刚好可以参加东京奥运会。<span class=\"bjh-br\"></span></span></p><p><img class=\"large\" src=\"https://pics0.baidu.com/feed/18d8bc3eb13533fa2f657a0360a0a31940345b42.jpeg?token=95ee3c1d58f8d81703466b04a858336b\"/></p><p style=\"margin-top: 26px; margin-bottom: 0px; padding: 0px; line-height: 24px; color: rgb(51, 51, 51); text-align: justify; font-family: arial; white-space: normal; background-color: rgb(255, 255, 255);\"><span class=\"bjh-p\" style=\"display: block;\">事情还在酝酿，禁赛八年不是最终结果。作为中国人，我们有理由支持一个为国争光的运动员，我们希望他的职业生涯得到延续。</span></p><p><br/></p>', 'c2ea86c1b58242b29b927cfbd8cc3cd6', 'G96rCbCnoN4tgI3E0FQXHQ', '2020-03-17 10:54:52', 'ad313d38fe9447ce863fe8584743a010', '2020-03-19 11:32:40', 'BrocI_IUOi8uvcVesEWEJQ', '0');
INSERT INTO `org_personnel` VALUES ('s0JE93DyHcP_Sp5IhAPffQ', 'liuwenwen', 'cY2VL408mS_e6ua5tppHpA', '1', '000000000000003', '刘文文', '0', '2020-03-09 00:00:00', '13584950000', '051257700000', '4', '', null, 'G96rCbCnoN4tgI3E0FQXHQ', '2020-03-17 10:55:38', 'ad313d38fe9447ce863fe8584743a010', '2020-03-17 10:55:38', 'ad313d38fe9447ce863fe8584743a010', '0');
INSERT INTO `org_personnel` VALUES ('WX14wsMFPCucbgiDLCLtFg', 'kewen001', 'cY2VL408mS_e6ua5tppHpA', '1', '000000000000006', '柯文', '0', '2020-03-03 00:00:00', '13584950000', '051257700000', '5', '', null, 'G96rCbCnoN4tgI3E0FQXHQ', '2020-03-17 10:57:24', 'ad313d38fe9447ce863fe8584743a010', '2020-03-17 10:57:24', 'ad313d38fe9447ce863fe8584743a010', '0');
INSERT INTO `org_personnel` VALUES ('zIpIlLzzpBaWYv2B6SgFCg', 'lisi001', 'tS6TrjSyX3jKZ2zf_7rIKw', '2', '0000000000000021', '李四', '0', null, '', '', null, '', 'ba0a0a245b3f4ec6b8d8d951b8ac92f5,ba7f3df9474d4983b0386e2b92a239f7', 'G96rCbCnoN4tgI3E0FQXHQ', '2020-03-19 11:33:11', 'BrocI_IUOi8uvcVesEWEJQ', '2020-03-19 15:55:51', 'BrocI_IUOi8uvcVesEWEJQ', '0');

-- ----------------------------
-- Table structure for `org_role`
-- ----------------------------
DROP TABLE IF EXISTS `org_role`;
CREATE TABLE `org_role` (
  `id` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT 'id',
  `role_name` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '角色名称',
  `role_level` tinyint(4) NOT NULL COMMENT '角色等级（考核权限等级）',
  `role_desc` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '角色说明',
  `is_default` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否默认角色（1是0否）',
  `enable` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否启用 1是0否',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of org_role
-- ----------------------------
INSERT INTO `org_role` VALUES ('1', '管理员', '9', '应用管理员，可以操作所有业务相关功能', '0', '1', '2019-04-15 14:02:13', null, '2020-03-17 10:15:22', 'ad313d38fe9447ce863fe8584743a010', '0');
INSERT INTO `org_role` VALUES ('G96rCbCnoN4tgI3E0FQXHQ', '个人用户', '1', '只能操作个人相关业务', '0', '1', '2020-03-17 10:14:29', 'ad313d38fe9447ce863fe8584743a010', '2020-03-17 10:14:29', 'ad313d38fe9447ce863fe8584743a010', '0');
INSERT INTO `org_role` VALUES ('wKlw-zXI_VFRbbwsltWM4Q', '机构管理员', '2', '可以操作所属单位相关数据及业务', '0', '1', '2020-03-17 10:15:55', 'ad313d38fe9447ce863fe8584743a010', '2020-03-17 10:15:55', 'ad313d38fe9447ce863fe8584743a010', '0');

-- ----------------------------
-- Table structure for `org_role_permission`
-- ----------------------------
DROP TABLE IF EXISTS `org_role_permission`;
CREATE TABLE `org_role_permission` (
  `role_id` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '角色ID',
  `permission_id` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of org_role_permission
-- ----------------------------
INSERT INTO `org_role_permission` VALUES ('1', '1000');
INSERT INTO `org_role_permission` VALUES ('1', '1010');
INSERT INTO `org_role_permission` VALUES ('1', '1011');
INSERT INTO `org_role_permission` VALUES ('1', '1012');
INSERT INTO `org_role_permission` VALUES ('1', '1013');
INSERT INTO `org_role_permission` VALUES ('1', '1014');
INSERT INTO `org_role_permission` VALUES ('1', '1110');
INSERT INTO `org_role_permission` VALUES ('1', '2000');
INSERT INTO `org_role_permission` VALUES ('1', '2010');
INSERT INTO `org_role_permission` VALUES ('1', '2011');
INSERT INTO `org_role_permission` VALUES ('1', '2013');
INSERT INTO `org_role_permission` VALUES ('1', '2014');
INSERT INTO `org_role_permission` VALUES ('1', '2015');
INSERT INTO `org_role_permission` VALUES ('1', '2016');
INSERT INTO `org_role_permission` VALUES ('1', '2020');
INSERT INTO `org_role_permission` VALUES ('1', '2021');
INSERT INTO `org_role_permission` VALUES ('1', '2023');
INSERT INTO `org_role_permission` VALUES ('1', '2024');
INSERT INTO `org_role_permission` VALUES ('1', '2025');
INSERT INTO `org_role_permission` VALUES ('G96rCbCnoN4tgI3E0FQXHQ', '2011');
INSERT INTO `org_role_permission` VALUES ('wKlw-zXI_VFRbbwsltWM4Q', '2000');
INSERT INTO `org_role_permission` VALUES ('wKlw-zXI_VFRbbwsltWM4Q', '2010');
INSERT INTO `org_role_permission` VALUES ('wKlw-zXI_VFRbbwsltWM4Q', '2011');
INSERT INTO `org_role_permission` VALUES ('wKlw-zXI_VFRbbwsltWM4Q', '2013');
INSERT INTO `org_role_permission` VALUES ('wKlw-zXI_VFRbbwsltWM4Q', '2014');
INSERT INTO `org_role_permission` VALUES ('wKlw-zXI_VFRbbwsltWM4Q', '2015');
INSERT INTO `org_role_permission` VALUES ('wKlw-zXI_VFRbbwsltWM4Q', '2016');
INSERT INTO `org_role_permission` VALUES ('wKlw-zXI_VFRbbwsltWM4Q', '2020');
INSERT INTO `org_role_permission` VALUES ('wKlw-zXI_VFRbbwsltWM4Q', '2021');
INSERT INTO `org_role_permission` VALUES ('wKlw-zXI_VFRbbwsltWM4Q', '2023');
INSERT INTO `org_role_permission` VALUES ('wKlw-zXI_VFRbbwsltWM4Q', '2024');
INSERT INTO `org_role_permission` VALUES ('wKlw-zXI_VFRbbwsltWM4Q', '2025');

-- ----------------------------
-- Table structure for `org_unit`
-- ----------------------------
DROP TABLE IF EXISTS `org_unit`;
CREATE TABLE `org_unit` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(120) NOT NULL COMMENT '单位名称',
  `type` tinyint(4) NOT NULL COMMENT '单位类型',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '上级单位',
  `contact` varchar(50) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `order_no` int(11) NOT NULL DEFAULT '1' COMMENT '排序号',
  `create_time` datetime NOT NULL,
  `create_by` varchar(32) NOT NULL,
  `update_time` datetime NOT NULL,
  `update_by` varchar(32) NOT NULL,
  `deleted` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of org_unit
-- ----------------------------
INSERT INTO `org_unit` VALUES ('1fT4-742peLu6zcRerRUUQ', '第一人民医院', '1', '', '张文', '13584950000', '1', '2020-03-09 10:34:47', 'ad313d38fe9447ce863fe8584743a010', '2020-03-09 10:34:47', 'ad313d38fe9447ce863fe8584743a010', '0');
INSERT INTO `org_unit` VALUES ('6kf6Ds_Wp5wIVM_BFgoUKw', '外科', '2', '1fT4-742peLu6zcRerRUUQ', '刘医生', '13584950000', '10', '2020-03-09 10:52:51', 'ad313d38fe9447ce863fe8584743a010', '2020-03-09 11:12:34', 'ad313d38fe9447ce863fe8584743a010', '0');
INSERT INTO `org_unit` VALUES ('7gGneX0jb792kBFCPcwPag', '疾控科', '2', 'kBu6Ud4RES25xcHAIxeCJQ', '武达', '13584950000', '1', '2020-03-09 11:14:19', 'ad313d38fe9447ce863fe8584743a010', '2020-03-09 11:14:19', 'ad313d38fe9447ce863fe8584743a010', '0');
INSERT INTO `org_unit` VALUES ('cY2VL408mS_e6ua5tppHpA', '妇科', '2', '1fT4-742peLu6zcRerRUUQ', '吴医生', '13584950000', '2', '2020-03-09 10:53:25', 'ad313d38fe9447ce863fe8584743a010', '2020-03-09 10:53:25', 'ad313d38fe9447ce863fe8584743a010', '0');
INSERT INTO `org_unit` VALUES ('kBu6Ud4RES25xcHAIxeCJQ', '卫健委', '1', '', '刘仁', '13584950000', '0', '2020-03-09 11:13:44', 'ad313d38fe9447ce863fe8584743a010', '2020-03-09 11:13:44', 'ad313d38fe9447ce863fe8584743a010', '0');
INSERT INTO `org_unit` VALUES ('tS6TrjSyX3jKZ2zf_7rIKw', '第二人民医院', '1', '', '张三', '13584950000', '3', '2020-03-19 11:32:18', 'BrocI_IUOi8uvcVesEWEJQ', '2020-03-19 11:32:18', 'BrocI_IUOi8uvcVesEWEJQ', '0');

-- ----------------------------
-- Table structure for `sys_attachment`
-- ----------------------------
DROP TABLE IF EXISTS `sys_attachment`;
CREATE TABLE `sys_attachment` (
  `id` varchar(32) NOT NULL,
  `type` tinyint(4) NOT NULL,
  `name` varchar(100) NOT NULL,
  `suffix` varchar(10) DEFAULT NULL,
  `size` int(11) unsigned DEFAULT NULL,
  `relative_path` varchar(200) NOT NULL,
  `thumbnail_relative_path` varchar(200) DEFAULT NULL COMMENT '缩略图相对地址',
  `create_time` datetime NOT NULL,
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of sys_attachment
-- ----------------------------
INSERT INTO `sys_attachment` VALUES ('3c3c24303ff2498f8056eb87e5679aee', '1', '老年人健身', '.jpg', '155024', '20200317/3c3c24303ff2498f8056eb87e5679aee.jpg', null, '2020-03-17 09:11:25', '0');
INSERT INTO `sys_attachment` VALUES ('5d1ef80e8b7345d8956d1660370dc21d', '1', '小柯基', '.jpeg', '205816', '20200319/5d1ef80e8b7345d8956d1660370dc21d.jpeg', null, '2020-03-19 15:36:50', '0');
INSERT INTO `sys_attachment` VALUES ('ba0a0a245b3f4ec6b8d8d951b8ac92f5', '1', '小柯基', '.jpeg', '205816', '20200319/ba0a0a245b3f4ec6b8d8d951b8ac92f5.jpeg', null, '2020-03-19 15:55:32', '0');
INSERT INTO `sys_attachment` VALUES ('ba7f3df9474d4983b0386e2b92a239f7', '1', '初日', '.jpg', '249661', '20200319/ba7f3df9474d4983b0386e2b92a239f7.jpg', null, '2020-03-19 15:55:51', '0');
INSERT INTO `sys_attachment` VALUES ('c2ea86c1b58242b29b927cfbd8cc3cd6', '1', '刚出生的柯基', '.jpg', '135791', '20200317/c2ea86c1b58242b29b927cfbd8cc3cd6.jpg', null, '2020-03-17 10:54:52', '0');
INSERT INTO `sys_attachment` VALUES ('daf5b54e172e4401aaf4a6fc499fd297', '1', '年轻人不要熬夜', '.jpg', '403023', '20200317/daf5b54e172e4401aaf4a6fc499fd297.jpg', null, '2020-03-17 09:11:25', '0');
INSERT INTO `sys_attachment` VALUES ('fb5be8fabe534018ae9b8ef16c20a615', '1', '英女皇与她的狗', '.jpeg', '69807', '20200317/fb5be8fabe534018ae9b8ef16c20a615.jpeg', null, '2020-03-17 09:11:22', '0');

-- ----------------------------
-- Table structure for `sys_constant`
-- ----------------------------
DROP TABLE IF EXISTS `sys_constant`;
CREATE TABLE `sys_constant` (
  `type` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '分类',
  `code` varchar(11) COLLATE utf8_unicode_ci NOT NULL COMMENT '编码ID',
  `name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '名称',
  `order_no` int(11) DEFAULT NULL COMMENT '排序号',
  PRIMARY KEY (`type`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of sys_constant
-- ----------------------------
INSERT INTO `sys_constant` VALUES ('boolean-type', '0', '否', '2');
INSERT INTO `sys_constant` VALUES ('boolean-type', '1', '是', '1');
INSERT INTO `sys_constant` VALUES ('identification-type', '1', '身份证', '1');
INSERT INTO `sys_constant` VALUES ('identification-type', '2', '居民户口簿', '2');
INSERT INTO `sys_constant` VALUES ('identification-type', '3', '护照', '3');
INSERT INTO `sys_constant` VALUES ('identification-type', '4', '军官证', '4');
INSERT INTO `sys_constant` VALUES ('identification-type', '5', '驾驶执照', '5');
INSERT INTO `sys_constant` VALUES ('identification-type', '6', '港澳居民来往内地通行证', '6');
INSERT INTO `sys_constant` VALUES ('identification-type', '7', '台湾居民来往内地通行证', '7');
INSERT INTO `sys_constant` VALUES ('identification-type', '8', '其他', '8');
INSERT INTO `sys_constant` VALUES ('nation-type', '1', '汉族', '1');
INSERT INTO `sys_constant` VALUES ('nation-type', '10', '朝鲜族', '10');
INSERT INTO `sys_constant` VALUES ('nation-type', '11', '满族', '11');
INSERT INTO `sys_constant` VALUES ('nation-type', '12', '侗族', '12');
INSERT INTO `sys_constant` VALUES ('nation-type', '13', '瑶族', '13');
INSERT INTO `sys_constant` VALUES ('nation-type', '14', '白族', '14');
INSERT INTO `sys_constant` VALUES ('nation-type', '15', '土家族', '15');
INSERT INTO `sys_constant` VALUES ('nation-type', '16', '哈尼族', '16');
INSERT INTO `sys_constant` VALUES ('nation-type', '17', '哈萨克族', '17');
INSERT INTO `sys_constant` VALUES ('nation-type', '18', '傣族', '18');
INSERT INTO `sys_constant` VALUES ('nation-type', '19', '黎族', '19');
INSERT INTO `sys_constant` VALUES ('nation-type', '2', '蒙古族', '2');
INSERT INTO `sys_constant` VALUES ('nation-type', '20', '僳僳族', '20');
INSERT INTO `sys_constant` VALUES ('nation-type', '21', '佤族', '21');
INSERT INTO `sys_constant` VALUES ('nation-type', '22', '畲族', '22');
INSERT INTO `sys_constant` VALUES ('nation-type', '23', '高山族', '23');
INSERT INTO `sys_constant` VALUES ('nation-type', '24', '拉祜族', '24');
INSERT INTO `sys_constant` VALUES ('nation-type', '25', '水族', '25');
INSERT INTO `sys_constant` VALUES ('nation-type', '26', '东乡族', '26');
INSERT INTO `sys_constant` VALUES ('nation-type', '27', '纳西族', '27');
INSERT INTO `sys_constant` VALUES ('nation-type', '28', '景颇族', '28');
INSERT INTO `sys_constant` VALUES ('nation-type', '29', '柯尔克孜族', '29');
INSERT INTO `sys_constant` VALUES ('nation-type', '3', '回族', '3');
INSERT INTO `sys_constant` VALUES ('nation-type', '30', '土族', '30');
INSERT INTO `sys_constant` VALUES ('nation-type', '31', '达斡尔族', '31');
INSERT INTO `sys_constant` VALUES ('nation-type', '32', '仫佬族', '32');
INSERT INTO `sys_constant` VALUES ('nation-type', '33', '羌族', '33');
INSERT INTO `sys_constant` VALUES ('nation-type', '34', '布朗族', '34');
INSERT INTO `sys_constant` VALUES ('nation-type', '35', '撤拉族', '35');
INSERT INTO `sys_constant` VALUES ('nation-type', '36', '毛南族', '36');
INSERT INTO `sys_constant` VALUES ('nation-type', '37', '仡佬族', '37');
INSERT INTO `sys_constant` VALUES ('nation-type', '38', '锡伯族', '38');
INSERT INTO `sys_constant` VALUES ('nation-type', '39', '阿昌族', '39');
INSERT INTO `sys_constant` VALUES ('nation-type', '4', '藏族', '4');
INSERT INTO `sys_constant` VALUES ('nation-type', '40', '普米族', '40');
INSERT INTO `sys_constant` VALUES ('nation-type', '41', '塔吉克族', '41');
INSERT INTO `sys_constant` VALUES ('nation-type', '42', '怒族', '42');
INSERT INTO `sys_constant` VALUES ('nation-type', '43', '乌孜别克族', '43');
INSERT INTO `sys_constant` VALUES ('nation-type', '44', '俄罗斯族', '44');
INSERT INTO `sys_constant` VALUES ('nation-type', '45', '鄂温克族', '45');
INSERT INTO `sys_constant` VALUES ('nation-type', '46', '崩龙族', '46');
INSERT INTO `sys_constant` VALUES ('nation-type', '47', '保安族', '47');
INSERT INTO `sys_constant` VALUES ('nation-type', '48', '裕固族', '48');
INSERT INTO `sys_constant` VALUES ('nation-type', '49', '京族', '49');
INSERT INTO `sys_constant` VALUES ('nation-type', '5', '维吾尔族', '5');
INSERT INTO `sys_constant` VALUES ('nation-type', '50', '塔塔尔族', '50');
INSERT INTO `sys_constant` VALUES ('nation-type', '51', '独龙族', '51');
INSERT INTO `sys_constant` VALUES ('nation-type', '52', '鄂伦春族', '52');
INSERT INTO `sys_constant` VALUES ('nation-type', '53', '赫哲族', '53');
INSERT INTO `sys_constant` VALUES ('nation-type', '54', '门巴族', '54');
INSERT INTO `sys_constant` VALUES ('nation-type', '55', '珞巴族', '55');
INSERT INTO `sys_constant` VALUES ('nation-type', '56', '基诺族', '56');
INSERT INTO `sys_constant` VALUES ('nation-type', '57', '外籍', '57');
INSERT INTO `sys_constant` VALUES ('nation-type', '58', '不详', '58');
INSERT INTO `sys_constant` VALUES ('nation-type', '6', '苗族', '6');
INSERT INTO `sys_constant` VALUES ('nation-type', '7', '彝族', '7');
INSERT INTO `sys_constant` VALUES ('nation-type', '8', '壮族', '8');
INSERT INTO `sys_constant` VALUES ('nation-type', '9', '布依族', '9');
INSERT INTO `sys_constant` VALUES ('notice-type', '0', '公告', '0');
INSERT INTO `sys_constant` VALUES ('notice-type', '1', '文件', '1');
INSERT INTO `sys_constant` VALUES ('role-level-type', '1', '个人', '1');
INSERT INTO `sys_constant` VALUES ('role-level-type', '2', '机构', '2');
INSERT INTO `sys_constant` VALUES ('role-level-type', '9', '管理员', '3');
INSERT INTO `sys_constant` VALUES ('sex-type', '0', '女', '2');
INSERT INTO `sys_constant` VALUES ('sex-type', '1', '男', '1');
INSERT INTO `sys_constant` VALUES ('unit-type', '1', '公司', '1');
INSERT INTO `sys_constant` VALUES ('unit-type', '2', '部门', '2');

-- ----------------------------
-- Table structure for `sys_container_version`
-- ----------------------------
DROP TABLE IF EXISTS `sys_container_version`;
CREATE TABLE `sys_container_version` (
  `id` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '容器ID',
  `version` int(11) NOT NULL COMMENT '版本',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of sys_container_version
-- ----------------------------
INSERT INTO `sys_container_version` VALUES ('constants_container', '2', '2019-07-25 10:54:02');
INSERT INTO `sys_container_version` VALUES ('data_source_container', '0', '2019-08-30 09:00:14');
INSERT INTO `sys_container_version` VALUES ('org_unit_container', '7', '2020-03-19 11:32:18');
INSERT INTO `sys_container_version` VALUES ('permission_container', '9', '2020-03-17 11:40:03');
INSERT INTO `sys_container_version` VALUES ('role_container', '22', '2020-03-19 11:42:53');
INSERT INTO `sys_container_version` VALUES ('unit_container', '1', null);

-- ----------------------------
-- Table structure for `sys_logger_login`
-- ----------------------------
DROP TABLE IF EXISTS `sys_logger_login`;
CREATE TABLE `sys_logger_login` (
  `id` varchar(32) CHARACTER SET utf8 NOT NULL,
  `ip` varchar(50) CHARACTER SET utf8 NOT NULL,
  `login_type` tinyint(4) NOT NULL COMMENT '登录方式',
  `account` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '登录账号',
  `user_id` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '用户ID',
  `user_type` tinyint(4) NOT NULL COMMENT '用户类型',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT 'id',
  `account` varchar(30) COLLATE utf8_unicode_ci NOT NULL COMMENT '账号',
  `password` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '密码',
  `salt` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '盐',
  `user_id` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户信息表ID',
  `is_first_login` tinyint(4) DEFAULT '1' COMMENT '是否第一次登录',
  `state` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否启用，0不启用',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否管理员',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('2qiIT_bFLUFStx8fCQFxIA', 'zhudan', '27b9681d1ff3fae11430e0e18d95d28b', '7e3bc62e32f2074e0be700f3d6df8149', 'BCjljSsd1bndH2wvMobB9w', '1', '1', '3', '2020-03-16 16:15:03', 'ad313d38fe9447ce863fe8584743a010', '2020-03-16 16:15:03', 'ad313d38fe9447ce863fe8584743a010', '0', null);
INSERT INTO `sys_user` VALUES ('ad313d38fe9447ce863fe8584743a010', 'admin', '48379ee570aa6772e692623d826846ed', 'd3b04396a75024974c5c97787c17547a', '', '1', '1', '1', '2019-03-22 09:59:44', null, '2020-03-18 17:10:29', '', '0', '2020-03-18 17:10:29');
INSERT INTO `sys_user` VALUES ('Hs0Lw4k2hIjy4vPFmkaQ5g', 'zhouxingxing', '439f8290319aa2174b82adcc2f836c66', '48d33e833a78e41d6bf791ae1ef23d9a', 'FrX7JyqZVB3W3u41wBtprw', '1', '1', '3', '2020-03-17 10:58:30', 'ad313d38fe9447ce863fe8584743a010', '2020-03-17 10:58:30', 'ad313d38fe9447ce863fe8584743a010', '0', null);
INSERT INTO `sys_user` VALUES ('J309ecE_G7NUajMDb8tvQg', 'zhouxuwu', 'e22c088055a5ada418f9a52be6676535', '3baf18b030ea37785a0a112a9f180c9d', 'BrocI_IUOi8uvcVesEWEJQ', '1', '1', '3', '2020-03-17 09:11:26', 'ad313d38fe9447ce863fe8584743a010', '2020-03-23 10:16:42', '', '0', '2020-03-23 10:16:42');
INSERT INTO `sys_user` VALUES ('KPG86iVnfC9BgkclJrQwog', 'zhangtianshuo', '3de12dfd672fdb29b5bf13c29f1fe559', '2e3e2610b5869649d0bf7ab16f7037bb', '7yqslBkAG-XCjFmtaH4-8Q', '1', '1', '3', '2020-03-17 10:56:27', 'ad313d38fe9447ce863fe8584743a010', '2020-03-17 10:56:27', 'ad313d38fe9447ce863fe8584743a010', '0', null);
INSERT INTO `sys_user` VALUES ('OkWSUDWa_thVbDL-eDYzYg', 'kewen001', '0c9e74c7ddbb116a48d0a99b5b5fb0e2', 'acaa8e79947162432c5470b16f70d64b', 'WX14wsMFPCucbgiDLCLtFg', '1', '1', '3', '2020-03-17 10:57:24', 'ad313d38fe9447ce863fe8584743a010', '2020-03-19 11:31:31', '', '0', '2020-03-19 11:31:31');
INSERT INTO `sys_user` VALUES ('qRZYwX0OwMzqvBliRecsVw', 'yangyang', '36d2cd433a38b266d1237056b3dcd49e', 'f52266837b88ce2ec40fc5c131c72af4', 'lHMmloreL_B1rXrDmul8Qw', '1', '1', '3', '2020-03-17 10:54:52', 'ad313d38fe9447ce863fe8584743a010', '2020-03-17 10:54:52', 'ad313d38fe9447ce863fe8584743a010', '0', null);
INSERT INTO `sys_user` VALUES ('WP1GMTcLAiJ_BdnCCc5ifg', 'liuwenwen', 'ce83acc8df713d5f2cc4b424c1dd6f6f', '2b3f0338da5de3273f833579596b3ca2', 's0JE93DyHcP_Sp5IhAPffQ', '1', '1', '3', '2020-03-17 10:55:38', 'ad313d38fe9447ce863fe8584743a010', '2020-03-17 10:55:38', 'ad313d38fe9447ce863fe8584743a010', '0', null);
INSERT INTO `sys_user` VALUES ('YLO0mxoBwCd8W5TJk2Zy6w', 'lisi001', '1b85c26f9f2f1ab3e6367f54175c69cb', '5ee440a018a7779b005d9e1ef5731577', 'zIpIlLzzpBaWYv2B6SgFCg', '1', '1', '3', '2020-03-19 11:33:11', 'BrocI_IUOi8uvcVesEWEJQ', '2020-03-19 11:33:11', 'BrocI_IUOi8uvcVesEWEJQ', '0', null);

-- ----------------------------
-- Table structure for `sys_visit_cache`
-- ----------------------------
DROP TABLE IF EXISTS `sys_visit_cache`;
CREATE TABLE `sys_visit_cache` (
  `id` varchar(32) NOT NULL,
  `ip` varchar(50) NOT NULL COMMENT 'IP',
  `code` varchar(20) NOT NULL COMMENT 'IP',
  `value` varchar(400) NOT NULL COMMENT '内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



