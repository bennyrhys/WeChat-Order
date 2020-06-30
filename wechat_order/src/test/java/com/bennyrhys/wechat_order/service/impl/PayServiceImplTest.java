package com.bennyrhys.wechat_order.service.impl;

import com.bennyrhys.wechat_order.dto.OrderDTO;
import com.bennyrhys.wechat_order.service.OrderService;
import com.bennyrhys.wechat_order.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 微信支付
 * @Author bennyrhys
 * @Date 2020-06-30 19:48
 */
@SpringBootTest
@Slf4j
class PayServiceImplTest {

    @Autowired
    private PayService payService;
    @Autowired
    private OrderService orderService;

    @Test
    void create() {
        OrderDTO orderDTO = orderService.findOne("1593336982853846059");
        payService.create(orderDTO);
    }
}