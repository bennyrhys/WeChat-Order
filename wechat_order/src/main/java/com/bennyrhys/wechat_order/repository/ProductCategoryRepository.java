package com.bennyrhys.wechat_order.repository;

import com.bennyrhys.wechat_order.daoobject.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author bennyrhys
 * @Date 2020-06-26 10:40
 */
// 继承jpa<对象， 主键类型>
//    直接通过接口名创建测试类
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
//    注意需要按照指定格式
//    通过类目编号数组查询类目
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
