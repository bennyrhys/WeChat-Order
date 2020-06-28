package com.bennyrhys.wechat_order.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 买家创建订单
 * 表单检验
 * @Author bennyrhys
 * @Date 2020-06-28 15:41
 */
@Data
public class OrderForm {
    /**
     * 买家姓名
     */
    @NotEmpty(message = "姓名必填")
    private String name;

    /**
     * 买家手机号
     */
    @NotEmpty(message = "手机号必填")
    private String phone;

    /**
     * 买家地址
     */
    @NotEmpty(message = "买家地址")
    private String address;

    /**
     * 买家微信openId
     */
    @NotEmpty(message = "微信openId必填")
    private String openid;

    /**
     * 购物车
     */
    @NotEmpty(message = "购物车不能为空")
    private String items;
}
