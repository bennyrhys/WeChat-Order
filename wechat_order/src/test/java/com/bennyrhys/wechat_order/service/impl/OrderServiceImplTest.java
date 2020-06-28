package com.bennyrhys.wechat_order.service.impl;

import com.bennyrhys.wechat_order.daoobject.OrderDetail;
import com.bennyrhys.wechat_order.dto.OrderDTO;
import com.bennyrhys.wechat_order.enums.OrderStatusEnum;
import com.bennyrhys.wechat_order.enums.PayStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单
 * @Author bennyrhys
 * @Date 2020-06-28 11:26
 */
@SpringBootTest
@Slf4j
class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    private final String BUYER_OPENID = "110110";
    private final String ORDER_ID = "1593317227294809400";

    @Test
    void create() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("瑞新");
        orderDTO.setBuyerAddress("吉林通化");
        orderDTO.setBuyerPhone("123456789123");
        orderDTO.setBuyerOpenid(BUYER_OPENID);

//        购物车
        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail od = new OrderDetail();
        od.setProductId("11");
        od.setProductQuantity(1);
        orderDetailList.add(od);

        OrderDetail od2 = new OrderDetail();
        od2.setProductId("22");
        od2.setProductQuantity(2);
        orderDetailList.add(od2);

        orderDTO.setOrderDetailList(orderDetailList);

        OrderDTO result = orderService.create(orderDTO);

//        log.info("【创建订单】result={}", result);
        Assertions.assertNotNull(result);
    }

    @Test
    void findOne() {
        OrderDTO result = orderService.findOne(ORDER_ID);

        log.info("【单个订单】result={}", result);
        Assertions.assertEquals(ORDER_ID, result.getOrderId());
    }

    @Test
    void findList() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<OrderDTO> orderDTOPage =  orderService.findList(BUYER_OPENID,pageRequest);
        Assertions.assertNotEquals(0, orderDTOPage.getTotalElements());
    }

    @Test
    void cancel() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.cancel(orderDTO);

        Assertions.assertEquals(OrderStatusEnum.CANCEL.getCode(), result.getOrderStatus());
    }

    @Test
    void finish() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.finish(orderDTO);

        Assertions.assertEquals(OrderStatusEnum.FINISHED.getCode(), result.getOrderStatus());
    }

    @Test
    void paid() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.paid(orderDTO);

        Assertions.assertEquals(PayStatusEnum.SUCCESS.getCode(), result.getPayStatus());
    }

}