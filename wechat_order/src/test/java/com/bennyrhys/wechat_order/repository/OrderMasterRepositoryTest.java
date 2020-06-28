package com.bennyrhys.wechat_order.repository;

import com.bennyrhys.wechat_order.daoobject.OrderMaster;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author bennyrhys
 * @Date 2020-06-28 08:27
 */
@SpringBootTest
class OrderMasterRepositoryTest {
    @Autowired
    OrderMasterRepository repository;

    private final String OPENID = "110110";

    @Test
    public void saveTest() {
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("12");
        orderMaster.setBuyerName("瑞新");
        orderMaster.setBuyerAddress("吉林通化");
        orderMaster.setBuyerOpenid(OPENID);
        orderMaster.setBuyerPhone("123456789123");
        orderMaster.setOrderAmount(new BigDecimal(8.8));

        OrderMaster result = repository.save(orderMaster);

        Assertions.assertNotNull(result);
    }

    /**
     * 分页查询一个用户的全部订单
     */
    @Test
    public void findByBuyerOpenid() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<OrderMaster> byBuyerOpenid = repository.findByBuyerOpenid(OPENID, pageRequest);
//        System.out.println(byBuyerOpenid.getTotalElements());

        Assertions.assertNotEquals(0, byBuyerOpenid.getTotalElements());
    }
}