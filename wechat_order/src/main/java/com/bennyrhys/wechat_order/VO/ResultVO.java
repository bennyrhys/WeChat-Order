package com.bennyrhys.wechat_order.VO;

import lombok.Data;

/**
 * http请求返回的最外层对象
 * @Author bennyrhys
 * @Date 2020-06-27 19:56
 */
@Data
public class ResultVO<T> {
//    错误码
    private Integer code;
//    提示
    private String msg;
//    具体内容
    private T data;
}
