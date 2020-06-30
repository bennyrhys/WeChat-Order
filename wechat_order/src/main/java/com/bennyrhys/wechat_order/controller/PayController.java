package com.bennyrhys.wechat_order.controller;

import com.bennyrhys.wechat_order.dto.OrderDTO;
import com.bennyrhys.wechat_order.enums.ResultEnum;
import com.bennyrhys.wechat_order.exception.SellException;
import com.bennyrhys.wechat_order.service.OrderService;
import com.bennyrhys.wechat_order.service.PayService;
import com.lly835.bestpay.model.PayRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 支付
 * @Author bennyrhys
 * @Date 2020-06-30 16:30
 */

@Controller
@RequestMapping("pay")
@Slf4j
public class PayController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private PayService payService;

    @GetMapping("create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl,
                               Map<String,Object> map){
//      1. 查询订单
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null) {
            log.info("【支付】订单为空");
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

//      2. 发起支付【支付逻辑写到paySerive】
        PayRequest payRequest = payService.create(orderDTO);
//      参数注入模板动态模板
        map.put("orderId", "1111");
        map.put("payRequest",payRequest);
        map.put("returnUrl", returnUrl);
        return new ModelAndView("pay/create", map);
    }
}
