package com.bennyrhys.wechat_order.service;

import com.bennyrhys.wechat_order.daoobject.ProductCategory;

import java.util.List;

/**
 * 类目
 * @Author bennyrhys
 * @Date 2020-06-26 16:12
 */
public interface CategoryService {
    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);

}
