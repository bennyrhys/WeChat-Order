package com.bennyrhys.wechat_order.service.impl;

import com.bennyrhys.wechat_order.daoobject.ProductCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author bennyrhys
 * @Date 2020-06-27 08:46
 */
@SpringBootTest
class CategorySericeImplTest {

    @Autowired
    CategorySericeImpl categorySerice;

    @Test
    void findOne() {
        ProductCategory one = categorySerice.findOne(1);
        Assertions.assertEquals(1, one.getCategoryId());
    }

    @Test
    void findAll() {
        List<ProductCategory> categoryList = categorySerice.findAll();
        Assertions.assertNotEquals(0,categoryList.size());
    }

    @Test
    void findByCategoryTypeIn() {
        List<ProductCategory> byCategoryTypeIn = categorySerice.findByCategoryTypeIn(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        Assertions.assertNotEquals(0, byCategoryTypeIn.size());
    }

    @Test
    void save() {
        ProductCategory category = new ProductCategory("男生专享", 3);
        ProductCategory result = categorySerice.save(category);
        Assertions.assertNotNull(result);
    }
}