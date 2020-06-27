package com.bennyrhys.wechat_order.enums;

import lombok.Getter;

/**
 * 商品状态
 * @Author bennyrhys
 * @Date 2020-06-27 17:00
 */
@Getter
public enum ProductStatusEnum {
    UP(0, "在架"),
    DOWN (1, "下架");

    private Integer code;
    private String msg;

    ProductStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
