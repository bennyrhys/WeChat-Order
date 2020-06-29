package com.bennyrhys.wechat_order.service;

import com.bennyrhys.wechat_order.dto.OrderDTO;

/**
 * 买家
 * 为控制台提供安全性保证
 * @Author bennyrhys
 * @Date 2020-06-29 08:31
 */
public interface BuyerService {
//    查询一个订单
    OrderDTO findOrderOne(String openid, String orderId);
//    取消一个订单
    OrderDTO cancelOrder(String openid, String orderId);
}
