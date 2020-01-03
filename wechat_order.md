[TOC]



# 概述

## 技术选型

- 页面
  - 前端：vue——webapp
  - 后端：springboot——bootstrap+freemarker+jquery

- 前端后端接口：restful

- 数据库springboot+jpa
  - springboot
  - mybits
- 缓存springboot+redis
  - 分布式session
  - 分布式锁
- 消息推送
  - websocket

## 核心业务

扫码登录

模版消息推送

微信支付/退款

## 配置

Idea2019

Java -version1.8.0.111

mvn -version3.9

Springboot2.2.2

centos7.3

mysql5.7

Nginx1.11.7

redis3.2.8

```xml
推荐：meaven镜像仓库
vim ~/.m2/settings.xml 
    <mirror>
      <id>alimaven</id>
      <name>aliyun maven</name>
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
      <mirrorOf>central</mirrorOf>
    </mirror>
    
<!--配置profiles节点-->
  <profiles>
  <profile>
            <id>jdk-1.8</id>
            <activation>
                <jdk>1.8</jdk>
            </activation>
            <repositories>
                <repository>
                    <id>nexus</id>
                    <name>local private nexus</name>
                    <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                </repository>
            </repositories>
         
   </profile> 
```

## 前置知识

先学习以下前置知识。如果还不了解，点击对应的实战内容，移步学习！记得star哦～

**meaven基础**

**springboot基础**

