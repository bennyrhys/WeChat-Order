package com.bennyrhys.wechat_order.service;

import com.bennyrhys.wechat_order.daoobject.ProductInfo;
import com.bennyrhys.wechat_order.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商品
 * @Author bennyrhys
 * @Date 2020-06-27 16:49
 */
public interface ProductSerice {
    ProductInfo findOne(String productId);

    /**
     * 查询在架的商品-c端
     * @return
     */
    List<ProductInfo> findUpAll();

    /**
     * 查找全部商品
     * @param pageable 分页 springframe domain的包
     * @return
     */
    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    /**
     * 加库存 【传入抽象的 购物车 信息】
     * @param cartDTOList
     */
    void increaseStock(List<CartDTO> cartDTOList);

    /**
     * 减库存
     */
    void decreaseStock(List<CartDTO> cartDTOList);



}
