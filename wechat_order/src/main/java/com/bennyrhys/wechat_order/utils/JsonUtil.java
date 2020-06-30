package com.bennyrhys.wechat_order.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Json格式化工具
 * @Author bennyrhys
 * @Date 2020-06-30 20:05
 */
public class JsonUtil {
//    对象格式化为json格式
    public static String toJson(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }
}