GitHub:[点击预览——极客浪漫红包表白程序](https://github.com/bennyrhys/LuckyMoney-SpringBootProject)

详细文档：[点击搜索——极客浪漫红包表白详解](https://bennyrhys.github.io/about/)

GitHub：[点击预览——基因芯片个人信息程序](https://github.com/bennyrhys/Girl-SpringBootProject)

详细文档：[点击搜索——基因芯片个人信息程序](https://bennyrhys.github.io/about/)

**javaweb基础**

GitHub：[点击预览——SpringBoot进阶基因芯片个人信息程序](https://github.com/bennyrhys/GirlPlus-SpringBootProject)

详细文档：[点击搜索——SpringBoot进阶基因芯片个人信息程序](https://bennyrhys.github.io/about/)

## 项目设计

- 角色划分

  - 买家（手机端：微信公众账号提供服务）
  - 卖家（pc端）

- 功能模块划分

  - 商品

    - 商品列表

  - 订单

    买家

    - 订单创建
    - 订单查询
    - 订单取消……

    卖家

    - 商品管理
    - 订单管理
    - 类目管理……

  - 类目 

- 部署架构

  - 卖家、买家请求服务器：NGINX在虚拟机中搭建好了
  - 前端资源获取：NGINX服务器
  - 后端资源获取：NGINX服务器-》tomcat（分布式） -〉做了缓存到redies，没做缓存到mysql 

- 数据库设计

  - 类目表（product_category）
  - 商品表（product_info）
  - 订单详情（order_detil）
  - 订单主表（order_master）
  - 卖家信息表（seller_info）

# 数据库创建

Mysql5.7,若5.6default current_timestamp有两个会报错

```sql
-- 商品表
create table `product_info`(
	`product_id` varchar(32) not null comment '商品id',
	`product_name` varchar(64) not null comment '商品名称',
	`product_price` decimal(8,2) not null comment '商品单价',
	`product_stock` int not null comment '商品库存',
	`product_description` varchar(64) comment '商品描述',
	`product_icon` varchar(512) comment '商品图',
	`category_type` int not null comment '类目编号',
	`create_time` timestamp not null default current_timestamp comment '创建时间',
	`update_time` timestamp not null default current_timestamp on update current_timestamp comment '更新时间',
	primary key (`product_id`)
)comment '商品表';
    `product_status` tinyint(3) DEFAULT '0' COMMENT '商品状态,0正常1下架',
-- 类目表
create table `product_category`(
	`category_id` int not null auto_increment comment '类目id',
	`category_name` varchar(64) not null comment '类目名称',
	`category_type` int not null comment '类目编号',
	`create_time` timestamp not null default current_timestamp comment '创建时间',
	`update_time` timestamp not null default current_timestamp on update current_timestamp comment '更新时间',
	primary key (`category_id`),
	unique key `uqe_category_type` (`category_type`)
) comment '类目表';
-- 订单表
create table `order_master`(
	`order_id` varchar(32) not null comment '订单id',
	`buyer_name` varchar(32) not null comment '买家名字',
	`buyer_phone` varchar(32) not null comment '买家电话',
	`buyer_address` varchar(128) not null comment '买家地址',
	`buyer_openid` varchar(64) not null comment '买家微信openid',
	`order_amount` decimal(8,2) not null comment '订单总金额',
	`order_status` tinyint(3) not null default '0' comment '订单状态,默认0新下单',
	`pay_status` tinyint(3) not null default '0' comment '支付状态,默认0未支付',
	`create_time` timestamp not null default current_timestamp comment '创建时间',
	`update_time` timestamp not null default current_timestamp on update current_timestamp comment '更新时间',
	primary key (`order_id`),
	key `idx_buyer_openid` (`buyer_openid`)
) comment '订单表';
-- 订单详情表
create table `order_detail`(
	`detail_id` varchar(32) not null comment '订单详情id',
	`order_id` varchar(32) not null comment '订单id',
	`product_id` varchar(32) not null comment '商品id',
	`product_name` varchar(64) not null comment '商品名称',
	`product_price` decimal(8,2) not null comment '商品价格',
	`product_quantity` int not null comment '商品数量',
	`product_icon` varchar(512) not null comment '商品图',
	`create_time` timestamp not null default current_timestamp comment '创建时间',
	`update_time` timestamp not null default current_timestamp on update current_timestamp comment '更新时间',
	primary key (`detail_id`),
	key `idx_order_id` (`order_id`)
) comment '订单详情表';
-- 卖家(登录后台使用, 卖家登录之后可能直接采用微信扫码登录，不使用账号密码)
create table `seller_info` (
    `id` varchar(32) not null,
    `username` varchar(32) not null,
    `password` varchar(32) not null,
    `openid` varchar(64) not null comment '微信openid',
    `create_time` timestamp not null default current_timestamp comment '创建时间',
    `update_time` timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (`id`)
) comment '卖家信息表';
```

# 虚拟机

## 概要

虚拟机配置好vue+mysql+redis环境，本次只专注后端开发。

我的虚拟机端口号192.168.210.131

访问端口号测试微信登录后台。

VirtualBox-5.1.22

虚拟机系统 centos7.3

账号 root

密码 123456

## 软件版本

* jdk 1.8.0_111
* nginx 1.11.7
* mysql 5.7.17
* redis 3.2.8

## 路径及启动

jdk

* 路径 /usr/local/jdk1.8.0_111

nginx

* 路径 /usr/local/nginx
* 启动 nginx
* 重启 nginx -s reload

mysql

* 配置 /etc/my.conf
* 账号 root
* 密码 123456
* 端口 3306
* 启动 systemctl start mysqld
* 停止 systemctl stop mysqld

redis

* 路径 /usr/local/redis
* 配置 /etc/reis.conf
* 端口 6379
* 密码 123456
* 启动 systemctl start redis
* 停止 systemctl stop redis

tomcat

* 路径 /usr/local/tomcat
* 启动 systemctl start tomcat
* 停止 systemctl stop tomcat

# 日志框架

## 概述

能够实现日志输出的工具包，描述运行状态的所有时间。

例如：用户下线，接口超时，数据库崩溃

## 能力

定制输出目标

定制输出格式

携带上下文信息

运行时选择性输出

灵活配置-运维

## 对比

jdk自带logging jul

apach自带clogging jcl

log4j

log4j2

logback

slf4j

hibinate的jboss-logging

| 日志门面                                        | 日志实现                                          |
| ----------------------------------------------- | ------------------------------------------------- |
| JCL                                             | Log4j（作者ceki，太烂了作者不想改）               |
| SLF4j（作者ceki ）                              | Log4j2（apach借名，优于Logout性能， 先进坑多）    |
| Jobs-logging（诞生就不是为服务大众，不受亲睐 ） | Logout（作者ceki）                                |
|                                                 | Jul（虽然官方，但实现简陋，多方面受到开发者吐槽） |

**推荐：日志门面SLF4j， 日志实现logback**

## 测试日志-简单版

```java
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
```



## API



###商品列表

```
GET /sell/buyer/product/list
```

参数

```
无
```

返回

```
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "name": "热榜",
            "type": 1,
            "foods": [
                {
                    "id": "123456",
                    "name": "皮蛋粥",
                    "price": 1.2,
                    "description": "好吃的皮蛋粥",
                    "icon": "http://xxx.com",
                }
            ]
        },
        {
            "name": "好吃的",
            "type": 2,
            "foods": [
                {
                    "id": "123457",
                    "name": "慕斯蛋糕",
                    "price": 10.9,
                    "description": "美味爽口",
                    "icon": "http://xxx.com",
                }
            ]
        }
    ]
}
```


###创建订单

```
POST /sell/buyer/order/create
```

参数

```
name: "张三"
phone: "18868822111"
address: "慕课网总部"
openid: "ew3euwhd7sjw9diwkq" //用户的微信openid
items: [{
    productId: "1423113435324",
    productQuantity: 2 //购买数量
}]

```

返回

```
{
  "code": 0,
  "msg": "成功",
  "data": {
      "orderId": "147283992738221" 
  }
}
```

###订单列表

```
GET /sell/buyer/order/list
```

参数

```
openid: 18eu2jwk2kse3r42e2e
page: 0 //从第0页开始
size: 10
```

返回

```
{
  "code": 0,
  "msg": "成功",
  "data": [
    {
      "orderId": "161873371171128075",
      "buyerName": "张三",
      "buyerPhone": "18868877111",
      "buyerAddress": "慕课网总部",
      "buyerOpenid": "18eu2jwk2kse3r42e2e",
      "orderAmount": 0,
      "orderStatus": 0,
      "payStatus": 0,
      "createTime": 1490171219,
      "updateTime": 1490171219,
      "orderDetailList": null
    },
    {
      "orderId": "161873371171128076",
      "buyerName": "张三",
      "buyerPhone": "18868877111",
      "buyerAddress": "慕课网总部",
      "buyerOpenid": "18eu2jwk2kse3r42e2e",
      "orderAmount": 0,
      "orderStatus": 0,
      "payStatus": 0,
      "createTime": 1490171219,
      "updateTime": 1490171219,
      "orderDetailList": null
    }]
}
```

###查询订单详情

```
GET /sell/buyer/order/detail

```

参数

```
openid: 18eu2jwk2kse3r42e2e
orderId: 161899085773669363

```

返回

```
{
    "code": 0,
    "msg": "成功",
    "data": {
          "orderId": "161899085773669363",
          "buyerName": "李四",
          "buyerPhone": "18868877111",
          "buyerAddress": "慕课网总部",
          "buyerOpenid": "18eu2jwk2kse3r42e2e",
          "orderAmount": 18,
          "orderStatus": 0,
          "payStatus": 0,
          "createTime": 1490177352,
          "updateTime": 1490177352,
          "orderDetailList": [
            {
                "detailId": "161899085974995851",
                "orderId": "161899085773669363",
                "productId": "157875196362360019",
                "productName": "招牌奶茶",
                "productPrice": 9,
                "productQuantity": 2,
                "productIcon": "http://xxx.com",
                "productImage": "http://xxx.com"
            }
        ]
    }
}

```

###取消订单

```
POST /sell/buyer/order/cancel

```

参数

```
openid: 18eu2jwk2kse3r42e2e
orderId: 161899085773669363

```

返回

```
{
    "code": 0,
    "msg": "成功",
    "data": null
}

```

###获取openid

```
重定向到 /sell/wechat/authorize

```

参数

```
returnUrl: http://xxx.com/abc  //【必填】

```

返回

```
http://xxx.com/abc?openid=oZxSYw5ldcxv6H0EU67GgSXOUrVg

```

## 原本API

###商品列表

```
GET /sell/buyer/product/list
```

参数

```
无
```

返回

```
{
    "code": 0,
    "msg": "成功",
    "data": [
        {
            "name": "热榜",
            "type": 1,
            "foods": [
                {
                    "id": "123456",
                    "name": "皮蛋粥",
                    "price": 1.2,
                    "description": "好吃的皮蛋粥",
                    "icon": "http://xxx.com",
                }
            ]
        },
        {
            "name": "好吃的",
            "type": 2,
            "foods": [
                {
                    "id": "123457",
                    "name": "慕斯蛋糕",
                    "price": 10.9,
                    "description": "美味爽口",
                    "icon": "http://xxx.com",
                }
            ]
        }
    ]
}
```


###创建订单

```
POST /sell/buyer/order/create
```

参数

```
name: "张三"
phone: "18868822111"
address: "慕课网总部"
openid: "ew3euwhd7sjw9diwkq" //用户的微信openid
items: [{
    productId: "1423113435324",
    productQuantity: 2 //购买数量
}]

```

返回

```
{
  "code": 0,
  "msg": "成功",
  "data": {
      "orderId": "147283992738221" 
  }
}
```

###订单列表

```
GET /sell/buyer/order/list
```

参数

```
openid: 18eu2jwk2kse3r42e2e
page: 0 //从第0页开始
size: 10
```

返回

```
{
  "code": 0,
  "msg": "成功",
  "data": [
    {
      "orderId": "161873371171128075",
      "buyerName": "张三",
      "buyerPhone": "18868877111",
      "buyerAddress": "慕课网总部",
      "buyerOpenid": "18eu2jwk2kse3r42e2e",
      "orderAmount": 0,
      "orderStatus": 0,
      "payStatus": 0,
      "createTime": 1490171219,
      "updateTime": 1490171219,
      "orderDetailList": null
    },
    {
      "orderId": "161873371171128076",
      "buyerName": "张三",
      "buyerPhone": "18868877111",
      "buyerAddress": "慕课网总部",
      "buyerOpenid": "18eu2jwk2kse3r42e2e",
      "orderAmount": 0,
      "orderStatus": 0,
      "payStatus": 0,
      "createTime": 1490171219,
      "updateTime": 1490171219,
      "orderDetailList": null
    }]
}
```

###查询订单详情

```
GET /sell/buyer/order/detail
```

参数

```
openid: 18eu2jwk2kse3r42e2e
orderId: 161899085773669363
```

返回

```
{
    "code": 0,
    "msg": "成功",
    "data": {
          "orderId": "161899085773669363",
          "buyerName": "李四",
          "buyerPhone": "18868877111",
          "buyerAddress": "慕课网总部",
          "buyerOpenid": "18eu2jwk2kse3r42e2e",
          "orderAmount": 18,
          "orderStatus": 0,
          "payStatus": 0,
          "createTime": 1490177352,
          "updateTime": 1490177352,
          "orderDetailList": [
            {
                "detailId": "161899085974995851",
                "orderId": "161899085773669363",
                "productId": "157875196362360019",
                "productName": "招牌奶茶",
                "productPrice": 9,
                "productQuantity": 2,
                "productIcon": "http://xxx.com",
                "productImage": "http://xxx.com"
            }
        ]
    }
}
```

###取消订单

```
POST /sell/buyer/order/cancel
```

参数

```
openid: 18eu2jwk2kse3r42e2e
orderId: 161899085773669363
```

返回

```
{
    "code": 0,
    "msg": "成功",
    "data": null
}
```

###获取openid

```
重定向到 /sell/wechat/authorize
```

参数

```
returnUrl: http://xxx.com/abc  //【必填】
```

返回

```
http://xxx.com/abc?openid=oZxSYw5ldcxv6H0EU67GgSXOUrVg
```

###支付订单

```
重定向 /sell/pay/create
```

参数

```
orderId: 161899085773669363
returnUrl: http://xxx.com/abc/order/161899085773669363
```

返回

```
http://xxx.com/abc/order/161899085773669363
```

