package com.bennyrhys.wechat_order.enums;

import lombok.Getter;

/**
 * 支付状态
 * @Author bennyrhys
 * @Date 2020-06-28 08:00
 */
@Getter
public enum PayStatusEnum {
    WAIT(0, "待支付"),
    SUCCESS(1, "支付成功")
    ;

    private Integer code;
    private String msg;

    PayStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
