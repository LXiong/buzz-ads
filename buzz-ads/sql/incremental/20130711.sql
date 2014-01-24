ALTER TABLE `bfp_channel_daily_stat` ADD `PAGEVIEW` INT( 14 ) NOT NULL DEFAULT '0';
ALTER TABLE `stats_publisher_daily` ADD `PAGEVIEW` INT( 14 ) NOT NULL DEFAULT '0';

ALTER TABLE `publisher_contact_info`
ADD COLUMN `PROPORTION`  tinyint(3) NOT NULL DEFAULT 0 COMMENT '站长的分成比率  如果为0则通过配置信息来取得' AFTER `STATUS`;

ALTER TABLE `stats_ad_daily`
ADD COLUMN `UUID`  binary(16) NOT NULL COMMENT '站长uuid' AFTER `NETWORK`,
DROP INDEX `UNQ_ADENTRY_DAY` ,
ADD UNIQUE INDEX `UNQ_ADENTRY_DAY` (`ADENTRY_ID`, `DATE_DAY`, `NETWORK`, `UUID`) USING BTREE ;

-- 刷新用户分类
DELETE FROM `ad_order_category`;

insert into ad_order_category values(11,0,'文化艺术','Culture');
insert into ad_order_category values(1101,11,'网络小说','Online Literature');
insert into ad_order_category values(1102,11,'文化文学','Culture/Literature');
insert into ad_order_category values(1103,11,'书刊杂志','Books/Magazines');
insert into ad_order_category values(1104,11,'人文艺术','Humanities & Arts');
insert into ad_order_category values(12,0,'教育培训','Jobs & Education');
insert into ad_order_category values(1201,12,'初级中级教育','Primary & Secondary Schooling');
insert into ad_order_category values(1202,12,'留学移民','Overseas Education');
insert into ad_order_category values(1203,12,'MBA','MBA');
insert into ad_order_category values(1204,12,'职业技能培训','Vocational & Continuing Education');
insert into ad_order_category values(1205,12,'早教幼教','Early Childhood Education');
insert into ad_order_category values(1206,12,'语言培训','Language Education');
insert into ad_order_category values(1207,12,'大学','University');
insert into ad_order_category values(13,0,'游戏','Games');
insert into ad_order_category values(1301,13,'大型客户端游戏','Massive Multiplayer');
insert into ad_order_category values(1302,13,'网页游戏','Online Games');
insert into ad_order_category values(1303,13,'手机平板游戏','Mobile Phone/Tablet Games');
insert into ad_order_category values(1304,13,'小游戏Flash游戏','Mini Games & Flash Games');
insert into ad_order_category values(1305,13,'电子竞技','Sports Games');
insert into ad_order_category values(1306,13,'游戏周边','Games Related Products');
insert into ad_order_category values(14,0,'消费数码','Consumer Electronics');
insert into ad_order_category values(1401,14,'摄影摄像','Photography & Videography');
insert into ad_order_category values(1402,14,'电玩','Video Games');
insert into ad_order_category values(1403,14,'手机平板与应用','Mobile Phones/Tablets & Apps');
insert into ad_order_category values(1404,14,'硬件及网络','Hardware & Networking');
insert into ad_order_category values(1405,14,'数码配件','Home Appliances');
insert into ad_order_category values(15,0,'健康医疗','Health & Wellness');
insert into ad_order_category values(1501,15,'情感心理','Psychology');
insert into ad_order_category values(1502,15,'整形整容','Cosmetic Procedures');
insert into ad_order_category values(1503,15,'保健养生','Healthcare');
insert into ad_order_category values(1504,15,'医疗','Medical Treatment');
insert into ad_order_category values(16,0,'美容及化妆品','Beauty & Cosmetics');
insert into ad_order_category values(1601,16,'护肤','Skin Care');
insert into ad_order_category values(1602,16,'洗护日用','Daily Personal Care');
insert into ad_order_category values(1603,16,'彩妆','Make-Up/Cosmetics');
insert into ad_order_category values(1604,16,'美发美体','Hair & Body');
insert into ad_order_category values(1605,16,'品牌','Brands');
insert into ad_order_category values(17,0,'汽车','Automobiles');
insert into ad_order_category values(1701,17,'高档汽车','High-End Cars');
insert into ad_order_category values(1702,17,'中档汽车','Mid-End Cars');
insert into ad_order_category values(1703,17,'入门级汽车','Entry-Level Cars');
insert into ad_order_category values(1704,17,'养车用车','Vehicle Maintenance');
insert into ad_order_category values(1705,17,'二手车','Used Cars');
insert into ad_order_category values(1706,17,'汽车租赁','Rental Cars');
insert into ad_order_category values(1707,17,'汽车制造商','Car Manufacturers');
insert into ad_order_category values(1708,17,'汽车保险','Auto Insurance');
insert into ad_order_category values(1709,17,'汽车周边','Auto Accessories');
insert into ad_order_category values(1710,17,'豪华汽车','Luxury Cars');
insert into ad_order_category values(1711,17,'品牌','Brands');
insert into ad_order_category values(18,0,'运动健身','Sports & Fitness');
insert into ad_order_category values(1801,18,'运动','Sports');
insert into ad_order_category values(1802,18,'健身','Fitness');
insert into ad_order_category values(1803,18,'户外','Outdoor Sports');
insert into ad_order_category values(19,0,'IT及信息产业','Internet & Technology');
insert into ad_order_category values(1901,19,'电信、通信服务','Telecommunications/Services');
insert into ad_order_category values(1902,19,'软件','Software');
insert into ad_order_category values(1903,19,'电子商务','E-commerce');
insert into ad_order_category values(20,0,'母婴育儿','Baby & Maternity');
insert into ad_order_category values(2002,20,'母婴用品','Maternity & Parenting');
insert into ad_order_category values(2001,20,'亲子服务','Family Services');
insert into ad_order_category values(21,0,'金融财经','Personal Finance');
insert into ad_order_category values(2101,21,'保险','Insurance');
insert into ad_order_category values(2102,21,'投资理财','Investing');
insert into ad_order_category values(22,0,'咨询及服务','Consulting & Services');
insert into ad_order_category values(2201,22,'婚庆','Wedding Services');
insert into ad_order_category values(2202,22,'招聘求职','Careers');
insert into ad_order_category values(2203,22,'相亲交友','Dating');
insert into ad_order_category values(23,0,'工业','Industrial');
insert into ad_order_category values(24,0,'时尚','Female Fashion');
insert into ad_order_category values(2401,24,'服装','Clothing');
insert into ad_order_category values(2402,24,'首饰饰品','Jewelry/Accessories');
insert into ad_order_category values(2403,24,'奢侈品','Designers & Collections');
insert into ad_order_category values(2404,24,'鞋包','Shoes & Bags');
insert into ad_order_category values(25,0,'旅游商旅','Leisure Travel & Business Travel');
insert into ad_order_category values(2501,25,'酒店','Hotels & Accommodations');
insert into ad_order_category values(2502,25,'旅游','Leisure Travel');
insert into ad_order_category values(2503,25,'航空','Air Travel');
insert into ad_order_category values(2504,25,'旅行社','Travel Agencies & Services');
insert into ad_order_category values(26,0,'食品美食','Food & Drink');
insert into ad_order_category values(2601,26,'烹饪和菜谱','Cooking & Recipes');
insert into ad_order_category values(2602,26,'休闲食品','Snacks');
insert into ad_order_category values(2603,26,'餐馆美食','Restaurants');
insert into ad_order_category values(2604,26,'保健食品','Healthy Food');
insert into ad_order_category values(2605,26,'生鲜食品','Fresh Food');
insert into ad_order_category values(2606,26,'地方特产','Local Food');
insert into ad_order_category values(2607,26,'饮料','Drinks');
insert into ad_order_category values(27,0,'家居生活','Home & Lifestyle');
insert into ad_order_category values(2701,27,'家居家装','Furniture Decoration');
insert into ad_order_category values(2702,27,'家用电器','Domestic Appliance');
insert into ad_order_category values(2703,27,'日用百货','Daily Supplies');
insert into ad_order_category values(2704,27,'宠物','Pets');
insert into ad_order_category values(28,0,'房地产及建筑','Real Estate & Construction');
insert into ad_order_category values(2801,28,'开发商','Property Development');
insert into ad_order_category values(2802,28,'中介代理','Real Estate Agencies');
insert into ad_order_category values(2803,28,'物业管理','Property Management');
insert into ad_order_category values(2804,28,'楼盘','Real Estate');
insert into ad_order_category values(29,0,'电脑办公','Office Equipment');
insert into ad_order_category values(2901,29,'电脑及外设','Computers and Related Products');
insert into ad_order_category values(2902,29,'办公文具','Office Supplies');
insert into ad_order_category values(30,0,'科学','Science');
insert into ad_order_category values(31,0,'酒类','Alcohol');
insert into ad_order_category values(3101,31,'洋酒','Liquor & Spirits');
insert into ad_order_category values(3102,31,'红酒、白葡萄酒、香槟','Wine/Beer/Champagne');
insert into ad_order_category values(3103,31,'国酒','Chinese Liquor/Spirits');
insert into ad_order_category values(32,0,'时政','Politics');
insert into ad_order_category values(33,0,'娱乐','Arts & Entertainment');
insert into ad_order_category values(3301,33,'娱乐综艺','Variety & Entertainment');
insert into ad_order_category values(3302,33,'院线电影','Movies');
insert into ad_order_category values(3303,33,'网络影视剧','TV Dramas');
insert into ad_order_category values(3304,33,'动画漫画','Comics & Animation');
insert into ad_order_category values(3305,33,'高雅音乐','Classical Music & Jazz');
insert into ad_order_category values(3306,33,'流行音乐','Pop Music');
insert into ad_order_category values(3307,33,'音响器材','Audio Equipment');
insert into ad_order_category values(3308,33,'乐器','Musical Instruments');
insert into ad_order_category values(34,0,'宗教和灵媒','Religion and Psychics');
insert into ad_order_category values(35,0,'交通','Traffic');
insert into ad_order_category values(36,0,'商业','Commerce');

--  ADX DSP RTB 相关信息
CREATE TABLE `adx_dsp_rtb_info` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `kEY` int(11) NOT NULL DEFAULT '0' COMMENT '用户在系统获取到的key(目前为advertiserId)',
  `WINNERNOTIFYURL` varchar(50) NOT NULL COMMENT '接收bidResponse最终结果的地址',
  `BIDREQUESTURL` varchar(50) NOT NULL COMMENT '接收bidRequest的地址',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UNQ_KEY` (`kEY`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Adx RTB Info';

ALTER TABLE `stats_admin_daily`
ADD COLUMN `CPC_ORIGINAL_CLICK_NO`  int(11) NOT NULL DEFAULT 0 COMMENT 'CPC原始点击' AFTER `CPC_VIEW_NO`,
ADD COLUMN `CPS_VIEW_NO`  int(11) NOT NULL DEFAULT 0 COMMENT 'CPS展示数' AFTER `CPC_TOTAL_COMMISSION`,
ADD COLUMN `CPS_CLICK_NO`  int(11) NOT NULL DEFAULT 0 COMMENT 'CPS点击数' AFTER `CPS_VIEW_NO`;

