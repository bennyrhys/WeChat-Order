package com.bennyrhys.wechat_order.service.impl;

import com.bennyrhys.wechat_order.daoobject.ProductCategory;
import com.bennyrhys.wechat_order.repository.ProductCategoryRepository;
import com.bennyrhys.wechat_order.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类目
 * @Author bennyrhys
 * @Date 2020-06-26 16:16
 */
@Service
public class CategorySericeImpl implements CategoryService {
    @Autowired
    ProductCategoryRepository repository;

    @Override
    public ProductCategory findOne(Integer categoryId) {
        return repository.findById(categoryId).orElse(null);
    }

    @Override
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        return repository.findByCategoryTypeIn(categoryTypeList);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return repository.save(productCategory);
    }
}
