package com.bennyrhys.wechat_order.service.impl;

import com.bennyrhys.wechat_order.daoobject.ProductInfo;
import com.bennyrhys.wechat_order.enums.ProductStatusEnum;
import com.bennyrhys.wechat_order.repository.ProductInfoRepository;
import com.bennyrhys.wechat_order.service.ProductSerice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品
 * @Author bennyrhys
 * @Date 2020-06-27 16:55
 */
@Service
public class ProductServiceImpl implements ProductSerice {
    @Autowired
    ProductInfoRepository repository;

    @Override
    public ProductInfo findOne(String productId) {
        return repository.findById(productId).orElse(null);
    }

    /**
     * 查询在架的商品-c端
     *
     * @return
     */
    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    /**
     * 查找全部商品
     *
     * @param pageable 分页 springframe domain的包
     * @return
     */
    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }
}
