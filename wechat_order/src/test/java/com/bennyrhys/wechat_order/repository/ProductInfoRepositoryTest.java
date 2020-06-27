package com.bennyrhys.wechat_order.repository;

import com.bennyrhys.wechat_order.daoobject.ProductInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author bennyrhys
 * @Date 2020-06-27 09:50
 */
@SpringBootTest
class ProductInfoRepositoryTest {
    @Autowired
    ProductInfoRepository repository;
    @Test
    public void saveTest() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("11");
        productInfo.setCategoryType(1);
        productInfo.setProductName("衬衫");
        productInfo.setProductDescription("纯棉，超薄");
        productInfo.setProductIcon("http://xxxx.png");
        productInfo.setProductPrice(new BigDecimal(9.9));
        productInfo.setProductStock(100);
        productInfo.setProductStatus(0);

        ProductInfo result = repository.save(productInfo);
        Assertions.assertNotNull(result);
    }

//    测试上架商品
    @Test
    public void findByProductStatus(){
        List<ProductInfo> productInfoList = repository.findByProductStatus(0);
        Assertions.assertNotEquals(0, productInfoList.size());
    }

}