package com.bennyrhys.wechat_order.daoobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 类目
 * 注意表名：idea中驼峰，sql表是_。springDataJpa做的映射。（如果非要自定义idea的表名,下边@Table，不建议）
 * @Author bennyrhys
 * @Date 2020-06-26 10:28
 */
//@Table(name = "s_my_table")
// 数据库映射成对象
@Entity
// 自动更新时间
@DynamicUpdate

//  每次属性类型改变不想重写get、set方法生成麻烦。那就全部去掉@Data代替【插件lombok，不会影响性能，打包时自动添加get、set】
@Data
public class ProductCategory {
//    字段命名也是对应_为驼峰
//    类目id【主键 自增指定生成策略】
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;
//    类目名称
    private String categoryName;
//    类目编号
    private Integer categoryType;
////    创建时间
//    private Date createTime;
////    更新时间
//    private Date updateTime;


    public ProductCategory() {
    }

    public ProductCategory(String categoryName, Integer categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }


//    get、set toString

}
