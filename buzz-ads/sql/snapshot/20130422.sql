-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 22, 2013 at 03:51 下午
-- Server version: 5.5.16
-- PHP Version: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `buzzads`
--

-- --------------------------------------------------------

--
-- Table structure for table `advertiser_account`
--

CREATE TABLE IF NOT EXISTS `advertiser_account` (
  `ADVERTISER_ID` int(11) NOT NULL COMMENT '广告主ID',
  `COMPANY_NAME` varchar(50) NOT NULL COMMENT '公司名',
  `WEBSITE_NAME` varchar(50) NOT NULL COMMENT '网站名',
  `WEBSITE_URL` varchar(200) NOT NULL COMMENT '网站URL',
  `BUSINESS_TYPE` tinyint(4) NOT NULL COMMENT '业务类型',
  `STATUS` tinyint(4) NOT NULL COMMENT '状态：0-Normal，1-Frozen',
  PRIMARY KEY (`ADVERTISER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='广告主帐户信息。';

-- --------------------------------------------------------

--
-- Table structure for table `advertiser_balance`
--

CREATE TABLE IF NOT EXISTS `advertiser_balance` (
  `ADVERTISER_ID` int(11) NOT NULL COMMENT '广告主ID',
  `DEBITS_TOTAL` bigint(20) NOT NULL COMMENT '借项和，即广告主支出和',
  `CREDITS_TOTAL` bigint(20) NOT NULL COMMENT '信贷和，即广告主充值和',
  `BALANCE` bigint(20) NOT NULL COMMENT '广告主的余额 ',
  `UPDATE_AT` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`ADVERTISER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='广告主余额与支付充值和';

-- --------------------------------------------------------

--
-- Table structure for table `advertiser_billing`
--

CREATE TABLE IF NOT EXISTS `advertiser_billing` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '递增主键',
  `ADVERTISER_ID` int(11) NOT NULL COMMENT '广告主ID',
  `TYPE` tinyint(4) NOT NULL COMMENT '结帐类型，目前可以是DEBIT_DAY，REFILL_RECHARGE等。',
  `COMMENT` varchar(200) DEFAULT NULL COMMENT '备注，如充值方式、流水号等',
  `DEBITS` int(11) NOT NULL COMMENT '借项，即广告主支出',
  `CREDITS` int(11) NOT NULL COMMENT '信贷，即广告主充值',
  `BALANCE` bigint(20) NOT NULL COMMENT '广告主的余额 ',
  `UPDATE_AT` datetime NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='广告主结帐';

-- --------------------------------------------------------

--
-- Table structure for table `advertiser_contact_info`
--

CREATE TABLE IF NOT EXISTS `advertiser_contact_info` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '联系人ID',
  `ADVERTISER_ID` int(11) NOT NULL COMMENT '广告主ID',
  `ADDRESS` varchar(100) NOT NULL COMMENT '地址',
  `EMAIL` varchar(100) NOT NULL COMMENT 'Email',
  `NAME` varchar(50) NOT NULL COMMENT '姓名',
  `MOBILE` varchar(20) NOT NULL COMMENT '手机号码',
  `QQ` varchar(20) NOT NULL COMMENT 'QQ',
  PRIMARY KEY (`ID`),
  KEY `IDX_ADVERTISER` (`ADVERTISER_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='广告主联系人信息。';

-- --------------------------------------------------------

--
-- Table structure for table `ad_campaign`
--

CREATE TABLE IF NOT EXISTS `ad_campaign` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `ADVERTISER_ID` int(11) NOT NULL COMMENT '广告主ID',
  `NAME` varchar(50) NOT NULL COMMENT '活动名称',
  `STATUS` tinyint(4) NOT NULL COMMENT '活动状态，：0-Ready，1-Enabled，2-Paused，3-Disabled，4-Suspended, 5-Deleted, 6-Verifying, 7-Rejected',
  `NETWORK` tinyint(4) NOT NULL COMMENT '活动网络：0-Lezhi，1-bShare，2-BuzzAds',
  `BID_TYPE` tinyint(4) NOT NULL COMMENT '活动出价类型：0-CPM，1-CPA，2-CPS，3-CPC',
  `START_DATE` date DEFAULT NULL COMMENT '活动开始日期',
  `END_DATE` date DEFAULT NULL COMMENT '活动结束日期',
  `LOCATION` varchar(200) DEFAULT NULL COMMENT '活动位置，以因为逗号分割的省市代号。比如”BJ,SH,TJ”，表示在指定的省份进行投放。',
  `UPDATE_AT` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`),
  KEY `IDX_ADVERTISER` (`ADVERTISER_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='广告活动信息。';

-- --------------------------------------------------------

--
-- Table structure for table `ad_campaign_budget`
--

CREATE TABLE IF NOT EXISTS `ad_campaign_budget` (
  `CAMPAIGN_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '广告活动ID',
  `ADVERTISER_ID` int(11) NOT NULL COMMENT '广告主ID',
  `DATE_DAY` date NOT NULL COMMENT '当日预算的日期',
  `BUDGET_DAY` bigint(20) NOT NULL DEFAULT '0' COMMENT '当日已使用预算',
  `BUDGET_TOTAL` bigint(20) NOT NULL DEFAULT '0' COMMENT '总的已使用预算',
  `MAX_BUDGET_DAY` bigint(20) NOT NULL DEFAULT '0' COMMENT '最大每日预算，<=0表示没有限制',
  `MAX_BUDGET_TOTAL` bigint(20) NOT NULL DEFAULT '0' COMMENT '最大总预算，<=0表示没有限制',
  PRIMARY KEY (`CAMPAIGN_ID`),
  KEY `IDX_ADVERTISER` (`ADVERTISER_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='广告活动预算统计';

-- --------------------------------------------------------

--
-- Table structure for table `ad_detail_cps`
--

CREATE TABLE IF NOT EXISTS `ad_detail_cps` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `UUID` binary(16) NOT NULL COMMENT '站长UUID',
  `ADORDER_ID` int(11) NOT NULL COMMENT '广告订单ID',
  `ADENTRY_ID` int(11) NOT NULL COMMENT '广告ID',
  `SOURCE` tinyint(4) NOT NULL COMMENT '来源：0-Buzz，1-Chanet',
  `NETWORK` tinyint(4) NOT NULL COMMENT '来源：0-Lezhi，1-bShare',
  `CPS_OID` varchar(50) NOT NULL COMMENT 'CPS订单号',
  `STATUS` tinyint(4) NOT NULL COMMENT 'CPS状态，0-未确认，1-已确认',
  `PUB_COMMISSION` int(11) NOT NULL COMMENT 'CPS站长佣金，即我们发放给站长的佣金',
  `COMMISSION` int(11) NOT NULL COMMENT 'CPS佣金总额，即我们得到的佣金',
  `TOTAL_PRICE` int(11) NOT NULL COMMENT 'CPS订单总额',
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
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ADVERTISER_ID` int(11) NOT NULL COMMENT '广告主ID',
  `CAMPAIGN_ID` int(11) NOT NULL COMMENT '广告活动ID',
  `ORDER_ID` int(20) NOT NULL COMMENT '广告订单ID',
  `NAME` varchar(100) NOT NULL COMMENT '广告项名字',
  `STATUS` tinyint(4) NOT NULL COMMENT '广告项状态：0-Ready，1-Enabled，2-Paused，3-Disabled，4-Suspended, 5-Deleted, 6-Verifying, 7-Rejected',
  `LINK` text NOT NULL COMMENT '广告链接',
  `RESOURCE_TYPE` tinyint(4) NOT NULL COMMENT '广告资源的类型。0-TEXT，1-IMAGE',
  `RESOURCE_URL` text NOT NULL COMMENT '广告资源的URL，例如图片URL',
  `SIZE` int(11) NOT NULL COMMENT '广告大小',
  `DISPLAY_URL` varchar(100) DEFAULT NULL COMMENT '展示URL',
  `TITLE` varchar(500) DEFAULT NULL COMMENT '广告标题',
  `DESCRIPTION` varchar(500) DEFAULT NULL COMMENT '广告文字',
  `UPDATE_AT` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`),
  KEY `IDX_ADVERTISER` (`ADVERTISER_ID`),
  KEY `IDX_CAMPAIGN` (`CAMPAIGN_ID`),
  KEY `IDX_ORDER` (`ORDER_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='广告表';

-- --------------------------------------------------------

--
-- Table structure for table `ad_order`
--

CREATE TABLE IF NOT EXISTS `ad_order` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `ADVERTISER_ID` int(11) NOT NULL COMMENT '广告主ID',
  `CAMPAIGN_ID` int(11) NOT NULL COMMENT '广告活动ID',
  `NAME` varchar(100) NOT NULL COMMENT '订单名字',
  `NETWORK` tinyint(4) NOT NULL COMMENT '广告网络：0-Lezhi，1-bShare，2-BuzzAds',
  `BID_PRICE` int(11) NOT NULL COMMENT '广告出价',
  `BID_TYPE` tinyint(4) NOT NULL COMMENT '支付出价：0-CPM，1-CPA，2-CPS，3-CPC',
  `STATUS` tinyint(4) NOT NULL COMMENT '广告订单状态，：0-Ready，1-Enabled，2-Paused，3-Disabled，4-Suspended, 5-Deleted, 6-Verifying, 7-Rejected',
  `KEYWORDS` varchar(500) DEFAULT NULL COMMENT '广告的关键词',
  `START_DATE` date DEFAULT NULL COMMENT '订单开始时间',
  `END_DATE` date DEFAULT NULL COMMENT '订单结束时间',
  `SCHEDULE_DAY` int(11) NOT NULL DEFAULT '0' COMMENT '计划投放日。使用IntegerValuedEnumSetType保存的WeekDay集合，为空则不指定投放日，即每日都可投放。',
  `SCHEDULE_TIME` varchar(100) DEFAULT NULL COMMENT '计划投放时间。包括开始时间和结束时间，例如10:00-18:00。为空则不指定投放时间，即一天24小时都可投放。',
  `UPDATE_AT` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`),
  KEY `IDX_ADVERTISER` (`ADVERTISER_ID`),
  KEY `IDX_CAMPAIGN` (`CAMPAIGN_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='广告订单表';

-- --------------------------------------------------------

--
-- Table structure for table `ad_order_category`
--

CREATE TABLE IF NOT EXISTS `ad_order_category` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PARENT_ID` int(11) NOT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `NAME_EN` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `PARENT_ID_IDX` (`PARENT_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

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
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '递增主键',
  `OP_USER` bigint(20) NOT NULL COMMENT '操作者用户ID',
  `OP_TYPE` int(10) NOT NULL COMMENT '操作类型，0-',
  `DESCRIPTION` varchar(2000) NOT NULL COMMENT '操作行为描述',
  `TARGET_USER` bigint(20) DEFAULT NULL COMMENT '操作目标用户ID，如果有的话',
  `OUT_ID` bigint(11) DEFAULT NULL COMMENT '对应操作表中的ID，若有的话',
  `CREATE_AT` datetime NOT NULL COMMENT '操作时间',
  `OP_NAME` varchar(128) NOT NULL DEFAULT '' COMMENT '操作类型名称',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='操作记录表';

-- --------------------------------------------------------

--
-- Table structure for table `payment_order_info`
--

CREATE TABLE IF NOT EXISTS `payment_order_info` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `ORDER_NO` varchar(32) NOT NULL,
  `PRODUCT_TYPE` tinyint(1) NOT NULL,
  `UNIT_PRICE` int(11) NOT NULL,
  `AMOUNT_MONTH` tinyint(4) NOT NULL,
  `TOTAL_FEE` int(11) NOT NULL,
  `ORDER_TIME` datetime NOT NULL,
  `ORDER_STATUS` tinyint(4) DEFAULT NULL,
  `PAYMENT_PLATFORM` tinyint(4) DEFAULT NULL,
  `OUT_TRADE_NO` varchar(64) DEFAULT NULL,
  `PAID_TIME` datetime DEFAULT NULL,
  `SEND_EMAIL` tinyint(1) DEFAULT '0',
  `DATE_START` date DEFAULT NULL,
  `DATE_END` date DEFAULT NULL,
  `REMARK` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ORDER_NO_UNIQUE` (`ORDER_NO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `publisher_contact_info`
--

CREATE TABLE IF NOT EXISTS `publisher_contact_info` (
  `USER_ID` bigint(20) NOT NULL COMMENT '站长USERID',
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
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` int(20) NOT NULL COMMENT '站长用户ID',
  `PERIOD` varchar(100) NOT NULL DEFAULT '0' COMMENT '支付的结算周期，如2012.7至2012.10',
  `AMOUNT` int(11) NOT NULL DEFAULT '0' COMMENT '应付金额',
  `FEE` int(11) NOT NULL DEFAULT '0' COMMENT '手续费',
  `TAX` int(11) NOT NULL DEFAULT '0' COMMENT '税',
  `PAID` int(11) NOT NULL DEFAULT '0' COMMENT '实付金额',
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
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `MONTH` date NOT NULL COMMENT '结算月，每月首日',
  `USER_ID` int(20) NOT NULL COMMENT '站长用户ID',
  `CPS_NO` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS订单数（已确认CPS）',
  `CPS_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS站长佣金（已确认CPS）',
  `CPC_NO` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC点击数',
  `CPC_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC站长佣金',
  `COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT '佣金总额',
  `STATUS` tinyint(1) NOT NULL DEFAULT '0' COMMENT '结算状态：0未支付，1已支付',
  `PAYMENT_ID` int(11) NOT NULL DEFAULT '0' COMMENT '支付记录ID',
  `PAYMENT_TIME` datetime DEFAULT NULL COMMENT '付账时间',
  `COMM_UPDATE_DAY` date NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UNQ_USERID_MONTH` (`USER_ID`,`MONTH`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='站长结算表';

-- --------------------------------------------------------

--
-- Table structure for table `recharge_alipay_record`
--

CREATE TABLE IF NOT EXISTS `recharge_alipay_record` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `RECORD_ID` int(11) NOT NULL,
  `ADVERTISER_ID` int(11) NOT NULL,
  `TOTAL_FEE` int(11) NOT NULL DEFAULT '0',
  `TRADE_NO` varchar(100) DEFAULT '',
  `TRADE_STATUS` int(11) NOT NULL DEFAULT '0',
  `CREATE_TIME` datetime NOT NULL,
  `PAYMENT_TIME` datetime NOT NULL,
  `ALI_TRADE_NO` varchar(100) DEFAULT '',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `recharge_operate_his`
--

CREATE TABLE IF NOT EXISTS `recharge_operate_his` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `OPERATE_TYPE` tinyint(1) NOT NULL,
  `RECORD_ID` int(11) NOT NULL,
  `ADVERTISER_ID` int(11) NOT NULL,
  `INSERT_TIME` datetime NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `stats_admin_daily`
--

CREATE TABLE IF NOT EXISTS `stats_admin_daily` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `DATE_DAY` date NOT NULL COMMENT '统计日期',
  `NETWORK` tinyint(4) NOT NULL COMMENT '来源：0-Lezhi，1-bShare',
  `VIEWS` int(11) NOT NULL DEFAULT '0' COMMENT '广告浏览数',
  `CLICKS` int(11) NOT NULL DEFAULT '0' COMMENT '广告点击数',
  `CPC_CLICK_NO` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC有效点击',
  `CPC_PUB_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC站长佣金，即我们发放给站长的佣金',
  `CPC_TOTAL_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC佣金总额，即我们得到的佣金',
  `CPS_ORDER_NO` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS订单数',
  `CPS_CONFIRMED_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS站长实际佣金，即已确认的CPS佣金',
  `CPS_PUB_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS站长预期佣金，包括已确认和未确认的CPS佣金',
  `CPS_TOTAL_CONFIRMED_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS实际收入',
  `CPS_TOTAL_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS佣金总额，即我们得到的佣金',
  `CPS_TOTAL_PRICE` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS总订单金额',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UNQ_UUID_DAY` (`DATE_DAY`,`NETWORK`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='Admin每日数据';

-- --------------------------------------------------------

--
-- Table structure for table `stats_ad_daily`
--

CREATE TABLE IF NOT EXISTS `stats_ad_daily` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `ADENTRY_ID` int(11) NOT NULL COMMENT '广告ID',
  `DATE_DAY` date NOT NULL COMMENT '统计日期',
  `NETWORK` tinyint(4) NOT NULL COMMENT '来源：0-Lezhi，1-bShare',
  `ADORDER_ID` int(11) NOT NULL COMMENT '广告订单ID',
  `VIEWS` int(11) NOT NULL DEFAULT '0' COMMENT '广告浏览数',
  `CLICKS` int(11) NOT NULL DEFAULT '0' COMMENT '广告点击数',
  `CPC_CLICK_NO` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC有效点击',
  `CPC_PUB_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC站长佣金，即我们发放给站长的佣金',
  `CPC_TOTAL_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC佣金总额，即我们得到的佣金',
  `CPS_ORDER_NO` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS订单数',
  `CPS_CONFIRMED_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS站长实际佣金，即已确认的CPS佣金',
  `CPS_PUB_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS站长佣金，即我们发放给站长的佣金',
  `CPS_TOTAL_CONFIRMED_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS实际收入',
  `CPS_TOTAL_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS佣金总额，即我们得到的佣金',
  `CPS_TOTAL_PRICE` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS总订单金额',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UNQ_ADENTRY_DAY` (`ADENTRY_ID`,`DATE_DAY`,`NETWORK`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='广告每日数据';

-- --------------------------------------------------------

--
-- Table structure for table `stats_campaign_daily`
--

CREATE TABLE IF NOT EXISTS `stats_campaign_daily` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `CAMPAIGN_ID` int(11) NOT NULL COMMENT '广告活动ID',
  `DATE_DAY` date NOT NULL COMMENT '统计日期',
  `NETWORK` tinyint(4) NOT NULL COMMENT '广告网络：0-Lezhi，1-bShare，2-BuzzAds',
  `ADVERTISER_ID` int(11) NOT NULL COMMENT '广告主ID',
  `VIEWS` int(11) NOT NULL DEFAULT '0' COMMENT '广告浏览数',
  `CLICKS` int(11) NOT NULL DEFAULT '0' COMMENT '广告点击数',
  `CPC_CLICK_NO` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC有效点击',
  `CPC_PUB_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC站长佣金，即我们发放给站长的佣金',
  `CPC_TOTAL_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC佣金总额，即我们得到的佣金',
  `CPS_ORDER_NO` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS订单数',
  `CPS_CONFIRMED_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS站长实际佣金，即已确认的CPS佣金',
  `CPS_PUB_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS站长佣金，即我们发放给站长的佣金',
  `CPS_TOTAL_CONFIRMED_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS实际收入',
  `CPS_TOTAL_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS佣金总额，即我们得到的佣金',
  `CPS_TOTAL_PRICE` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS总订单金额',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UNQ_UUID_DAY` (`CAMPAIGN_ID`,`DATE_DAY`,`NETWORK`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='广告活动每日数据';

-- --------------------------------------------------------

--
-- Table structure for table `stats_order_daily`
--

CREATE TABLE IF NOT EXISTS `stats_order_daily` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `ORDER_ID` int(11) NOT NULL COMMENT '广告组ID',
  `DATE_DAY` date NOT NULL COMMENT '统计日期',
  `NETWORK` tinyint(4) NOT NULL COMMENT '广告网络：0-Lezhi，1-bShare，2-BuzzAds',
  `VIEWS` int(11) NOT NULL DEFAULT '0' COMMENT '广告浏览数',
  `CLICKS` int(11) NOT NULL DEFAULT '0' COMMENT '广告点击数',
  `CPC_CLICK_NO` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC有效点击',
  `CPC_PUB_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC站长佣金，即我们发放给站长的佣金',
  `CPC_TOTAL_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC佣金总额，即我们得到的佣金',
  `CPS_ORDER_NO` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS订单数',
  `CPS_CONFIRMED_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS站长实际佣金，即已确认的CPS佣金',
  `CPS_PUB_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS站长佣金，即我们发放给站长的佣金',
  `CPS_TOTAL_CONFIRMED_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS实际收入',
  `CPS_TOTAL_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS佣金总额，即我们得到的佣金',
  `CPS_TOTAL_PRICE` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS总订单金额',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UNQ_UUID_DAY` (`ORDER_ID`,`DATE_DAY`,`NETWORK`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='广告活动每日数据';

-- --------------------------------------------------------

--
-- Table structure for table `stats_publisher_daily`
--

CREATE TABLE IF NOT EXISTS `stats_publisher_daily` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `UUID` binary(16) NOT NULL COMMENT '站长UUID',
  `DATE_DAY` date NOT NULL COMMENT '统计日期',
  `NETWORK` tinyint(4) NOT NULL COMMENT '来源：0-Lezhi，1-bShare',
  `VIEWS` int(11) NOT NULL DEFAULT '0' COMMENT '广告浏览数',
  `CLICKS` int(11) NOT NULL DEFAULT '0' COMMENT '广告点击数',
  `CPC_CLICK_NO` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC有效点击',
  `CPC_PUB_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC站长佣金，即我们发放给站长的佣金',
  `CPC_TOTAL_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPC佣金总额，即我们得到的佣金',
  `CPS_ORDER_NO` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS订单数',
  `CPS_CONFIRMED_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS站长实际佣金，即已确认的CPS佣金',
  `CPS_PUB_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS站长佣金，即我们发放给站长的佣金',
  `CPS_TOTAL_CONFIRMED_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS实际收入',
  `CPS_TOTAL_COMMISSION` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS佣金总额，即我们得到的佣金',
  `CPS_TOTAL_PRICE` int(11) NOT NULL DEFAULT '0' COMMENT 'CPS总订单金额',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UNQ_UUID_DAY` (`UUID`,`DATE_DAY`,`NETWORK`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='站长每日数据';




INSERT INTO `buzzads`.`ad_order_category` VALUES
('1', '0', '文化娱乐', 'Arts & Entertainment'),
('2', '0', '教育培训', 'Jobs & Education'),
('3', '0', '游戏', 'Games'),
('4', '0', '消费数码', 'Consumer Electronics'),
('5', '0', '健康医疗', 'Health & Wellness'),
('6', '0', '美容及化妆品', 'Beauty & Cosmetics'),
('7', '0', '汽车', 'Automobiles'),
('8', '0', '运动健身', 'Sports & Fitness'),
('9', '0', 'IT及信息产业', 'Internet & Technology'),
('10', '0', '母婴育儿', 'Baby & Maternity'),
('11', '0', '金融财经', 'Personal Finance'),
('12', '0', '咨询及服务', 'Consulting & Services'),
('13', '0', '工业', 'Business & Industrial'),
('14', '0', '女性时尚', 'Female Fashion'),
('15', '0', '旅游商旅', 'Leisure Travel & Business Travel'),
('16', '0', '食品美食', 'Food & Drink'),
('17', '0', '家居生活', 'Home & Lifestyle'),
('18', '0', '房地产及建筑', 'Real Estate & Construction'),
('19', '0', '办公用品', 'Office Equipment'),
('20', '0', '农业', 'Agriculture'),
('21', '0', '烟酒', 'Tobacco & Alcohol'),
('22', '0', '时政与财经', 'Politics & Finance'),
('23', '1', '娱乐综艺', 'Variety & Entertainment'),
('24', '1', '院线电影', 'Movies'),
('25', '1', '网络影视剧', 'TV Dramas'),
('26', '1', '动画漫画', 'Comics & Animation'),
('27', '1', '网络小说', 'Online Literature'),
('28', '1', '文化文学', 'Culture/Literature'),
('29', '1', '书刊杂志', 'Books/Magazines'),
('30', '1', '高雅音乐', 'Classical Music & Jazz'),
('31', '1', '流行音乐', 'Pop Music'),
('32', '1', '音响器材', 'Audio Equipment'),
('33', '1', '乐器', 'Musical Instruments'),
('34', '1', '人文艺术', 'Humanities & Arts'),
('35', '1', '其他', 'Others'),
('36', '2', '初级中级教育', 'Primary & Secondary Schooling'),
('37', '2', '学历与留学', 'Overseas Education'),
('38', '2', 'MBA', 'MBA'),
('39', '2', '职业技能培训', 'Vocational & Continuing Education'),
('40', '2', '其他', 'Others'),
('41', '3', '大型客户端游戏', 'Massive Multiplayer'),
('42', '3', '网页游戏', 'Online Games'),
('43', '3', '手机平板游戏', 'Mobile Phone/Tablet Games'),
('44', '3', '小游戏Flash游戏', 'Mini Games & Flash Games'),
('45', '3', '电子竞技', 'Sports Games'),
('46', '3', '其他', 'Others'),
('47', '4', '摄影摄像', 'Photography & Videography'),
('48', '4', '电玩', 'Video Games'),
('49', '4', '手机平板与应用', 'Mobile Phones/Tablets & Apps'),
('50', '4', '硬件及网络', 'Hardware & Networking'),
('51', '4', '家电', 'Home Appliances'),
('52', '4', '其他', 'Others'),
('53', '5', '情感心理', 'Psychology'),
('54', '5', '整形整容', 'Cosmetic Procedures'),
('55', '5', '保健养生', 'Healthcare'),
('56', '5', '医疗', 'Medical Treatment'),
('57', '6', '护肤', 'Skin Care'),
('58', '6', '洗护日用', 'Daily Personal Care'),
('59', '6', '彩妆', 'Make-Up/Cosmetics'),
('60', '7', '高档汽车', 'Luxury Cars'),
('61', '7', '中档汽车', 'Mid-End Cars'),
('62', '7', '入门级汽车', 'Entry-Level Cars'),
('63', '7', '养车用车', 'Vehicle Maintenance'),
('64', '7', '二手车', 'Used Cars'),
('65', '7', '汽车租赁', 'Rental Cars'),
('66', '7', '汽车制造商', 'Car Manufacturers'),
('67', '7', '汽车保险', 'Auto Insurance'),
('68', '7', '汽车周边', 'Auto Accessories'),
('69', '7', '其他', 'Others'),
('70', '8', '运动', 'Sports'),
('71', '8', '健身', 'Fitness'),
('72', '8', '户外', 'Outdoor Sports'),
('73', '8', '其他', 'Others'),
('74', '9', '电信、通信服务', 'Telecommunications/Services'),
('75', '9', '软件', 'Software'),
('76', '10', '婴幼', 'Baby'),
('77', '10', '母婴用品', 'Maternity & Parenting'),
('78', '10', '童书阅读', 'Baby Books'),
('79', '11', '保险', 'Insurance'),
('80', '11', '投资理财', 'Investing'),
('81', '11', '其他', 'Others'),
('82', '12', '婚庆', 'Wedding Services'),
('83', '12', '健身娱乐', 'Health & Fitness Services'),
('84', '14', '服装', 'Clothing'),
('85', '14', '首饰饰品', 'Jewelry/Accessories'),
('86', '14', '奢侈品', 'Designers & Collections'),
('87', '14', '鞋包', 'Shoes & Bags'),
('88', '14', '化妆造型', 'Make-Up & Cosmetics'),
('89', '15', '酒店', 'Hotels & Accommodations'),
('90', '15', '旅游', 'Leisure Travel'),
('91', '15', '航空', 'Air Travel'),
('92', '15', '旅行社', 'Travel Agencies & Services'),
('93', '16', '烹饪和菜谱', 'Cooking & Recipes'),
('94', '16', '休闲食品', 'Snacks'),
('95', '16', '餐馆美食', 'Restaurants'),
('96', '16', '其他', 'Others'),
('97', '18', '开发商', 'Property Development'),
('98', '18', '中介代理', 'Real Estate Agencies'),
('99', '18', '物业管理', 'Property Management'),
('100', '18', '楼盘', 'Real Estate'),
('101', '18', '其他', 'Others'),
('102', '21', '洋酒', 'Liquor & Spirits'),
('103', '21', '红酒、白葡萄酒、香槟', 'Wine/Beer/Champagne'),
('104', '21', '国酒', 'Chinese Liquor/Spirits');