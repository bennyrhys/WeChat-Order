package com.bennyrhys.wechat_order.controller;

import com.bennyrhys.wechat_order.enums.ResultEnum;
import com.bennyrhys.wechat_order.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;

/**
 * 公众号获取openid-第三方sdk
 * @Author bennyrhys
 * @Date 2020-06-29 21:27
 */
//@RestController // 返回的是json，要页面跳转得用@Controller
@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {

    @Autowired
    private WxMpService wxMpService;

    /**
     * 网页授权
     * http://bennyrhys.mynatapp.cc/sell/wechat/authorize?returnUrl=http://www.imooc.com
     * @param returnUrl
     * @return
     */
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl) {
//        1. 公众号mp配置【公共config】
//        2. 调用方法 网页授权获取code【重定向url(配置项目地址+方法地址)，scope，state】
        String url = "http://bennyrhys.mynatapp.cc/sell/wechat/userInfo";
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(returnUrl));
        log.info("【微信网页授权】获取code，result={}", redirectUrl);

        return "redirect:"+redirectUrl;
    }

    /**
     * 获取用户信息-重定向的目的
     */
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                         @RequestParam("state") String returnUrl) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        }catch (WxErrorException e) {
            log.error("【微信网页授权】{}", e);
            throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getError().getErrorMsg());
        }
//        获取openid
        String openid = wxMpOAuth2AccessToken.getOpenId();
        return "redirect:" + returnUrl + "?openid=" + openid;
    }

}
