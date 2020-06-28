package com.bennyrhys.wechat_order.converter;

import com.bennyrhys.wechat_order.daoobject.OrderMaster;
import com.bennyrhys.wechat_order.dto.OrderDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 转换器
 * @Author bennyrhys
 * @Date 2020-06-28 12:49
 */
public class OrderMaster2OrderDTOConverter {
    public static OrderDTO convert(OrderMaster orderMaster) {
        OrderDTO orderDTO = new OrderDTO();

        BeanUtils.copyProperties(orderMaster, orderDTO);

        return orderDTO;
    }

    public static List<OrderDTO> convert(List<OrderMaster> orderMasterList) {
        return orderMasterList.stream().map(e ->
                convert(e))
                .collect(Collectors.toList());
    }

}

