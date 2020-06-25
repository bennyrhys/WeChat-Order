[TOC]

作者：bennyrhys@163.com

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
	`product_icon` varchar(512) comment '商品小图',
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
	`product_icon` varchar(512) not null comment '商品小图',
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

运行虚拟机：保证虚拟机和主机相互ping通

联通虚拟机中的数据库：创建数据库选择utf-8mb4(存微信聊天的表情)

## 配置环境

虚拟机配置好vue+mysql+redis环境，本次只专注后端开发。

`ifconfig`我的虚拟机端口号192.168.210.132

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

# 本地环境

Jdk 1.8

Maven 3.3.9

使用阿里云的镜像地址会快点`~ vim .m2/settings.xml`

```xml
    <mirror>
      <id>alimaven</id>
      <name>aliyun maven</name>
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
      <mirrorOf>central</mirrorOf>
    </mirror>
  </mirrors>
```

开发idea

# 日志框架

## 概述

能够实现日志输出的工具包，描述运行状态的所有时间。

例如：用户下线，接口超时，数据库崩溃



定制输出目标

定制输出格式

携带上下文信息

运行时选择性输出

灵活配置-运维

优秀性能 

## 对比

jdk自带logging jul

apach自带clogging jcl

log4j

log4j2

logback

slf4j

hibinate的jboss-logging

| 日志门面                                        | 日志实现                                                    |
| ----------------------------------------------- | ----------------------------------------------------------- |
| JCL                                             | Log4j（作者ceki，太烂了作者不想改）                         |
| SLF4j（作者ceki ）                              | Log4j2（apach借名，优于Logback性能， 因为太先进，所以坑多） |
| Jobs-logging（诞生就不是为服务大众，不受亲睐 ） | Logback（作者ceki）                                         |
|                                                 | Jul（虽然官方，但实现简陋，多方面受到开发者吐槽）           |

**推荐：日志门面SLF4j， 日志实现logback**

## 测试日志-简版

传入包名创建日志工厂

引入lambol的@Slf4j直接log.

打印变量

```java
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
```

pom.xml

```xml
<!--        添加小工具 日志方便，get、set方便-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
```

## logbak配置

application.yml只能简单配置（日志路径、格式）

logbak-spring.xml可以复杂配置（区分info和error日志，每天一个日志）

注意：可以通过springframeworkjar包org-logging-logback下默认配置引用信息

**application.yml**

```yml
# 日志配置
logging:
  pattern:
    console: "%d - %msg%n"
#  level:
#    com.bennyrhys.wechat_order.LoggerTest: debug

#  level: debug # 直接指定级别
#  path: /Users/bennyrhys/Documents/Idea_Demo/WeChat-Order/wechat_order/src/main/resources/ # 弃用了
#  file: /Users/bennyrhys/Documents/Idea_Demo/WeChat-Order/wechat_order/src/main/resources/sell.log # 弃用了
```

**logback-spring.xml**

```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
<!--    控制台输出-->
    <appender name = "consoleLog" class="ch.qos.logback.core.ConsoleAppender">
        <layout class="ch.qos.logback.classic.PatternLayout">
            <pattern>
                %d - %msg%n
            </pattern>
        </layout>
    </appender>
<!--    文件info -->
    <appender name="fileInfoLog" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!--        过滤级别 只保留,条件等级比对过滤
                    public enum FilterReply {
                        DENY, //禁止
                        NEUTRAL, // 忽略
                        ACCEPT; // 接受
        -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>DENY</onMatch>
            <onMismatch>ACCEPT</onMismatch>
        </filter>
        <encoder>
            <pattern>
                %msg%n
            </pattern>
        </encoder>
<!--        滚动策略 每天输出一个文件+路径-->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
<!--            路径-->
            <fileNamePattern>/Users/bennyrhys/Documents/Idea_Demo/WeChat-Order/wechat_order/src/main/resources/info.%d.log</fileNamePattern>
        </rollingPolicy>
    </appender>
    <!--    文件error -->
    <appender name="fileErrorLog" class="ch.qos.logback.core.rolling.RollingFileAppender">
<!--        过滤级别 只保留范围：比当前范围大-->
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>ERROR</level>
        </filter>
        <encoder>
            <pattern>
                %msg%n
            </pattern>
        </encoder>
        <!--        滚动策略 每天输出一个文件+路径-->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--            路径-->
            <fileNamePattern>/Users/bennyrhys/Documents/Idea_Demo/WeChat-Order/wechat_order/src/main/resources/error.%d.log</fileNamePattern>
        </rollingPolicy>
    </appender>

<!--级别-->
    <root level="INFO">
        <appender-ref ref="consoleLog" />
        <appender-ref ref="fileInfoLog" />
        <appender-ref ref="fileErrorLog" />
    </root>

</configuration>
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

# 1.x-2.x版本升级

jpa

findOne->findById返回值Optional对象，

暂时解决抛红.get()。之前查不到返回null但是.get（）会抛异常

.orElse（null），指定查不到返回null



配置不能用驼峰

projectUrl

Project-url



projrctService

findOne()可以添加图片到又拍云。

写法视频13.42



数据库链接

Cj.



Server.ontext-path -> server.servlet.context-path:/sell 

为什么多servlet因为2.0多了webflux特性



警告废弃

@NotEmpty，进入注解更换包



new pagerequest（）-> pagerequest（）.of



URLEncode.encode

