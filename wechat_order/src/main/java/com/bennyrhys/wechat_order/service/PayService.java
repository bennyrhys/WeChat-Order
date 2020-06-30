package com.bennyrhys.wechat_order.service;

import com.bennyrhys.wechat_order.dto.OrderDTO;
import com.lly835.bestpay.model.PayRequest;

/**
 * 支付
 * @Author bennyrhys
 * @Date 2020-06-30 16:43
 */
public interface PayService {
    PayRequest create(OrderDTO orderDTO);
}
