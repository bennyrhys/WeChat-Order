package com.bennyrhys.wechat_order.daoobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * 商品
 * @Author bennyrhys
 * @Date 2020-06-27 09:34
 */
@Entity
@Data
public class ProductInfo {
    @Id
//    商品id
    private String productId;
//    商品名称
    private String productName;
//    商品单价
    private BigDecimal productPrice;
//    商品库存
    private Integer productStock;
//    商品描述
    private String productDescription;
//    商品小图
    private String productIcon;
//    类目编号
    private Integer categoryType;
//    商品状态,0正常1下架
    private Integer productStatus;
}

