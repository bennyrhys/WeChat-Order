package com.bennyrhys.wechat_order.utils;

import com.bennyrhys.wechat_order.VO.ResultVO;

/**
 * 通用返回消息
 * @Author bennyrhys
 * @Date 2020-06-27 21:24
 */
public class ResultVOUtil {
    public static ResultVO success(Object object) {
        ResultVO<Object> resultVO = new ResultVO<>();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO success() {
        return success(null);
    }

    public static ResultVO error(Integer code, String msg) {
        ResultVO<Object> resultVO = new ResultVO<>();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
}
