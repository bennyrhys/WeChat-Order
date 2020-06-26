package com.bennyrhys.wechat_order.repository;

import com.bennyrhys.wechat_order.daoobject.ProductCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;


/**
 * @Author bennyrhys
 * @Date 2020-06-26 10:42
 */
@SpringBootTest
public class ProductCategoryRepositoryTest {
    @Autowired
    private ProductCategoryRepository repository;
//  查
    @Test
    public void findById(){
        ProductCategory productCategory = repository.findById(2).orElse(null);
        System.out.println(productCategory.toString());
    }
//    增
//    为保证数据库干净 操作完就回滚【@Transactional完全回滚。不像事务里，失败才回滚】
    @Test
    @Transactional
    public void saveOne() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("女生最爱");
        productCategory.setCategoryType(2);
        ProductCategory result = repository.save(productCategory);

//      断言
        if (result == null){
            Assert.isTrue(false,"null才抛异常");
        }


    }
//  更，共用save但是要指定主键

    @Test
    public void updateOne() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryId(2);
        productCategory.setCategoryName("女生最爱");
        productCategory.setCategoryType(2);
        repository.save(productCategory);
    }
//    场景模拟-先查出后修改
    @Test
    public void findAndSave(){
        ProductCategory productCategory = repository.findById(2).orElse(null);
        productCategory.setCategoryType(2);
        repository.save(productCategory);
    }

//    接口制定方法：类目编号数组查类目
    @Test
    public void findByCategoryTypeIn() {
        List<Integer> list = Arrays.asList(1,2,4);
        List<ProductCategory> result = repository.findByCategoryTypeIn(list);
//      断言不为0则成功
        Assert.notEmpty(result,"不为空数组");
    }
}