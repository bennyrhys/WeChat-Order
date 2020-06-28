package com.bennyrhys.wechat_order.enums;

import lombok.Getter;

/**
 * 订单状态
 * @Author bennyrhys
 * @Date 2020-06-28 07:53
 */
@Getter
public enum OrderStatusEnum {
    NEW(0, "新订单"),
    FINISHED(1, "完结"),
    CANCEL(2, "取消"),
    ;
    private Integer code;
    private String msg;

    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
