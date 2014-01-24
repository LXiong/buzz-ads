package com.buzzinate.buzzads.enums;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;


/**
 * 订单来源枚举
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2012-11-22
 */
public enum OrderSourceEnum implements IntegerValuedEnum {
    //本系统
    BUZZ(0),
    //成果网
    CHANET(1);
    private int value;

    OrderSourceEnum(int value) {
        this.value = value;
    }

    @Override
    public int getCode() {
        return this.value;
    }
    
    public static OrderSourceEnum findByValue(int param) {
        OrderSourceEnum[] values = values();
        for (OrderSourceEnum e : values) {
            if (e.getCode() == param) {
                return e;
            }
        }
        return OrderSourceEnum.BUZZ;
    }

}
