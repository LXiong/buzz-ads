package com.buzzinate.buzzads.domain.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;



/**
 * 交易状态:0-成功下单，2-已发货，3-已签收，4-已退货，6-已完成，9-部分退换货，99-无订单状态
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2012-11-23
 */
public enum TradeStatusEnum implements IntegerValuedEnum {

    SUCCESS_TRADE(0), DISPATCH(2), SIGN_IN(3), SIGN_OUT(4), TRADE_COMPLETE(6), SOME_SIGN_OUT(
            9), NO_TRADE_STATUS(99);

    private int value;

    TradeStatusEnum(int value) {
        this.value = value;
    }

    @Override
    public int getCode() {
        return this.value;
    }
    
    public static TradeStatusEnum findByValue(int param) {
        TradeStatusEnum[] values = values();
        for (TradeStatusEnum e : values) {
            if (e.getCode() == param) {
                return e;
            }
        }
        return TradeStatusEnum.SUCCESS_TRADE;
    }
}
