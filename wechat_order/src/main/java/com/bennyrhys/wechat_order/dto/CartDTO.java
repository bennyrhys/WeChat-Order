package com.bennyrhys.wechat_order.dto;

import lombok.Data;

/**
 * 购物车
 * @Author bennyrhys
 * @Date 2020-06-28 11:00
 */
@Data
public class CartDTO {
    private String productId;
    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
