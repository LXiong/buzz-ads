--  增加dsp的统计功能
DROP TABLE IF EXISTS `adx_dsp_rtb_info`;
CREATE TABLE `adx_dsp_rtb_info` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `kEY` int(11) NOT NULL DEFAULT '0' COMMENT '用户在系统获取到的key(目前为advertiser Id)',
  `WINNERNOTIFYURL` varchar(100) NOT NULL COMMENT '接收bidResponse最终结果的地址',
  `BIDREQUESTURL` varchar(100) NOT NULL COMMENT '接收bidRequest的地址',
  `RTB` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否参与RTB 竞价',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UNQ_KEY` (`kEY`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='Adx RTB Info';

DROP TABLE IF EXISTS `adx_dsp_rtb_statistic`;
CREATE TABLE `adx_dsp_rtb_statistic` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `kEY` int(11) NOT NULL DEFAULT '0' COMMENT '用户在系统获取到的key(目前为advertiser Id)',
  `REQUEST_NO` bigint(20) DEFAULT '0' COMMENT 'DSP请求数据',
  `RESPONSE_NO` bigint(20) DEFAULT '0' COMMENT 'DSP返回数量',
  `BIDSUCC_NO` bigint(20) DEFAULT '0' COMMENT 'DSP竞价成功数量（即显示在page上）',
  `VALID_NO` bigint(20) DEFAULT '0' COMMENT 'DSP广告元素有效性数量',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UNQ_KEY` (`kEY`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='Adx RTB statistic Info';
