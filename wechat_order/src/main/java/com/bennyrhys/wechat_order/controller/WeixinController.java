package com.bennyrhys.wechat_order.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 公众号手动获取openid
 * @Author bennyrhys
 * @Date 2020-06-29 16:27
 */
@RestController
@RequestMapping("/weixin")
@Slf4j
public class WeixinController {
    @GetMapping("/auth")
    public void auth(@RequestParam("code") String code) {
        log.info("进入微信授权。。。");
        log.info("code={}",code);

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxc10086806f4c0df0&secret=bdcb164ea61b4b1cc6846cd549325898&code="+ code +"&grant_type=authorization_code";
        // 接收 json
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        log.info("response={}",response);
//        oWBZiwSvnfae70D6wESTWbc0cDi4
    }

    @GetMapping("/test")
    public String test() {
        return "lai";
    }

}
