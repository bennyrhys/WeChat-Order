package com.bennyrhys.wechat_order.utils;

/**
 * 金额计算
 * @Author bennyrhys
 * @Date 2020-07-01 09:59
 */
public class MathUtil {

    private static final Double MONEY_RANGE = 0.01;

    /*比较两金额相减，判断金额一致*/
    public static boolean equals(Double d1, Double d2) {
        double result = Math.abs(d1 - d2);
        return (result < MONEY_RANGE) ? true : false;
    }
}
