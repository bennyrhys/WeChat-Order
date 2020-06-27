package com.bennyrhys.wechat_order.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 商品类目
 * @Author bennyrhys
 * @Date 2020-06-27 20:22
 */
@Data
public class ProductVO {
    @JsonProperty("name")
    private String productName;

    @JsonProperty("type")
    private Integer productType;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;
}
