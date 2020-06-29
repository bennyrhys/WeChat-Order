package com.bennyrhys.wechat_order.service.impl;

import com.bennyrhys.wechat_order.dto.OrderDTO;
import com.bennyrhys.wechat_order.enums.ResultEnum;
import com.bennyrhys.wechat_order.exception.SellException;
import com.bennyrhys.wechat_order.service.BuyerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 买家
 * 为控制台提供安全性保证
 * @Author bennyrhys
 * @Date 2020-06-29 08:33
 */
@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    OrderServiceImpl orderService;

    @Override
    public OrderDTO findOrderOne(String openid, String orderId) {
        return checkOrderOwner(openid, orderId);
    }

    @Override
    public OrderDTO cancelOrder(String openid, String orderId) {
        OrderDTO orderDTO = checkOrderOwner(openid, orderId);
        if (orderDTO == null) {
            log.error("【取消订单】查不到该订单。orderId={}", orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        return orderService.cancel(orderDTO);
    }

    /**
     * 判断是否是自己的订单，通过openid
     */
    public OrderDTO checkOrderOwner(String openid, String orderId) {
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null) {
            return  null;
        }
//       判断是否是自己的订单，通过openid
        if(!orderDTO.getBuyerOpenid().equalsIgnoreCase(openid)) {
            log.error("【查询订单】订单的openid不一致。openid={},orderId", openid, orderId);
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return orderDTO;
    }
}
