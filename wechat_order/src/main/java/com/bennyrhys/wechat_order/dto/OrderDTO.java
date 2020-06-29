package com.bennyrhys.wechat_order.dto;

import com.bennyrhys.wechat_order.daoobject.OrderDetail;
import com.bennyrhys.wechat_order.utils.serializer.Date2LongSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单
 * 逻辑处理，数据传输的定制化字段
 *
 * 注意：@Id不用写了，增加的字段不用注解特殊忽略
 * @Author bennyrhys
 * @Date 2020-06-28 09:27
 */
@Data
// 保障不给前端返回为null的字段 【一个个配太傻，配置文件统一配】
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
    //    订单id
    private String orderId;
    //    买家名字
    private String buyerName;
    //    买家电话
    private String buyerPhone;
    //    买家地址
    private String buyerAddress;
    //    买家微信openid
    private String buyerOpenid;
    //    订单总金额
    private BigDecimal orderAmount;
    //    订单状态,默认0新下单
    private Integer orderStatus;
    //    支付状态,默认0未支付
    private Integer payStatus;
    //    创建时间 【考虑到时间排序】 @JsonSerialize 毫秒变秒
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;
    //    更新时间
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

//    【新增】一对多关系，订单详情列表。防止映射为空加 @Transient 过滤掉
    private List<OrderDetail> orderDetailList;
}
