package com.buzzinate.buzzads.core.util;

/**
 * 系统日志常量
 *
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *         <p/>
 *         2012-12-25
 */
public interface LogConstants {

    //月结
    String OBJNAME_MONTHSETTLE = "月结";
    String OPTYPE_MONTHSETTLE_PAY = "10001";
    String OPNAME_MONTHSETTLE_PAY = "支付佣金";
    String OPTYPE_MONTHSETTLE_CANCELPAY = "10002";
    String OPNAME_MONTHSETTLE_CANCELPAY = "撤销";

    //会员管理
    String OBJNAME_PUBLISHER = "会员管理";
    String OPTYPE_PUBLISHER_FROZEN = "20001";
    String OPNAME_PUBLISHER_FROZEN = "冻结会员";
    String OPTYPE_PUBLISHER_UNFROZEN = "20002";
    String OPNAME_PUBLISHER_UNFROZEN = "解冻会员";
    String OPTYPE_PUBLISHER_UPDATE = "20003";
    String OPNAME_PUBLISHER_UPDATE = "更新会员";

    //广告管理
    String OBJNAME_ADENTRY = "广告管理";
    String OPTYPE_ADENTRY_ACCEPT = "30001";
    String OPNAME_ADENTRY_ACCEPT = "通过广告";
    String OPTYPE_ADENTRY_REJECT = "30002";
    String OPNAME_ADENTRY_REJECT = "拒绝广告";
    String OPTYPE_ADENTRY_OPERATE = "30003";
    String OPNAME_ADENTRY_OPERATE = "状态管理";

    //Adjustment管理
    String OBJNAME_ADJUSTMENT = "Adjustment管理";
    String OPTYPE_ADJUSTMENT_CREATE = "40001";
    String OPNAME_ADJUSTMENT_CREATE = "新增Adjustment";

    //充值
    String OBJNAME_ADVERTISER_RECHARGE = "广告主充值";
    String OPTYPE_ADVERTISER_RECHARGE = "50001";
    String OPNAME_ADVERTISER_RECHARGE = "新增充值记录";
    
    //广告主管理
    String OBJNAME_ADVERTISER = "广告主管理";
    String OPTYPE_ADVERTISER_FROZEN = "60001";
    String OPNAME_ADVERTISER_FROZEN = "冻结广告主";
    String OPTYPE_ADVERTISER_UNFROZEN = "60002";
    String OPNAME_ADVERTISER_UNFROZEN = "解冻广告主";
    
    //活动管理
    String OBJNAME_CAMPAIGN = "活动管理";
    String OPTYPE_CAMPAIGN_OPERATE = "70001";
    String OPNAME_CAMPAIGN_OPERATE = "状态管理";

    //广告组管理
    String OBJNAME_ADORDER = "广告组管理";
    String OPTYPE_ADORDER_OPERATE = "80001";
    String OPNAME_ADORDER_OPERATE = "广告组管理";
}
