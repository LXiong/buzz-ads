DROP table `buzzads`.`ad_order_category`;

CREATE TABLE IF NOT EXISTS `ad_order_category` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PARENT_ID` int(11) NOT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `NAME_EN` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `PARENT_ID_IDX` (`PARENT_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

INSERT INTO `buzzads`.`ad_order_category` VALUES
('11', '0', '文化娱乐', 'Arts & Entertainment'),
('12', '0', '教育培训', 'Jobs & Education'),
('13', '0', '游戏', 'Games'),
('14', '0', '消费数码', 'Consumer Electronics'),
('15', '0', '健康医疗', 'Health & Wellness'),
('16', '0', '美容及化妆品', 'Beauty & Cosmetics'),
('17', '0', '汽车', 'Automobiles'),
('18', '0', '运动健身', 'Sports & Fitness'),
('19', '0', 'IT及信息产业', 'Internet & Technology'),
('20', '0', '母婴育儿', 'Baby & Maternity'),
('21', '0', '金融财经', 'Personal Finance'),
('22', '0', '咨询及服务', 'Consulting & Services'),
('23', '0', '工业', 'Business & Industrial'),
('24', '0', '女性时尚', 'Female Fashion'),
('25', '0', '旅游商旅', 'Leisure Travel & Business Travel'),
('26', '0', '食品美食', 'Food & Drink'),
('27', '0', '家居生活', 'Home & Lifestyle'),
('28', '0', '房地产及建筑', 'Real Estate & Construction'),
('29', '0', '办公用品', 'Office Equipment'),
('30', '0', '农业', 'Agriculture'),
('31', '0', '烟酒', 'Tobacco & Alcohol'),
('32', '0', '时政与财经', 'Politics & Finance'),
('1101', '11', '娱乐综艺', 'Variety & Entertainment'),
('1102', '11', '院线电影', 'Movies'),
('1103', '11', '网络影视剧', 'TV Dramas'),
('1104', '11', '动画漫画', 'Comics & Animation'),
('1105', '11', '网络小说', 'Online Literature'),
('1106', '11', '文化文学', 'Culture/Literature'),
('1107', '11', '书刊杂志', 'Books/Magazines'),
('1108', '11', '高雅音乐', 'Classical Music & Jazz'),
('1109', '11', '流行音乐', 'Pop Music'),
('1110', '11', '音响器材', 'Audio Equipment'),
('1111', '11', '乐器', 'Musical Instruments'),
('1112', '11', '人文艺术', 'Humanities & Arts'),
('1201', '12', '初级中级教育', 'Primary & Secondary Schooling'),
('1202', '12', '学历与留学', 'Overseas Education'),
('1203', '12', 'MBA', 'MBA'),
('1204', '12', '职业技能培训', 'Vocational & Continuing Education'),
('1301', '13', '大型客户端游戏', 'Massive Multiplayer'),
('1302', '13', '网页游戏', 'Online Games'),
('1303', '13', '手机平板游戏', 'Mobile Phone/Tablet Games'),
('1304', '13', '小游戏Flash游戏', 'Mini Games & Flash Games'),
('1305', '13', '电子竞技', 'Sports Games'),
('1401', '14', '摄影摄像', 'Photography & Videography'),
('1402', '14', '电玩', 'Video Games'),
('1403', '14', '手机平板与应用', 'Mobile Phones/Tablets & Apps'),
('1404', '14', '硬件及网络', 'Hardware & Networking'),
('1405', '14', '家电', 'Home Appliances'),
('1501', '15', '情感心理', 'Psychology'),
('1502', '15', '整形整容', 'Cosmetic Procedures'),
('1503', '15', '保健养生', 'Healthcare'),
('1504', '15', '医疗', 'Medical Treatment'),
('1601', '16', '护肤', 'Skin Care'),
('1602', '16', '洗护日用', 'Daily Personal Care'),
('1603', '16', '彩妆', 'Make-Up/Cosmetics'),
('1701', '17', '高档汽车', 'Luxury Cars'),
('1702', '17', '中档汽车', 'Mid-End Cars'),
('1703', '17', '入门级汽车', 'Entry-Level Cars'),
('1704', '17', '养车用车', 'Vehicle Maintenance'),
('1705', '17', '二手车', 'Used Cars'),
('1706', '17', '汽车租赁', 'Rental Cars'),
('1707', '17', '汽车制造商', 'Car Manufacturers'),
('1708', '17', '汽车保险', 'Auto Insurance'),
('1709', '17', '汽车周边', 'Auto Accessories'),
('1801', '18', '运动', 'Sports'),
('1802', '18', '健身', 'Fitness'),
('1803', '18', '户外', 'Outdoor Sports'),
('1901', '19', '电信、通信服务', 'Telecommunications/Services'),
('1902', '19', '软件', 'Software'),
('2001', '20', '婴幼', 'Baby'),
('2002', '20', '母婴用品', 'Maternity & Parenting'),
('2003', '20', '童书阅读', 'Baby Books'),
('2101', '21', '保险', 'Insurance'),
('2102', '21', '投资理财', 'Investing'),
('2201', '22', '婚庆', 'Wedding Services'),
('2202', '22', '健身娱乐', 'Health & Fitness Services'),
('2401', '24', '服装', 'Clothing'),
('2402', '24', '首饰饰品', 'Jewelry/Accessories'),
('2403', '24', '奢侈品', 'Designers & Collections'),
('2404', '24', '鞋包', 'Shoes & Bags'),
('2405', '24', '化妆造型', 'Make-Up & Cosmetics'),
('2501', '25', '酒店', 'Hotels & Accommodations'),
('2502', '25', '旅游', 'Leisure Travel'),
('2503', '25', '航空', 'Air Travel'),
('2504', '25', '旅行社', 'Travel Agencies & Services'),
('2601', '26', '烹饪和菜谱', 'Cooking & Recipes'),
('2602', '26', '休闲食品', 'Snacks'),
('2603', '26', '餐馆美食', 'Restaurants'),
('2801', '28', '开发商', 'Property Development'),
('2802', '28', '中介代理', 'Real Estate Agencies'),
('2803', '28', '物业管理', 'Property Management'),
('2804', '28', '楼盘', 'Real Estate'),
('3101', '31', '洋酒', 'Liquor & Spirits'),
('3102', '31', '红酒、白葡萄酒、香槟', 'Wine/Beer/Champagne'),
('3103', '31', '国酒', 'Chinese Liquor/Spirits');


