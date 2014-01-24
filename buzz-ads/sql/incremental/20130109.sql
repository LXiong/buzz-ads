
-- DROP TABLE stats_publisher_ad;

ALTER TABLE  `buzzads`.`stats_ad_daily` ADD  `PRODUCT` tinyint(4) NOT NULL COMMENT  '来源：0-Lezhi，1-bShare' AFTER  `DATE_DAY`;
ALTER TABLE  `buzzads`.`stats_ad_daily` ADD  `CPC_CLICK_NO` int(11) NOT NULL DEFAULT  '0' COMMENT 'CPC有效点击' AFTER  `CLICKS`;
ALTER TABLE  `buzzads`.`stats_ad_daily` ADD  `CPC_PUB_COMMISSION` bigint(20) NOT NULL DEFAULT  '0' COMMENT 'CPC站长佣金，即我们发放给站长的佣金' AFTER  `CPC_CLICK_NO`;
ALTER TABLE  `buzzads`.`stats_ad_daily` ADD  `CPC_TOTAL_COMMISSION` bigint(20) NOT NULL DEFAULT  '0' COMMENT 'CPC佣金总额，即我们得到的佣金' AFTER  `CPC_PUB_COMMISSION`;
ALTER TABLE  `buzzads`.`stats_ad_daily` ADD  `CPS_CONFIRMED_COMMISSION` bigint(20) NOT NULL DEFAULT  '0' COMMENT 'CPS站长实际佣金，即已确认的CPS佣金' AFTER  `CPS_ORDER_NO`;
ALTER TABLE  `buzzads`.`stats_ad_daily` ADD  `CPS_TOTAL_CONFIRMED_COMMISSION` bigint(20) NOT NULL DEFAULT  '0' COMMENT 'CPS实际收入' AFTER  `CPS_PUB_COMMISSION`;

ALTER TABLE  `buzzads`.`stats_publisher_daily` ADD  `PRODUCT` tinyint(4) NOT NULL COMMENT  '来源：0-Lezhi，1-bShare' AFTER  `DATE_DAY`;
ALTER TABLE  `buzzads`.`stats_publisher_daily` ADD  `CPC_CLICK_NO` int(11) NOT NULL DEFAULT  '0' COMMENT 'CPC有效点击' AFTER  `CLICKS`;
ALTER TABLE  `buzzads`.`stats_publisher_daily` ADD  `CPC_PUB_COMMISSION` bigint(20) NOT NULL DEFAULT  '0' COMMENT 'CPC站长佣金，即我们发放给站长的佣金' AFTER  `CPC_CLICK_NO`;
ALTER TABLE  `buzzads`.`stats_publisher_daily` ADD  `CPC_TOTAL_COMMISSION` bigint(20) NOT NULL DEFAULT  '0' COMMENT 'CPC佣金总额，即我们得到的佣金' AFTER  `CPC_PUB_COMMISSION`;
ALTER TABLE  `buzzads`.`stats_publisher_daily` ADD  `CPS_CONFIRMED_COMMISSION` bigint(20) NOT NULL DEFAULT  '0' COMMENT 'CPS站长实际佣金，即已确认的CPS佣金' AFTER  `CPS_ORDER_NO`;



