package com.bennyrhys.wechat_order.exception;

import com.bennyrhys.wechat_order.enums.ResultEnum;

/**
 * 抛异常
 * @Author bennyrhys
 * @Date 2020-06-28 10:11
 */
public class SellException extends RuntimeException {
    private Integer code;


    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }
}
