package com.bennyrhys.wechat_order.service.impl;

import com.bennyrhys.wechat_order.daoobject.ProductInfo;
import com.bennyrhys.wechat_order.enums.ProductStatusEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品
 * @Author bennyrhys
 * @Date 2020-06-27 17:05
 */
@SpringBootTest
class ProductServiceImplTest {

    @Autowired
     private ProductServiceImpl productSerice;

    @Test
    void findOne() {
        ProductInfo productInfo = productSerice.findOne("11");
        Assertions.assertEquals("11", productInfo.getProductId());
    }

    @Test
    void findUpAll() {
        List<ProductInfo> productInfoList = productSerice.findUpAll();
        Assertions.assertNotEquals(0, productInfoList.size());
    }

    @Test
    void findAll() {
//        配置PageAble对象
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<ProductInfo> productInfos = productSerice.findAll(pageRequest);
//        System.out.println(productInfos.getTotalElements());
        Assertions.assertNotEquals(0, productInfos.getTotalElements());
    }

    @Test
    void save() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("22");
        productInfo.setCategoryType(1);
        productInfo.setProductName("皮蛋瘦肉粥");
        productInfo.setProductDescription("超甜，很好喝");
        productInfo.setProductIcon("http://xxxx.png");
        productInfo.setProductPrice(new BigDecimal(5.9));
        productInfo.setProductStock(100);
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());

        ProductInfo result = productSerice.save(productInfo);
        Assertions.assertNotNull(result);
    }

}