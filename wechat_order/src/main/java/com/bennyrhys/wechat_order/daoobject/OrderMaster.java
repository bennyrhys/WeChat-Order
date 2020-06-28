package com.bennyrhys.wechat_order.daoobject;

import com.bennyrhys.wechat_order.enums.OrderStatusEnum;
import com.bennyrhys.wechat_order.enums.PayStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单表
 * @Author bennyrhys
 * @Date 2020-06-28 07:49
 */
@Entity
@Data
//    时间自动更新
@DynamicUpdate
public class OrderMaster {
//    订单id
    @Id
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
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();
//    支付状态,默认0未支付
    private Integer payStatus = PayStatusEnum.WAIT.getCode();
//    创建时间 【考虑到时间排序】
    private Date createTime;
//    更新时间
    private Date updateTime;

}
