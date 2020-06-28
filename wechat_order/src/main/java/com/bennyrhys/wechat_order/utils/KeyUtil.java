package com.bennyrhys.wechat_order.utils;

import java.util.Random;

/**
 * 随机数生成
 * 用于无法自增的主键 key
 * @Author bennyrhys
 * @Date 2020-06-28 10:33
 */
public class KeyUtil {
    /**
     * 生成唯一主键
     * 格式：时间+随机数
     * 防止多线程
     */

    public static synchronized String genUniqueKey() {
//        六位定长
        Integer number = new Random().nextInt(900000)+100000;
        return System.currentTimeMillis() + String.valueOf(number);
    }
}
