package com.bennyrhys.wechat_order.repository;

import com.bennyrhys.wechat_order.daoobject.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author bennyrhys
 * @Date 2020-06-27 09:43
 */
public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {
    /**
     * 查询上架商品
     */
    List<ProductInfo> findByProductStatus(Integer productStatus);
}

