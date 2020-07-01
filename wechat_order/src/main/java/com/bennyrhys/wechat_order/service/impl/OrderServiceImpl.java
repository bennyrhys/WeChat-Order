package com.bennyrhys.wechat_order.service.impl;

import com.bennyrhys.wechat_order.converter.OrderMaster2OrderDTOConverter;
import com.bennyrhys.wechat_order.daoobject.OrderDetail;
import com.bennyrhys.wechat_order.daoobject.OrderMaster;
import com.bennyrhys.wechat_order.daoobject.ProductInfo;
import com.bennyrhys.wechat_order.dto.CartDTO;
import com.bennyrhys.wechat_order.dto.OrderDTO;
import com.bennyrhys.wechat_order.enums.OrderStatusEnum;
import com.bennyrhys.wechat_order.enums.PayStatusEnum;
import com.bennyrhys.wechat_order.enums.ResultEnum;
import com.bennyrhys.wechat_order.exception.SellException;
import com.bennyrhys.wechat_order.repository.OrderDetailRepository;
import com.bennyrhys.wechat_order.repository.OrderMasterRepository;
import com.bennyrhys.wechat_order.service.OrderService;
import com.bennyrhys.wechat_order.service.PayService;
import com.bennyrhys.wechat_order.service.ProductSerice;
import com.bennyrhys.wechat_order.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单
 * @Author bennyrhys
 * @Date 2020-06-28 09:45
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductSerice productSerice;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private PayService payService;

    /**
     * 创建订单
     * 为订单-订单详情，一对多关系抽象出dto
     *
     * @param orderDTO
     */
    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        String orderId = KeyUtil.genUniqueKey();

//        List<CartDTO> cartDTOList = new ArrayList<>();

//        1. 查询商品（数量、金额）
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            ProductInfo productInfo = productSerice.findOne(orderDetail.getProductId());
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //        2. 计算订单总价 【注意productInfo才有价格】
            orderAmount =  productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);
            //      订单详情入库【注意：前端不会传来全部所需字段。随机数id】
            //      对象拷贝 spring提供的简便方法Info->Detail
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            orderDetailRepository.save(orderDetail);

//            CartDTO cartDTO = new CartDTO(orderDetail.getProductId(), orderDetail.getProductQuantity());
//            cartDTOList.add(cartDTO);
        }
//        3. 写入订单数据库（orderMaster、orderDetail）
        OrderMaster orderMaster = new OrderMaster();
        //       拷贝对象 【注意：属性为null也会被拷贝，调整顺序先拷贝】
        orderDTO.setOrderId(orderId); // 先设id
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        //      增加因拷贝被覆盖的默认状态值
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);


//        4. 扣库存 【库存一次修改，所以要获取购物车list 法1：上for cartDTO直接list.add 法2：lamdba】
//        并发下 redis锁机制防止超卖
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e ->
                new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productSerice.decreaseStock(cartDTOList);

        return orderDTO;
    }

    /**
     * 查询单个订单
     *
     * @param orderId
     */
    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findById(orderId).orElse(null);
        if (orderMaster == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;


    }

    /**
     * 查询订单列表
     * 注意：包名springframe
     *
     * @param buyerOpenId
     * @param pageable
     */
    @Override
    public Page<OrderDTO> findList(String buyerOpenId, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenId, pageable);
//        不查详情，也无需判空
//        创建类型转换DTO
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());

        Page<OrderDTO> orderDTOS = new PageImpl<OrderDTO>(orderDTOList, pageable, orderMasterPage.getTotalElements());
        return orderDTOS;
    }

    /**
     * 取消订单
     *
     * @param orderDTO
     */
    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
//        状态转换
        OrderMaster orderMaster = new OrderMaster();

//        判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【取消订单，订单状态不正确，orderId={}, orderStatus={}】",orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
//        修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster); // 注意状态修改时机
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.info("【取消订单，更新失败 orderMaster={}】",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
//        返还库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【取消订单，订单详情为空，orderDTO={}】",orderDTO);
            throw new SellException(ResultEnum.ORDER_UPDATE_EMPTY);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productSerice.increaseStock(cartDTOList);
//        如果已支付需要退款
        if(orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            payService.refund(orderDTO);
        }
        return orderDTO;
    }

    /**
     * 完结订单
     *
     * @param orderDTO
     */
    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
//        判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【完结订单：订单状态不正确 orderId={}, orderStatus={}】",orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
//        修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.info("【完结订单：更新失败 orderMaster={}】",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
    }

    /**
     * 支付订单
     *
     * @param orderDTO
     */
    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
//        判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【订单支付成功】订单状态不正确 orderId={}, orderStatus={}",orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
//        判断支付状态
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【订单支付成功】订单支付状态不正确 orderDTO={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);

        }

//        修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.info("【订单支付成功：更新失败 orderMaster={}】",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
    }
}
