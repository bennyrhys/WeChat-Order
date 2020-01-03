package com.bennyrhys.wechat_order;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WechatOrderApplicationTests {
//指定当前输出所对应的类
    private final Logger logger = LoggerFactory.getLogger(WechatOrderApplicationTests.class);
//每次这样写麻烦，用注解代替
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
    void contextLoads() {
        logger.info("info..............");
        logger.debug("debug..............");
        logger.error("error..............");
    }

}
