--
-- Database: `buzzads`
--

-- --------------------------------------------------------

--
-- Table structure for table `ad_detail_cps`
--

CREATE TABLE IF NOT EXISTS `ad_detail_cps` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `UUID` binary(16) NOT NULL COMMENT '站长UUID',
  `ADORDER_ID` bigint(20) NOT NULL COMMENT '广告订单ID',
  `ADENTRY_ID` bigint(20) NOT NULL COMMENT '广告ID',
  `SOURCE` tinyint(4) NOT NULL COMMENT '来源：0-Buzz，1-Chanet',
  `PRODUCT` tinyint(4) NOT NULL COMMENT '来源：0-Lezhi，1-bShare',
  `CPS_OID` varchar(50) NOT NULL COMMENT 'CPS订单号',
  `STATUS` int(11) NOT NULL COMMENT 'CPS状态，0-未确认，1-已确认',
  `PUB_COMMISSION` bigint(20) NOT NULL COMMENT 'CPS站长佣金，即我们发放给站长的佣金',
  `COMMISSION` bigint(20) NOT NULL COMMENT 'CPS佣金总额，即我们得到的佣金',
  `TOTAL_PRICE` int(8) NOT NULL COMMENT 'CPS订单总额',
  `TRADE_TIME` datetime NOT NULL COMMENT 'CPS交易时间',
  `CONFIRM_TIME` datetime DEFAULT NULL COMMENT 'CPS确认时间',
  PRIMARY KEY (`ID`),
  KEY `IDX_UUID` (`UUID`),
  KEY `IDX_AD` (`ADORDER_ID`,`ADENTRY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='CPS佣金明细表';

-- --------------------------------------------------------

--
-- Table structure for table `ad_entry`
--

CREATE TABLE IF NOT EXISTS `ad_entry` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ORDER_ID` bigint(20) NOT NULL COMMENT '广告订单ID',
  `LINK` text NOT NULL COMMENT '广告链接',
  `KEYWORDS` varchar(500) NOT NULL COMMENT '广告的关键词',
  `RESOURCE_TYPE` tinyint(4) NOT NULL COMMENT '广告资源的类型。0-TEXT，1-IMAGE',
  `RESOURCE_URL` text NOT NULL COMMENT '广告资源的URL，例如图片URL',
  `TITLE` varchar(500) NOT NULL COMMENT '广告标题',
  `UPDATE_AT` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='广告表';

-- --------------------------------------------------------

--
-- Table structure for table `ad_order`
--

CREATE TABLE IF NOT EXISTS `ad_order` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `NAME` varchar(100) NOT NULL COMMENT '订单名字',
  `BILLING_TYPE` tinyint(4) NOT NULL COMMENT '支付类型：0-CPM，1-CPA，2-CPS，3-CPC',
  `STATUS` tinyint(4) NOT NULL COMMENT '状态：0-Ready，1-Live，2-Pause，3-Completed，4-Terminated',
  `SOURCE` tinyint(4) NOT NULL COMMENT '来源：0-Buzz，1-Chanet',
  `UPDATE_AT` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='广告订单表';

-- --------------------------------------------------------

--
-- Table structure for table `chanet_campaign`
--

CREATE TABLE IF NOT EXISTS `chanet_campaign` (
  `ORDER_ID` bigint(20) NOT NULL COMMENT '订单ID',
  `ADVERTISER` varchar(100) NOT NULL COMMENT '广告商',
  `CAMPAIGN_ID` bigint(20) NOT NULL COMMENT 'Chanet活动ID',
  `CAMPAIGN_NAME` varchar(100) NOT NULL COMMENT 'Chanet活动名',
  `CAMPAIGN_DOMAIN` varchar(100) NOT NULL COMMENT 'Chanet活动主域名',
  `SITE_ID` varchar(100) NOT NULL COMMENT 'Chanet网站ID',
  `SITE_NAME` varchar(100) NOT NULL COMMENT 'Chanet网站名',
  `ADVERTISER_LINK` text NOT NULL COMMENT 'Chanet广告目标链接',
  `CHANET_LINK` text NOT NULL COMMENT 'Chanet直链',
  `CAMPAIGN_RULES` text NOT NULL COMMENT 'Chanet活动规则XML',
  `STATUS` tinyint(4) NOT NULL COMMENT '状态，只会有1-Live和4-Terminated',
  PRIMARY KEY (`ORDER_ID`),
  UNIQUE KEY `UNQ_CAMPAIGN_ID` (`CAMPAIGN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='记录来自Chanet的广告订单信息';

-- --------------------------------------------------------

--
-- Table structure for table `chanet_data`
--

CREATE TABLE IF NOT EXISTS `chanet_data` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `OCD` varchar(50) NOT NULL COMMENT '订单号',
  `DATETIME` datetime NOT NULL COMMENT '交易发生的日期和时间',
  `BILLING_TYPE` tinyint(4) NOT NULL COMMENT '支付类型么，目前只有2-CPS',
  `CAMPAIGN_ID` bigint(20) NOT NULL COMMENT '活动ID',
  `CAMPAIGN_NAME` varchar(100) NOT NULL COMMENT '活动名称',
  `EXTINFO` varchar(100) NOT NULL COMMENT '下线信息，即广告链接里的EID',
  `USERINFO` varchar(100) NOT NULL COMMENT '用户信息，即广告链接里的UID信息',
  `COMMISSION` bigint(20) NOT NULL COMMENT '佣金',
  `TOTAL_PRICE` bigint(20) NOT NULL COMMENT '订单总价',
  `STATUS` tinyint(4) NOT NULL COMMENT '订单状态。0-成功下单，2-已发货，3-已签收，4-已退货，6-已完成，9-部分退换货，99-无订单状态',
  `PAID` tinyint(4) NOT NULL COMMENT '支付状态。0-未支付，1-已支付，2-未知，99-无支付状态状态。',
  `CONFIRM` tinyint(4) NOT NULL COMMENT '认证状态。0-未认证，1-已认证，2-否认证',
  `UPDATE_AT` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_CAMPAIGN_ID` (`CAMPAIGN_ID`),
  KEY `IDX_OCD` (`OCD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='记录Chanet推送的行为和交易数据';

-- --------------------------------------------------------

--
-- Table structure for table `operation_log`
--

CREATE TABLE IF NOT EXISTS `operation_log` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '递增主键',
  `OP_USER` bigint(20) NOT NULL COMMENT '操作者用户ID',
  `OP_TYPE` int(10) NOT NULL COMMENT '操作类型，0-',
  `DESCRIPTION` varchar(2000) NOT NULL COMMENT '操作行为描述',
  `TARGET_USER` bigint(20) DEFAULT NULL COMMENT '操作目标用户ID，如果有的话',
  `OUT_ID` bigint(11) DEFAULT NULL COMMENT '对应操作表中的ID，若有的话',
  `CREATE_AT` datetime NOT NULL COMMENT '操作时间',
  `OP_NAME` varchar(128) NOT NULL DEFAULT '' COMMENT '操作类型名称',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作记录表';

-- --------------------------------------------------------

--
-- Table structure for table `publisher_contact_info`
--

CREATE TABLE IF NOT EXISTS `publisher_contact_info` (
  `USER_ID` int(11) NOT NULL COMMENT '站长USERID',
  `EMAIL` varchar(100) NOT NULL,
  `NAME` varchar(20) NOT NULL COMMENT '联系人姓名',
  `MOBILE` varchar(20) NOT NULL COMMENT '联系人手机号',
  `QQ` varchar(20) NOT NULL COMMENT '联系人QQ号',
  `RECEIVING_METHOD` tinyint(4) NOT NULL COMMENT '收款方式，0-支付宝',
  `RECEIVING_ACCOUNT` varchar(50) NOT NULL COMMENT '收款账户',
  `RECEIVING_NAME` varchar(20) NOT NULL COMMENT '收款姓名',
  `RECEIVING_BANK` varchar(50) NOT NULL COMMENT '开户银行',
  `STATUS` tinyint(4) NOT NULL COMMENT '状态：0-Normal，1-Frozen',
  PRIMARY KEY (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='记录站长的联系信息';

-- --------------------------------------------------------

--
-- Table structure for table `publisher_payment`
--

CREATE TABLE IF NOT EXISTS `publisher_payment` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL COMMENT '站长用户ID',
  `PERIOD` varchar(100) NOT NULL DEFAULT '0' COMMENT '支付的结算周期，如2012.7至2012.10',
  `AMOUNT` bigint(20) NOT NULL DEFAULT '0' COMMENT '应付金额',
  `FEE` bigint(20) NOT NULL DEFAULT '0' COMMENT '手续费',
  `TAX` bigint(20) NOT NULL DEFAULT '0' COMMENT '税',
  `PAID` bigint(20) NOT NULL DEFAULT '0' COMMENT '实付金额',
  `RECEIPT_NO` varchar(100) NOT NULL DEFAULT '0' COMMENT '支付的流水号',
  `COMMENT` varchar(500) NOT NULL DEFAULT '' COMMENT '备注',
  `PAYMENT_TIME` datetime DEFAULT NULL COMMENT '付账时间',
  `UPDATE_TIME` datetime NOT NULL COMMENT '更新时间',
  `STATUS` tinyint(4) NOT NULL COMMENT '0删除,1正常',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='站长支付信息';

-- --------------------------------------------------------

--
-- Table structure for table `publisher_settlement`
--

CREATE TABLE IF NOT EXISTS `publisher_settlement` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `MONTH` date NOT NULL COMMENT '结算月，每月首日',
  `USER_ID` bigint(20) NOT NULL COMMENT '站长用户ID',
  `CPS_NO` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS订单数（已确认CPS）',
  `CPS_COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPS站长佣金（已确认CPS）',
  `CPC_NO` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC点击数',
  `CPC_COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPC站长佣金',
  `COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT '佣金总额',
  `STATUS` tinyint(1) NOT NULL DEFAULT '0' COMMENT '结算状态：0未支付，1已支付',
  `PAYMENT_ID` bigint(20) DEFAULT NULL COMMENT '支付记录ID',
  `PAYMENT_TIME` datetime DEFAULT NULL COMMENT '付账时间',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UNQ_USERID_MONTH` (`USER_ID`,`MONTH`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='站长结算表';

-- --------------------------------------------------------

--
-- Table structure for table `stats_admin_daily`
--

CREATE TABLE IF NOT EXISTS `stats_admin_daily` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `DATE_DAY` date NOT NULL COMMENT '统计日期',
  `PRODUCT` tinyint(4) NOT NULL COMMENT '来源：0-Lezhi，1-bShare',
  `VIEWS` bigint(20) NOT NULL DEFAULT '0' COMMENT '广告浏览数',
  `CLICKS` bigint(20) NOT NULL DEFAULT '0' COMMENT '广告点击数',
  `CPC_CLICK_NO` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC有效点击',
  `CPC_PUB_COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPC站长佣金，即我们发放给站长的佣金',
  `CPC_TOTAL_COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPC佣金总额，即我们得到的佣金',
  `CPS_ORDER_NO` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS订单数',
  `CPS_CONFIRMED_COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPS站长实际佣金，即已确认的CPS佣金',
  `CPS_PUB_COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPS站长预期佣金，包括已确认和未确认的CPS佣金',
  `CPS_TOTAL_CONFIRMED_COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPS实际收入',
  `CPS_TOTAL_COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPS佣金总额，即我们得到的佣金',
  `CPS_TOTAL_PRICE` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPS总订单金额',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UNQ_UUID_DAY` (`DATE_DAY`,`PRODUCT`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Admin每日数据';

-- --------------------------------------------------------

--
-- Table structure for table `stats_ad_daily`
--

CREATE TABLE IF NOT EXISTS `stats_ad_daily` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `ADENTRY_ID` bigint(20) NOT NULL COMMENT '广告ID',
  `DATE_DAY` date NOT NULL COMMENT '统计日期',
  `PRODUCT` tinyint(4) NOT NULL COMMENT '来源：0-Lezhi，1-bShare',
  `ADORDER_ID` bigint(20) NOT NULL COMMENT '广告订单ID',
  `VIEWS` bigint(20) NOT NULL DEFAULT '0' COMMENT '广告浏览数',
  `CLICKS` bigint(20) NOT NULL DEFAULT '0' COMMENT '广告点击数',
  `CPC_CLICK_NO` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC有效点击',
  `CPC_PUB_COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPC站长佣金，即我们发放给站长的佣金',
  `CPC_TOTAL_COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPC佣金总额，即我们得到的佣金',
  `CPS_ORDER_NO` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS订单数',
  `CPS_CONFIRMED_COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPS站长实际佣金，即已确认的CPS佣金',
  `CPS_PUB_COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPS站长佣金，即我们发放给站长的佣金',
  `CPS_TOTAL_CONFIRMED_COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPS实际收入',
  `CPS_TOTAL_COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPS佣金总额，即我们得到的佣金',
  `CPS_TOTAL_PRICE` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPS总订单金额',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UNQ_ADENTRY_DAY` (`ADENTRY_ID`,`DATE_DAY`,`PRODUCT`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='广告每日数据';

-- --------------------------------------------------------

--
-- Table structure for table `stats_publisher_daily`
--

CREATE TABLE IF NOT EXISTS `stats_publisher_daily` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `UUID` binary(16) NOT NULL COMMENT '站长UUID',
  `DATE_DAY` date NOT NULL COMMENT '统计日期',
  `PRODUCT` tinyint(4) NOT NULL COMMENT '来源：0-Lezhi，1-bShare',
  `VIEWS` bigint(20) NOT NULL DEFAULT '0' COMMENT '广告浏览数',
  `CLICKS` bigint(20) NOT NULL DEFAULT '0' COMMENT '广告点击数',
  `CPC_CLICK_NO` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC有效点击',
  `CPC_PUB_COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPC站长佣金，即我们发放给站长的佣金',
  `CPC_TOTAL_COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPC佣金总额，即我们得到的佣金',
  `CPS_ORDER_NO` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS订单数',
  `CPS_CONFIRMED_COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPS站长实际佣金，即已确认的CPS佣金',
  `CPS_PUB_COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPS站长佣金，即我们发放给站长的佣金',
  `CPS_TOTAL_CONFIRMED_COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPS实际收入',
  `CPS_TOTAL_COMMISSION` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPS佣金总额，即我们得到的佣金',
  `CPS_TOTAL_PRICE` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPS总订单金额',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UNQ_UUID_DAY` (`UUID`,`DATE_DAY`,`PRODUCT`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='站长每日数据';
