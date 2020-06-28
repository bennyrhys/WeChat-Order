package com.bennyrhys.wechat_order.enums;

import lombok.Getter;

/**
 * 返回给前端提示的异常消息
 * @Author bennyrhys
 * @Date 2020-06-28 10:12
 */
@Getter
public enum ResultEnum {
    PRODUCT_NOT_EXIST(10, "商品不存在"),
    PRODUCT_STOCK_ERROR(11,"库存不正确")
    ;

    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
