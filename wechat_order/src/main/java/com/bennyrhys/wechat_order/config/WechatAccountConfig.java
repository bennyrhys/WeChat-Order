package com.bennyrhys.wechat_order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信账号相关
 * 获取配置文件的openid
 * @Author bennyrhys
 * @Date 2020-06-30 07:23
 */

@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {

    private String mpAppId;
    private String mpAppSecret;
}
