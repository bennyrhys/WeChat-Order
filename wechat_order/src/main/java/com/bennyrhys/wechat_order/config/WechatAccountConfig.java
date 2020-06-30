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
    /*商户号*/
    private String mchId;
    /*商户秘钥*/
    private String mchKey;
    /*商户证书路径*/
    private String keyPath;
    /*微信异步通知*/
    private String notifyUrl;
}
