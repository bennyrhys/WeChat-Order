package com.bennyrhys.wechat_order.enums;

import lombok.Getter;

/**
 * 返回给前端提示的异常消息
 * @Author bennyrhys
 * @Date 2020-06-28 10:12
 */
@Getter
public enum ResultEnum {
    PARAM_ERROR(1, "参数不正确"),
    PRODUCT_NOT_EXIST(10, "商品不存在"),
    PRODUCT_STOCK_ERROR(11,"库存不正确"),
    ORDER_NOT_EXIST(12, "订单为空"),
    ORDERDETAIL_NOT_EXIST(13, "订单详情为空"),
    ORDER_STATUS_ERROR(14, "订单状态不正确"),
    ORDER_UPDATE_FAIL(15,"订单更新失败"),
    ORDER_UPDATE_EMPTY(16,"订单详情为空"),
    ORDER_PAY_STATUS_ERROR(17, "订单支付状态不正确"),
    CART_EMPTY(18, "购物车不能为空"),
    ORDER_OWNER_ERROR(19, "该订单不属于当前用户"),
    WECHAT_MP_ERROR(20, "微信公众账户方面错误"),

    ;

    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
