package com.bennyrhys.wechat_order.repository;

import com.bennyrhys.wechat_order.daoobject.OrderDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单详情
 * @Author bennyrhys
 * @Date 2020-06-28 08:56
 */
@SpringBootTest
class OrderDetailRepositoryTest {
    @Autowired
    OrderDetailRepository repository;

    @Test
    public void saveTest() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setDetailId("2");
        orderDetail.setOrderId("11");
        orderDetail.setProductIcon("http://xxx.png");
        orderDetail.setProductId("11");
        orderDetail.setProductName("雪梨");
        orderDetail.setProductPrice(new BigDecimal(9.5));
        orderDetail.setProductQuantity(2);

        OrderDetail result = repository.save(orderDetail);

        Assertions.assertNotNull(result);
    }

    /**
     * 根据订单id查订单详情
     * 一对多
     */
    @Test
    public void findByOrderId() {
        List<OrderDetail> detailList = repository.findByOrderId("11");
        Assertions.assertNotEquals(0, detailList.size());
    }
}