package com.bennyrhys.wechat_order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author bennyrhys
 * @Date 2020-06-24 22:12
 */
@SpringBootTest
@Slf4j
public class LoggerTest {

    /**
     * 指定当前输出所对应的类
     * 每次这样写麻烦，用注解代替
     *
     * 引入@slf4j 包名lambok @Data也是lambok，pom先引入lombok小插件 log代替logger
     * 不仅在类的get\set方法带来很大便捷，日志使用也很便捷
     */
//    private final Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    /**
     * 测试-日志
     * 快捷键：
     *  看级别comment+n，搜索level找slf4j
     *     ERROR(40, "ERROR"),
     *     WARN(30, "WARN"),
     *     INFO(20, "INFO"),
     *     DEBUG(10, "DEBUG"),
     *     TRACE(0, "TRACE");
     */
    @Test
    public void test1(){
//        logger.debug("debug...");
//        logger.info("info...");
//        logger.error("error...");

//        引入@Slf4j后
        log.debug("debug...");
        log.info("info...");
        log.error("error...");

//        引入变量 【建议使用{}占位符】
        String name = "bennyrhys";
        String tell = "158";
        log.info("name:" + name + ", tell:" + tell);
        log.info("name:{}, tell:{}", name, tell);
    }
}
