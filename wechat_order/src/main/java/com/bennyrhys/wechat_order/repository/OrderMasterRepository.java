package com.bennyrhys.wechat_order.repository;

import com.bennyrhys.wechat_order.daoobject.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 订单
 * @Author bennyrhys
 * @Date 2020-06-28 08:16
 */
public interface OrderMasterRepository extends JpaRepository<OrderMaster, String> {

    /**
     * 分页查询一个用户的全部订单
     * @param buyerOpenid
     * @param pageable
     * @return
     */
    Page<OrderMaster> findByBuyerOpenid(String buyerOpenid, Pageable pageable);
}
