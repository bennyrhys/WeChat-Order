package com.bennyrhys.wechat_order.service;

import com.bennyrhys.wechat_order.dto.OrderDTO;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;

/**
 * 支付
 * @Author bennyrhys
 * @Date 2020-06-30 16:43
 */
public interface PayService {
    PayRequest create(OrderDTO orderDTO);

    PayResponse notify(String notifyData);
}