CREATE TABLE IF NOT EXISTS `buzzads`.`stats_admin_daily` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `DATE_DAY` date NOT NULL COMMENT '统计日期',
  `PRODUCT` tinyint(4) NOT NULL COMMENT '来源：0-Lezhi，1-bShare',
  `VIEWS` bigint(20) NOT NULL DEFAULT  '0' COMMENT '广告浏览数',
  `CLICKS` bigint(20) NOT NULL DEFAULT  '0' COMMENT '广告点击数',
  `CPC_CLICK_NO` int(11) NOT NULL DEFAULT  '0' COMMENT 'CPC有效点击',
  `CPC_PUB_COMMISSION` bigint(20) NOT NULL DEFAULT  '0' COMMENT 'CPC站长佣金，即我们发放给站长的佣金',
  `CPC_TOTAL_COMMISSION` bigint(20) NOT NULL DEFAULT  '0' COMMENT 'CPC佣金总额，即我们得到的佣金',
  `CPS_ORDER_NO` int(11) NOT NULL DEFAULT  '0' COMMENT 'CPS订单数',
  `CPS_CONFIRMED_COMMISSION` bigint(20) NOT NULL DEFAULT  '0' COMMENT 'CPS站长实际佣金，即已确认的CPS佣金',
  `CPS_PUB_COMMISSION` bigint(20) NOT NULL DEFAULT  '0' COMMENT 'CPS站长预期佣金，包括已确认和未确认的CPS佣金',
  `CPS_TOTAL_COMMISSION` bigint(20) NOT NULL DEFAULT  '0' COMMENT 'CPS佣金总额，即我们得到的佣金',
  `CPS_TOTAL_PRICE` bigint(20) NOT NULL DEFAULT  '0' COMMENT 'CPS总订单金额',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UNQ_UUID_DAY` (`DATE_DAY`,`PRODUCT`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Admin每日数据';


ALTER TABLE  `buzzads`.`publisher_contact_info` ADD  `STATUS` TINYINT NOT NULL COMMENT  '状态：0-Normal，1-Frozen';

INSERT INTO `bshare`.`role` (`ID`, `NAME`, `DESCN`) VALUES 
    ('17', 'ROLE_AD_ADMIN', 'bshare_ad admin'), 
    ('19', 'ROLE_AD_FINANCE', 'bshare_ad finance');

    
    
ALTER TABLE  `buzzads`.`ad_detail_cps` ADD  `PRODUCT` TINYINT NOT NULL COMMENT  '来源：0-Lezhi，1-bShare' AFTER  `SOURCE`;

ALTER TABLE  `buzzads`.`publisher_contact_info` ADD  `EMAIL` VARCHAR( 100 ) NOT NULL AFTER  `USER_ID`;
    
DROP TABLE `buzzads`.`publisher_settlement`;
    
CREATE TABLE IF NOT EXISTS `buzzads`.`publisher_settlement`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `MONTH` date NOT NULL COMMENT '结算月，每月首日',
  `USER_ID` bigint(20) NOT NULL COMMENT '站长用户ID',
  `CPS_NO` int(11) NOT NULL DEFAULT  '0' COMMENT 'CPS订单数（已确认CPS）',
  `CPS_COMMISSION` bigint(20) NOT NULL DEFAULT  '0' COMMENT 'CPS站长佣金（已确认CPS）',
  `CPC_NO` int(11) NOT NULL DEFAULT  '0' COMMENT 'CPC点击数',
  `CPC_COMMISSION` bigint(20) NOT NULL DEFAULT  '0' COMMENT 'CPC站长佣金',
  `COMMISSION` bigint(20) NOT NULL DEFAULT  '0' COMMENT '佣金总额',
  `STATUS` tinyint(1) NOT NULL DEFAULT  '0' COMMENT '结算状态：0未支付，1已支付',
  `PAYMENT_ID` bigint(20) DEFAULT NULL COMMENT '支付记录ID',
  `PAYMENT_TIME` datetime DEFAULT NULL COMMENT '付账时间',
  PRIMARY KEY (`ID`),
  UNIQUE  `UNQ_USERID_MONTH` (  `USER_ID`, `MONTH`  )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='站长结算表';


CREATE TABLE IF NOT EXISTS `buzzads`.`publisher_payment` (
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
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='站长支付信息';


CREATE TABLE IF NOT EXISTS `buzzads`.`operation_log` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '递增主键',
  `OP_USER` bigint(20) NOT NULL COMMENT '操作者用户ID',
  `OP_TYPE` tinyint(4) NOT NULL COMMENT '操作类型，0-',
  `DESCRIPTION` varchar(200) NOT NULL COMMENT '操作行为描述',
  `TARGET_USER` bigint(20) DEFAULT NULL COMMENT '操作目标用户ID，如果有的话',
  `OUT_ID` bigint(11) DEFAULT NULL COMMENT '对应操作表中的ID，若有的话',
  `CREATE_AT` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作记录表';

ALTER TABLE  `buzzads`.`stats_publisher_daily` ADD  `CPS_TOTAL_CONFIRMED_COMMISSION` bigint(20) NOT NULL DEFAULT  '0' COMMENT 'CPS实际收入' AFTER  `CPS_PUB_COMMISSION`;
ALTER TABLE  `buzzads`.`stats_admin_daily` ADD  `CPS_TOTAL_CONFIRMED_COMMISSION` bigint(20) NOT NULL DEFAULT  '0' COMMENT 'CPS实际收入' AFTER  `CPS_PUB_COMMISSION`;

ALTER TABLE  `chanet_data` ADD  `UPDATE_AT` DATETIME NULL DEFAULT NULL;

ALTER TABLE  `buzzads`.`publisher_payment` ADD  `STATUS` tinyint(4) NOT NULL COMMENT '0删除,1正常' AFTER  `PAYMENT_TIME`;
ALTER TABLE  `buzzads`.`publisher_payment` ADD  `UPDATE_TIME` datetime NOT NULL COMMENT '更新时间' AFTER  `PAYMENT_TIME`;

ALTER TABLE `buzzads`.`operation_log` add column `OP_NAME` varchar(128) DEFAULT '' NOT NULL COMMENT '操作类型名称' after `CREATE_AT`;
ALTER TABLE `buzzads`.`operation_log` change `OP_TYPE` `OP_TYPE` int(10) NOT NULL comment '操作类型，0-';
ALTER TABLE `buzzads`.`operation_log` CHANGE  `DESCRIPTION`  `DESCRIPTION` VARCHAR( 2000 ) NOT NULL COMMENT  '操作行为描述';

ALTER TABLE  `buzzads`.`stats_publisher_daily` DROP INDEX  `UNQ_UUID_DAY` ,
ADD UNIQUE  `UNQ_UUID_DAY` (  `UUID` ,  `DATE_DAY` ,  `PRODUCT` );
ALTER TABLE  `buzzads`.`stats_ad_daily` DROP INDEX  `UNQ_ADENTRY_DAY` ,
ADD UNIQUE  `UNQ_ADENTRY_DAY` (  `ADENTRY_ID` ,  `DATE_DAY` ,  `PRODUCT` );