ALTER TABLE  `buzzads`.`ad_entry` ADD  `DESTINATION` text DEFAULT NULL COMMENT  '广告最终的实际链接' AFTER `DESCRIPTION`;
ALTER TABLE  `buzzads`.`ad_order` ADD  `AUDIENCECATEGORIES` text DEFAULT NULL COMMENT  '用户目标类型' AFTER  `KEYWORDS`;

CREATE TABLE IF NOT EXISTS `publisher_site_config` (
  `UUID` binary(16) NOT NULL,
  `BLACK_KEYWORDS` varchar(500) DEFAULT NULL COMMENT '关键词过滤',
   PRIMARY KEY (`UUID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `publisher_black_domain` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `UUID` binary(16) NOT NULL,
  `TYPE` tinyint(4) NOT NULL DEFAULT '0',
  `URL` text NOT NULL COMMENT '广告链接域名过滤',
   PRIMARY KEY(`ID`),
   KEY `UUID_IDX` (`UUID`)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8;


ALTER TABLE `stats_publisher_daily`
ADD COLUMN `CPM_VIEW_NO`  int(11) NOT NULL DEFAULT 0 COMMENT 'CPM展示' AFTER `CPS_TOTAL_PRICE`,
ADD COLUMN `CPM_PUB_COMMISSION`  decimal(16,5) NOT NULL DEFAULT 0 COMMENT 'CPM站长佣金，即我们发放给站长的佣金' AFTER `CPM_VIEW_NO`,
ADD COLUMN `CPM_TOTAL_COMMISSION`  decimal(16,5) NOT NULL DEFAULT 0 COMMENT 'CPM佣金总额，即我们得到的佣金' AFTER `CPM_PUB_COMMISSION`;

ALTER TABLE `stats_admin_daily`
ADD COLUMN `CPM_VIEW_NO`  int(11) NOT NULL DEFAULT 0 COMMENT 'CPM展示' AFTER `CPS_TOTAL_PRICE`,
ADD COLUMN `CPM_PUB_COMMISSION`  decimal(16,5) NOT NULL DEFAULT 0 COMMENT 'CPM站长佣金，即我们发放给站长的佣金' AFTER `CPM_VIEW_NO`,
ADD COLUMN `CPM_TOTAL_COMMISSION`  decimal(16,5) NOT NULL DEFAULT 0 COMMENT 'CPM佣金总额，即我们得到的佣金' AFTER `CPM_PUB_COMMISSION`;

ALTER TABLE `stats_campaign_daily`
ADD COLUMN `CPM_VIEW_NO`  int(11) NOT NULL DEFAULT 0 COMMENT 'CPM展示' AFTER `CPS_TOTAL_PRICE`,
ADD COLUMN `CPM_PUB_COMMISSION`  decimal(16,5) NOT NULL DEFAULT 0 COMMENT 'CPM站长佣金，即我们发放给站长的佣金' AFTER `CPM_VIEW_NO`,
ADD COLUMN `CPM_TOTAL_COMMISSION`  decimal(16,5) NOT NULL DEFAULT 0 COMMENT 'CPM佣金总额，即我们得到的佣金' AFTER `CPM_PUB_COMMISSION`;

ALTER TABLE `publisher_settlement`
ADD COLUMN `CPM__NO`  int(11) NOT NULL DEFAULT 0 COMMENT 'CPM展示' AFTER `CPC_COMMISSION`,
ADD COLUMN `CPM_COMMISSION`  decimal(16,5) NOT NULL DEFAULT 0 COMMENT 'CPM站长佣金，即我们发放给站长的佣金' AFTER `CPM__NO`;

ALTER TABLE  `advertiser_billing` ADD  `BILLING_DAY` DATE NOT NULL COMMENT  '账单日期' AFTER  `TYPE`;


ALTER TABLE  `stats_admin_daily` ADD  `CPC_VIEW_NO` INT NOT NULL DEFAULT  '0' COMMENT 'CPC广告的展示数' AFTER  `CLICKS` ;
ALTER TABLE  `stats_admin_daily` ADD  `CPM_CLICK_NO` INT NOT NULL DEFAULT  '0' COMMENT 'CPM广告的点击数' AFTER  `CPM_VIEW_NO` ;
ALTER TABLE  `stats_campaign_daily` ADD  `CPC_VIEW_NO` INT NOT NULL DEFAULT  '0' COMMENT 'CPC广告的展示数' AFTER  `CLICKS` ;
ALTER TABLE  `stats_campaign_daily` ADD  `CPM_CLICK_NO` INT NOT NULL DEFAULT  '0' COMMENT 'CPM广告的点击数' AFTER  `CPM_VIEW_NO` ;

ALTER TABLE  `ad_order` CHANGE  `AUDIENCECATEGORIES`  `AUDIENCE_CAT` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户目标类分类';