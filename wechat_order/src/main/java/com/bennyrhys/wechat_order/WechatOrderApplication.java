package com.bennyrhys.wechat_order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication

public class WechatOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatOrderApplication.class, args);
//        SpringApplicationBuilder builder = new SpringApplicationBuilder(WechatOrderApplication.class);
//        SpringApplication build = builder.build();
//        build.setBannerMode(Banner.Mode.OFF);
//        build.run(args);
    }

}
