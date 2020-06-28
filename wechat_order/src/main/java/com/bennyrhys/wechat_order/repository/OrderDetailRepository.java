package com.bennyrhys.wechat_order.repository;

import com.bennyrhys.wechat_order.daoobject.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 订单详情
 * @Author bennyrhys
 * @Date 2020-06-28 08:20
 */
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    /**
     * 根据订单id查询订单详情
     * 一对多
     * @param orderId
     * @return
     */
    List<OrderDetail> findByOrderId(String orderId);
}
