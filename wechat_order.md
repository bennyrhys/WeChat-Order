[TOC]

作者：bennyrhys@163.com （瑞新）

# Mac下Idea快捷键

看实现类：alt+commed+b

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

git clone https://git.imooc.com/coding-117/coding-117.git

切换标签分支

git checkout -b xxx xxx

查看当前分支

git log

返回刚刚已切换的标签

git checkout -b xxx 

终端插件

Zsh autosuggestions



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
  `product_status` tinyint(3) DEFAULT '0' COMMENT '商品状态,0正常1下架',
	`create_time` timestamp not null default current_timestamp comment '创建时间',
	`update_time` timestamp not null default current_timestamp on update current_timestamp comment '更新时间',
	primary key (`product_id`)
)comment '商品表';

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
	` ` varchar(512) not null comment '商品小图',
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

# 1.x-2.x版本差异

断言

Assert -> Assertions



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

# 类目

## Dao与Test

增删改查

时间自动更新

事务回滚

根据jpa规则写接口方法

pom.xml

```xml
<!--        添加小工具 日志方便，get、set方便【idea中下载插件lombok 】-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
<!--        引入数据库-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
<!--        jpa操作数据库-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
```

application.yml

```yml
# 数据库配置
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: 123456
    url: jdbc:mysql://192.168.210.132/db_wxds?characterEncoding=utf-8&userSSL=false
  jpa:
    show-sql: true
```

属性ProductCategory

```java
package com.bennyrhys.wechat_order.daoobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 类目
 * 注意表名：idea中驼峰，sql表是_。springDataJpa做的映射。（如果非要自定义idea的表名,下边@Table，不建议）
 * @Author bennyrhys
 * @Date 2020-06-26 10:28
 */
//@Table(name = "s_my_table")
// 数据库映射成对象
@Entity
// 自动更新时间
@DynamicUpdate

//  每次属性类型改变不想重写get、set方法生成麻烦。那就全部去掉@Data代替【插件lombok，不会影响性能，打包时自动添加get、set】
@Data
public class ProductCategory {
//    字段命名也是对应_为驼峰
//    类目id【主键 自增指定生成策略】
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;
//    类目名称
    private String categoryName;
//    类目编号
    private Integer categoryType;
////    创建时间
//    private Date createTime;
////    更新时间
//    private Date updateTime;

//    get、set toString

}
```

Dao-ProductCategoryRepository

```java
package com.bennyrhys.wechat_order.repository;

import com.bennyrhys.wechat_order.daoobject.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author bennyrhys
 * @Date 2020-06-26 10:40
 */
// 继承jpa<对象， 主键类型>
//    直接通过接口名创建测试类
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
//    注意需要按照指定格式
//    通过类目编号数组查询类目
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
```

测试ProductCategoryRepositoryTest

```java
package com.bennyrhys.wechat_order.repository;

import com.bennyrhys.wechat_order.daoobject.ProductCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;


/**
 * @Author bennyrhys
 * @Date 2020-06-26 10:42
 */
@SpringBootTest
public class ProductCategoryRepositoryTest {
    @Autowired
    private ProductCategoryRepository repository;
//  查
    @Test
    public void findById(){
        ProductCategory productCategory = repository.findById(2).orElse(null);
        System.out.println(productCategory.toString());
    }
//    增
//    为保证数据库干净 操作完就回滚【@Transactional完全回滚。不像事务里，失败才回滚】
    @Test
    @Transactional
    public void saveOne() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("女生最爱");
        productCategory.setCategoryType(2);
        ProductCategory result = repository.save(productCategory);

//      断言
        if (result == null){
            Assert.isTrue(false,"null才抛异常");
        }


    }
//  更，共用save但是要指定主键

    @Test
    public void updateOne() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryId(2);
        productCategory.setCategoryName("女生最爱");
        productCategory.setCategoryType(2);
        repository.save(productCategory);
    }
//    场景模拟-先查出后修改
    @Test
    public void findAndSave(){
        ProductCategory productCategory = repository.findById(2).orElse(null);
        productCategory.setCategoryType(2);
        repository.save(productCategory);
    }

//    接口制定方法：类目编号数组查类目
    @Test
    public void findByCategoryTypeIn() {
        List<Integer> list = Arrays.asList(1,2,4);
        List<ProductCategory> result = repository.findByCategoryTypeIn(list);
//      断言不为0则成功
        Assert.notEmpty(result,"不为空数组");
    }
}
```

## Service与Test

com.bennyrhys.wechat_order.service.CategoryService

```java
package com.bennyrhys.wechat_order.service;

import com.bennyrhys.wechat_order.daoobject.ProductCategory;

import java.util.List;

/**
 * 类目
 * @Author bennyrhys
 * @Date 2020-06-26 16:12
 */
public interface CategoryService {
    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);

}
```

CategorySericeImpl

```java
package com.bennyrhys.wechat_order.service.impl;

import com.bennyrhys.wechat_order.daoobject.ProductCategory;
import com.bennyrhys.wechat_order.repository.ProductCategoryRepository;
import com.bennyrhys.wechat_order.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类目
 * @Author bennyrhys
 * @Date 2020-06-26 16:16
 */
@Service
public class CategorySericeImpl implements CategoryService {
    @Autowired
    ProductCategoryRepository repository;

    @Override
    public ProductCategory findOne(Integer categoryId) {
        return repository.findById(categoryId).orElse(null);
    }

    @Override
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        return repository.findByCategoryTypeIn(categoryTypeList);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return repository.save(productCategory);
    }
}
```

CategorySericeImplTest

```java
package com.bennyrhys.wechat_order.service.impl;

import com.bennyrhys.wechat_order.daoobject.ProductCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author bennyrhys
 * @Date 2020-06-27 08:46
 */
@SpringBootTest
class CategorySericeImplTest {

    @Autowired
    CategorySericeImpl categorySerice;

    @Test
    void findOne() {
        ProductCategory one = categorySerice.findOne(1);
        Assertions.assertEquals(1, one.getCategoryId());
    }

    @Test
    void findAll() {
        List<ProductCategory> categoryList = categorySerice.findAll();
        Assertions.assertNotEquals(0,categoryList.size());
    }

    @Test
    void findByCategoryTypeIn() {
        List<ProductCategory> byCategoryTypeIn = categorySerice.findByCategoryTypeIn(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        Assertions.assertNotEquals(0, byCategoryTypeIn.size());
    }

    @Test
    void save() {
        ProductCategory category = new ProductCategory("男生专享", 3);
        ProductCategory result = categorySerice.save(category);
        Assertions.assertNotNull(result);
    }
}
```

# 商品

## 枚举商品状态

```
package com.bennyrhys.wechat_order.enums;

import lombok.Getter;

/**
 * 商品状态
 * @Author bennyrhys
 * @Date 2020-06-27 17:00
 */
@Getter
public enum ProductStatusEnum {
    UP(0, "在架"),
    DOWN (1, "下架");

    private Integer code;
    private String msg;

    ProductStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
```

## Dao

ProductInfo

```java
package com.bennyrhys.wechat_order.daoobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * 商品
 * @Author bennyrhys
 * @Date 2020-06-27 09:34
 */
@Entity
@Data
public class ProductInfo {
    @Id
//    商品id
    private String productId;
//    商品名称
    private String productName;
//    商品单价
    private BigDecimal productPrice;
//    商品库存
    private Integer productStock;
//    商品描述
    private String productDescription;
//    商品小图
    private String productIcon;
//    类目编号
    private Integer categoryType;
//    商品状态,0正常1下架
    private Integer productStatus;
}
```

ProductInfoRepository

```java
package com.bennyrhys.wechat_order.repository;

import com.bennyrhys.wechat_order.daoobject.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author bennyrhys
 * @Date 2020-06-27 09:43
 */
public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {
    /**
     * 查询上架商品
     */
    List<ProductInfo> findByProductStatus(Integer productStatus);
}
```

ProductInfoRepositoryTest

```java
package com.bennyrhys.wechat_order.repository;

import com.bennyrhys.wechat_order.daoobject.ProductInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author bennyrhys
 * @Date 2020-06-27 09:50
 */
@SpringBootTest
class ProductInfoRepositoryTest {
    @Autowired
    ProductInfoRepository repository;
    @Test
    public void saveTest() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("11");
        productInfo.setCategoryType(1);
        productInfo.setProductName("衬衫");
        productInfo.setProductDescription("纯棉，超薄");
        productInfo.setProductIcon("http://xxxx.png");
        productInfo.setProductPrice(new BigDecimal(9.9));
        productInfo.setProductStock(100);
        productInfo.setProductStatus(0);

        ProductInfo result = repository.save(productInfo);
        Assertions.assertNotNull(result);
    }

//    测试上架商品
    @Test
    public void findByProductStatus(){
        List<ProductInfo> productInfoList = repository.findByProductStatus(0);
        Assertions.assertNotEquals(0, productInfoList.size());
    }

}
```

## Serice 分页

ProductSerice

```java
package com.bennyrhys.wechat_order.service;

import com.bennyrhys.wechat_order.daoobject.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商品
 * @Author bennyrhys
 * @Date 2020-06-27 16:49
 */
public interface ProductSerice {
    ProductInfo findOne(String productId);

    /**
     * 查询在架的商品-c端
     * @return
     */
    List<ProductInfo> findUpAll();

    /**
     * 查找全部商品
     * @param pageable 分页 springframe domain的包
     * @return
     */
    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

//    加库存 减库存

}
```

ProductServiceImpl

```java
package com.bennyrhys.wechat_order.service.impl;

import com.bennyrhys.wechat_order.daoobject.ProductInfo;
import com.bennyrhys.wechat_order.enums.ProductStatusEnum;
import com.bennyrhys.wechat_order.repository.ProductInfoRepository;
import com.bennyrhys.wechat_order.service.ProductSerice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品
 * @Author bennyrhys
 * @Date 2020-06-27 16:55
 */
@Service
public class ProductServiceImpl implements ProductSerice {
    @Autowired
    ProductInfoRepository repository;

    @Override
    public ProductInfo findOne(String productId) {
        return repository.findById(productId).orElse(null);
    }

    /**
     * 查询在架的商品-c端
     *
     * @return
     */
    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    /**
     * 查找全部商品
     *
     * @param pageable 分页 springframe domain的包
     * @return
     */
    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }
}
```

测试

```java
package com.bennyrhys.wechat_order.service.impl;

import com.bennyrhys.wechat_order.daoobject.ProductInfo;
import com.bennyrhys.wechat_order.enums.ProductStatusEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品
 * @Author bennyrhys
 * @Date 2020-06-27 17:05
 */
@SpringBootTest
class ProductServiceImplTest {

    @Autowired
     private ProductServiceImpl productSerice;

    @Test
    void findOne() {
        ProductInfo productInfo = productSerice.findOne("11");
        Assertions.assertEquals("11", productInfo.getProductId());
    }

    @Test
    void findUpAll() {
        List<ProductInfo> productInfoList = productSerice.findUpAll();
        Assertions.assertNotEquals(0, productInfoList.size());
    }

    @Test
    void findAll() {
//        配置PageAble对象
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<ProductInfo> productInfos = productSerice.findAll(pageRequest);
//        System.out.println(productInfos.getTotalElements());
        Assertions.assertNotEquals(0, productInfos.getTotalElements());
    }

    @Test
    void save() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("22");
        productInfo.setCategoryType(1);
        productInfo.setProductName("皮蛋瘦肉粥");
        productInfo.setProductDescription("超甜，很好喝");
        productInfo.setProductIcon("http://xxxx.png");
        productInfo.setProductPrice(new BigDecimal(5.9));
        productInfo.setProductStock(100);
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());

        ProductInfo result = productSerice.save(productInfo);
        Assertions.assertNotNull(result);
    }
}
```

## API 封装jsonVO 封装状态

商品列表

com.bennyrhys.wechat_order.VO.ResultVO

```java
package com.bennyrhys.wechat_order.VO;

import lombok.Data;

/**
 * http请求返回的最外层对象
 * @Author bennyrhys
 * @Date 2020-06-27 19:56
 */
@Data
public class ResultVO<T> {
//    错误码
    private Integer code;
//    提示
    private String msg;
//    具体内容
    private T data;
}
```

```java
package com.bennyrhys.wechat_order.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 商品类目
 * @Author bennyrhys
 * @Date 2020-06-27 20:22
 */
@Data
public class ProductVO {
    @JsonProperty("name")
    private String productName;

    @JsonProperty("type")
    private Integer productType;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;
}
```

```java
package com.bennyrhys.wechat_order.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品详情（删减脱敏 ）
 * @Author bennyrhys
 * @Date 2020-06-27 20:24
 */
@Data
public class ProductInfoVO {
    @JsonProperty("id")
    private String productId;

    @JsonProperty("name")
    private String productName;

    @JsonProperty("price")
    private BigDecimal productPrice;

    @JsonProperty("description")
    private String productDescription;

    @JsonProperty("icon")
    private String productIcon;
}
```

封装状态

com.bennyrhys.wechat_order.utils.ResultVOUtils

```java
package com.bennyrhys.wechat_order.utils;

import com.bennyrhys.wechat_order.VO.ResultVO;

/**
 * 通用返回消息
 * @Author bennyrhys
 * @Date 2020-06-27 21:24
 */
public class ResultVOUtils {
    public static ResultVO success(Object object) {
        ResultVO<Object> resultVO = new ResultVO<>();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO success() {
        return success(null);
    }

    public static ResultVO error(Integer code, String msg) {
        ResultVO<Object> resultVO = new ResultVO<>();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
}
```

控制台

```java
package com.bennyrhys.wechat_order.controller;

import com.bennyrhys.wechat_order.VO.ProductInfoVO;
import com.bennyrhys.wechat_order.VO.ProductVO;
import com.bennyrhys.wechat_order.VO.ResultVO;
import com.bennyrhys.wechat_order.daoobject.ProductCategory;
import com.bennyrhys.wechat_order.daoobject.ProductInfo;
import com.bennyrhys.wechat_order.service.CategoryService;
import com.bennyrhys.wechat_order.service.ProductSerice;
import com.bennyrhys.wechat_order.utils.ResultVOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 买家商品
 * @Author bennyrhys
 * @Date 2020-06-27 19:30
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {
    
    @Autowired 
    private ProductSerice productSerice;

    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("list")
    public ResultVO list() {
//        1. 查询所有上架商品
        List<ProductInfo> productInfoList = productSerice.findUpAll();
//        2. 查询类目（一次查清）
//        List<Integer> categoryTypeList = new ArrayList<>();
//        获取categoryType-传统方法
//        for (ProductInfo productInfo : productInfoList) {
//            categoryTypeList.add(productInfo.getCategoryType());
//        }
//        精简方法（java8 lambda）
        List<Integer> categoryTypeList = productInfoList.stream()
                .map(e -> e.getCategoryType())
                .collect(Collectors.toList());
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypeList);
//        3. 数据拼装
        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setProductType(productCategory.getCategoryType());
            productVO.setProductName(productCategory.getCategoryName());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList) {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO = new ProductInfoVO();
//                    每个重新赋值太慢 对象拷贝
                    BeanUtils.copyProperties(productInfo, productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }


//        【直接返回】
//        ResultVO<Object> resultVO = new ResultVO<>();
//        resultVO.setData(productVOList);
//        resultVO.setCode(0);
//        resultVO.setMsg("成功");

//        【测试 json 结构】
//        ResultVO<Object> vo = new ResultVO<>();
//        ProductVO productVO = new ProductVO();
//        ProductInfoVO productInfoVO = new ProductInfoVO();
//
//        vo.setCode(0);
//        vo.setMsg("成功");
//
//        productVO.setProductInfoVOList(Arrays.asList(productInfoVO));
//        vo.setData(Arrays.asList(productVO));

//      【封装返回数据】
        return ResultVOUtils.success(productVOList);
    }
}
```

## 前端联调

>虚拟机nginx和本地联调，数据渲染

缺少cookie中携带的openid

http://192.168.210.132/

http://192.168.210.132/#/order 设置cookie document.cookie='openid=abc123'

虚拟机设置nginx配置文件`vim /usr/local/nginx/conf/nginx.conf`

1. 修改本机的100.68.53.177

   ```xml
    location /sell/ {
               proxy_pass http://100.68.53.177:8080/sell/;
           }
   ```

2. 修改域名`server_name sell.com;`

   修改本机的host`sudo vim /etc/hosts`

   ```
   # 配置项目的访问
   192.168.210.132 sell.com
   ```

3. 重启nginx`ngnix -s reload`



# 订单&订单详情

## 状态枚举

ResultEnum

```java
package com.bennyrhys.wechat_order.enums;

import lombok.Getter;

/**
 * 返回给前端提示的异常消息
 * @Author bennyrhys
 * @Date 2020-06-28 10:12
 */
@Getter
public enum ResultEnum {
    PRODUCT_NOT_EXIST(10, "商品不存在"),
    PRODUCT_STOCK_ERROR(11,"库存不正确"),
    ORDER_NOT_EXIST(12, "订单为空"),
    ORDERDETAIL_NOT_EXIST(13, "订单详情为空"),
    ORDER_STATUS_ERROR(14, "订单状态不正确"),
    ORDER_UPDATE_FAIL(15,"订单更新失败"),
    ORDER_UPDATE_EMPTY(16,"订单详情为空"),
    ORDER_PAY_STATUS_ERROR(17, "订单支付状态不正确"),


    ;

    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
```

PayStatusEnum

```java
package com.bennyrhys.wechat_order.enums;

import lombok.Getter;

/**
 * 支付状态
 * @Author bennyrhys
 * @Date 2020-06-28 08:00
 */
@Getter
public enum PayStatusEnum {
    WAIT(0, "待支付"),
    SUCCESS(1, "支付成功")
    ;

    private Integer code;
    private String msg;

    PayStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
```

OrderStatusEnum

```java
package com.bennyrhys.wechat_order.enums;

import lombok.Getter;

/**
 * 订单状态
 * @Author bennyrhys
 * @Date 2020-06-28 07:53
 */
@Getter
public enum OrderStatusEnum {
    NEW(0, "新订单"),
    FINISHED(1, "完结"),
    CANCEL(2, "取消"),
    ;
    private Integer code;
    private String msg;

    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
```

## Dao

OrderMaster

```java
package com.bennyrhys.wechat_order.daoobject;

import com.bennyrhys.wechat_order.enums.OrderStatusEnum;
import com.bennyrhys.wechat_order.enums.PayStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单表
 * @Author bennyrhys
 * @Date 2020-06-28 07:49
 */
@Entity
@Data
//    时间自动更新
@DynamicUpdate
public class OrderMaster {
//    订单id
    @Id
    private String orderId;
//    买家名字
    private String buyerName;
//    买家电话
    private String buyerPhone;
//    买家地址
    private String buyerAddress;
//    买家微信openid
    private String buyerOpenid;
//    订单总金额
    private BigDecimal orderAmount;
//    订单状态,默认0新下单
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();
//    支付状态,默认0未支付
    private Integer payStatus = PayStatusEnum.WAIT.getCode();
//    创建时间 【考虑到时间排序】
    private Date createTime;
//    更新时间
    private Date updateTime;

}
```

OrderDetail

```java
package com.bennyrhys.wechat_order.daoobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * 订单详情
 * @Author bennyrhys
 * @Date 2020-06-28 08:09
 */
@Entity
@Data
public class OrderDetail {
//    订单详情id
    @Id
    private String detailId;
//    订单id
    private String orderId;
//    商品id
    private String productId;
//    商品名称
    private String productName;
//    商品价格
    private BigDecimal productPrice;
//    商品数量
    private Integer productQuantity;
//    商品小图
    private String productIcon;
}
```

OrderMasterRepository

```java
package com.bennyrhys.wechat_order.repository;

import com.bennyrhys.wechat_order.daoobject.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 订单
 * @Author bennyrhys
 * @Date 2020-06-28 08:16
 */
public interface OrderMasterRepository extends JpaRepository<OrderMaster, String> {

    /**
     * 分页查询一个用户的全部订单
     * @param buyerOpenid
     * @param pageable
     * @return
     */
    Page<OrderMaster> findByBuyerOpenid(String buyerOpenid, Pageable pageable);
}
```

OrderDetailRepository

```java
package com.bennyrhys.wechat_order.repository;

import com.bennyrhys.wechat_order.daoobject.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 订单详情
 * @Author bennyrhys
 * @Date 2020-06-28 08:20
 */
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    /**
     * 根据订单id查询订单详情
     * 一对多
     * @param orderId
     * @return
     */
    List<OrderDetail> findByOrderId(String orderId);
}
```

OrderMasterRepositoryTest

```java
package com.bennyrhys.wechat_order.repository;

import com.bennyrhys.wechat_order.daoobject.OrderMaster;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author bennyrhys
 * @Date 2020-06-28 08:27
 */
@SpringBootTest
class OrderMasterRepositoryTest {
    @Autowired
    OrderMasterRepository repository;

    private final String OPENID = "110110";

    @Test
    public void saveTest() {
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("12");
        orderMaster.setBuyerName("瑞新");
        orderMaster.setBuyerAddress("吉林通化");
        orderMaster.setBuyerOpenid(OPENID);
        orderMaster.setBuyerPhone("123456789123");
        orderMaster.setOrderAmount(new BigDecimal(8.8));

        OrderMaster result = repository.save(orderMaster);

        Assertions.assertNotNull(result);
    }

    /**
     * 分页查询一个用户的全部订单
     */
    @Test
    public void findByBuyerOpenid() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<OrderMaster> byBuyerOpenid = repository.findByBuyerOpenid(OPENID, pageRequest);
//        System.out.println(byBuyerOpenid.getTotalElements());

        Assertions.assertNotEquals(0, byBuyerOpenid.getTotalElements());
    }
}
```

OrderDetailRepositoryTest

```java
package com.bennyrhys.wechat_order.repository;

import com.bennyrhys.wechat_order.daoobject.OrderDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单详情
 * @Author bennyrhys
 * @Date 2020-06-28 08:56
 */
@SpringBootTest
class OrderDetailRepositoryTest {
    @Autowired
    OrderDetailRepository repository;

    @Test
    public void saveTest() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setDetailId("2");
        orderDetail.setOrderId("11");
        orderDetail.setProductIcon("http://xxx.png");
        orderDetail.setProductId("11");
        orderDetail.setProductName("雪梨");
        orderDetail.setProductPrice(new BigDecimal(9.5));
        orderDetail.setProductQuantity(2);

        OrderDetail result = repository.save(orderDetail);

        Assertions.assertNotNull(result);
    }

    /**
     * 根据订单id查订单详情
     * 一对多
     */
    @Test
    public void findByOrderId() {
        List<OrderDetail> detailList = repository.findByOrderId("11");
        Assertions.assertNotEquals(0, detailList.size());
    }
}
```

## DTO 

为逻辑处理，抽象出来的传输对象。（保证不修改，已有的数据库字段上）

法1：注解，在原有字段进行改动

法2：dto新创建文件，方便维护



OrderMaster注解方式修改【不建议】

```java
//    【新增】一对多关系，订单详情列表。防止映射为空加 @Transient 过滤掉
//    @Transient
//    private List<OrderDetail> orderDetailList;
```

com.bennyrhys.wechat_order.dto.OrderDTO 新建dto文件单独生成字段【建议】

```java
package com.bennyrhys.wechat_order.dto;

import com.bennyrhys.wechat_order.daoobject.OrderDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单
 * 逻辑处理，数据传输的定制化字段
 *
 * 注意：@Id不用写了，增加的字段不用注解特殊忽略
 * @Author bennyrhys
 * @Date 2020-06-28 09:27
 */
@Data
public class OrderDTO {
    //    订单id
    private String orderId;
    //    买家名字
    private String buyerName;
    //    买家电话
    private String buyerPhone;
    //    买家地址
    private String buyerAddress;
    //    买家微信openid
    private String buyerOpenid;
    //    订单总金额
    private BigDecimal orderAmount;
    //    订单状态,默认0新下单
    private Integer orderStatus;
    //    支付状态,默认0未支付
    private Integer payStatus;
    //    创建时间 【考虑到时间排序】
    private Date createTime;
    //    更新时间
    private Date updateTime;

//    【新增】一对多关系，订单详情列表。防止映射为空加 @Transient 过滤掉
    private List<OrderDetail> orderDetailList;
}
```

方便计算减库存的购物车信息

CartDTO

```java
package com.bennyrhys.wechat_order.dto;

import lombok.Data;

/**
 * 购物车
 * @Author bennyrhys
 * @Date 2020-06-28 11:00
 */
@Data
public class CartDTO {
    private String productId;
    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
```

## 封装抛异常

com.bennyrhys.wechat_order.exception.SellException

```java
package com.bennyrhys.wechat_order.exception;

import com.bennyrhys.wechat_order.enums.ResultEnum;

/**
 * 抛异常
 * @Author bennyrhys
 * @Date 2020-06-28 10:11
 */
public class SellException extends RuntimeException {
    private Integer code;


    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }
}
```

```java
package com.bennyrhys.wechat_order.enums;

import lombok.Getter;

/**
 * 返回给前端提示的异常消息
 * @Author bennyrhys
 * @Date 2020-06-28 10:12
 */
@Getter
public enum ResultEnum {
    PRODUCT_NOT_EXIST(10, "商品不存在"),
    PRODUCT_STOCK_ERROR(11,"库存不正确")
    ;

    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
```

## 随机key

```java
package com.bennyrhys.wechat_order.utils;

import java.util.Random;

/**
 * 随机数生成
 * 用于无法自增的主键 key
 * @Author bennyrhys
 * @Date 2020-06-28 10:33
 */
public class KeyUtil {
    /**
     * 生成唯一主键
     * 格式：时间+随机数
     * 防止多线程
     */

    public static synchronized String genUniqueKey() {
//        六位定长
        Integer number = new Random().nextInt(900000)+100000;
        return System.currentTimeMillis() + String.valueOf(number);
    }
}
```

## 类型转换器

```java
package com.bennyrhys.wechat_order.converter;

import com.bennyrhys.wechat_order.daoobject.OrderMaster;
import com.bennyrhys.wechat_order.dto.OrderDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 转换器
 * @Author bennyrhys
 * @Date 2020-06-28 12:49
 */
public class OrderMaster2OrderDTOConverter {
    public static OrderDTO convert(OrderMaster orderMaster) {
        OrderDTO orderDTO = new OrderDTO();

        BeanUtils.copyProperties(orderMaster, orderDTO);

        return orderDTO;
    }

    public static List<OrderDTO> convert(List<OrderMaster> orderMasterList) {
        return orderMasterList.stream().map(e ->
                convert(e))
                .collect(Collectors.toList());
    }

}
```

## Service

商品库存

ProductSerice

```java
/**
 * 加库存 【传入抽象的 购物车 信息】
 * @param cartDTOList
 */
void increaseStock(List<CartDTO> cartDTOList);

/**
 * 减库存
 */
void decreaseStock(List<CartDTO> cartDTOList);
```

ProductServiceImpl

```java
/**
 * 加库存 【传入抽象的 购物车 信息】
 *
 * @param cartDTOList
 */
@Override
@Transactional
public void increaseStock(List<CartDTO> cartDTOList) {
    for (CartDTO cartDTO : cartDTOList) {
        ProductInfo productInfo = repository.findById(cartDTO.getProductId()).orElse(null);
        if (productInfo == null){
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        Integer result = productInfo.getProductStock() + cartDTO.getProductQuantity();
        productInfo.setProductStock(result);

        repository.save(productInfo);
    }
}

/**
 * 减库存
 *
 * @param cartDTOList
 */
@Override
@Transactional
public void decreaseStock(List<CartDTO> cartDTOList) {
    for (CartDTO cartDTO : cartDTOList) {
        ProductInfo productInfo = repository.findById(cartDTO.getProductId()).orElse(null);
        if (productInfo == null){
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }

        Integer result = productInfo.getProductStock() - cartDTO.getProductQuantity();
        if (result < 0) {
            throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
        }

        productInfo.setProductStock(result);

        repository.save(productInfo);
    }
}
```



创建订单

OrderService

```java
package com.bennyrhys.wechat_order.service;

import com.bennyrhys.wechat_order.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 订单
 * @Author bennyrhys
 * @Date 2020-06-28 09:23
 */
public interface OrderService {
    /** 创建订单
     * 为订单-订单详情，一对多关系抽象出dto */
    OrderDTO create(OrderDTO orderDTO);

    /** 查询单个订单 */
    OrderDTO findOne(String orderId);

    /** 查询订单列表
     * 注意：包名springframe */
    Page<OrderDTO> findList(String buyerOpenId, Pageable pageable);

    /** 取消订单 */
    OrderDTO cancel(OrderDTO orderDTO);

    /** 完结订单 */
    OrderDTO finish(OrderDTO orderDTO);

    /** 支付订单 */
    OrderDTO paid(OrderDTO orderDTO);

}
```

```java
package com.bennyrhys.wechat_order.service.impl;

import com.bennyrhys.wechat_order.OrderMaster2OrderDTOConverter;
import com.bennyrhys.wechat_order.daoobject.OrderDetail;
import com.bennyrhys.wechat_order.daoobject.OrderMaster;
import com.bennyrhys.wechat_order.daoobject.ProductInfo;
import com.bennyrhys.wechat_order.dto.CartDTO;
import com.bennyrhys.wechat_order.dto.OrderDTO;
import com.bennyrhys.wechat_order.enums.OrderStatusEnum;
import com.bennyrhys.wechat_order.enums.PayStatusEnum;
import com.bennyrhys.wechat_order.enums.ResultEnum;
import com.bennyrhys.wechat_order.exception.SellException;
import com.bennyrhys.wechat_order.repository.OrderDetailRepository;
import com.bennyrhys.wechat_order.repository.OrderMasterRepository;
import com.bennyrhys.wechat_order.service.OrderService;
import com.bennyrhys.wechat_order.service.ProductSerice;
import com.bennyrhys.wechat_order.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单
 * @Author bennyrhys
 * @Date 2020-06-28 09:45
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductSerice productSerice;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    /**
     * 创建订单
     * 为订单-订单详情，一对多关系抽象出dto
     *
     * @param orderDTO
     */
    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        String orderId = KeyUtil.genUniqueKey();

//        List<CartDTO> cartDTOList = new ArrayList<>();

//        1. 查询商品（数量、金额）
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            ProductInfo productInfo = productSerice.findOne(orderDetail.getProductId());
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //        2. 计算订单总价 【注意productInfo才有价格】
            orderAmount =  productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);
            //      订单详情入库【注意：前端不会传来全部所需字段。随机数id】
            //      对象拷贝 spring提供的简便方法Info->Detail
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            orderDetailRepository.save(orderDetail);

//            CartDTO cartDTO = new CartDTO(orderDetail.getProductId(), orderDetail.getProductQuantity());
//            cartDTOList.add(cartDTO);
        }
//        3. 写入订单数据库（orderMaster、orderDetail）
        OrderMaster orderMaster = new OrderMaster();
        //       拷贝对象 【注意：属性为null也会被拷贝，调整顺序先拷贝】
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderAmount(orderAmount);
        //      增加因拷贝被覆盖的默认状态值
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);


//        4. 扣库存 【库存一次修改，所以要获取购物车list 法1：上for cartDTO直接list.add 法2：lamdba】
//        并发下 redis锁机制防止超卖
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e ->
                new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productSerice.decreaseStock(cartDTOList);

        return orderDTO;
    }

    /**
     * 查询单个订单
     *
     * @param orderId
     */
    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findById(orderId).orElse(null);
        if (orderMaster == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;


    }

    /**
     * 查询订单列表
     * 注意：包名springframe
     *
     * @param buyerOpenId
     * @param pageable
     */
    @Override
    public Page<OrderDTO> findList(String buyerOpenId, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenId, pageable);
//        不查详情，也无需判空
//        创建类型转换DTO
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());

        Page<OrderDTO> orderDTOS = new PageImpl<OrderDTO>(orderDTOList, pageable, orderMasterPage.getTotalElements());
        return orderDTOS;
    }

    /**
     * 取消订单
     *
     * @param orderDTO
     */
    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
//        状态转换
        OrderMaster orderMaster = new OrderMaster();

//        判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【取消订单，订单状态不正确，orderId={}, orderStatus={}】",orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
//        修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster); // 注意状态修改时机
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.info("【取消订单，更新失败 orderMaster={}】",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
//        返还库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【取消订单，订单详情为空，orderDTO={}】",orderDTO);
            throw new SellException(ResultEnum.ORDER_UPDATE_EMPTY);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productSerice.increaseStock(cartDTOList);
//        如果已支付需要退款
        if(orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            // TODO
        }
        return orderDTO;
    }

    /**
     * 完结订单
     *
     * @param orderDTO
     */
    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
//        判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【完结订单：订单状态不正确 orderId={}, orderStatus={}】",orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
//        修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.info("【完结订单：更新失败 orderMaster={}】",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
    }

    /**
     * 支付订单
     *
     * @param orderDTO
     */
    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
//        判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【订单支付成功】订单状态不正确 orderId={}, orderStatus={}",orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
//        判断支付状态
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【订单支付成功】订单支付状态不正确 orderDTO={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);

        }

//        修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.info("【订单支付成功：更新失败 orderMaster={}】",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
    }
}

```



## 测试

```java
package com.bennyrhys.wechat_order.service.impl;

import com.bennyrhys.wechat_order.daoobject.OrderDetail;
import com.bennyrhys.wechat_order.dto.OrderDTO;
import com.bennyrhys.wechat_order.enums.OrderStatusEnum;
import com.bennyrhys.wechat_order.enums.PayStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单
 * @Author bennyrhys
 * @Date 2020-06-28 11:26
 */
@SpringBootTest
@Slf4j
class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    private final String BUYER_OPENID = "110110";
    private final String ORDER_ID = "1593317227294809400";

    @Test
    void create() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("瑞新");
        orderDTO.setBuyerAddress("吉林通化");
        orderDTO.setBuyerPhone("123456789123");
        orderDTO.setBuyerOpenid(BUYER_OPENID);

//        购物车
        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail od = new OrderDetail();
        od.setProductId("11");
        od.setProductQuantity(1);
        orderDetailList.add(od);

        OrderDetail od2 = new OrderDetail();
        od2.setProductId("22");
        od2.setProductQuantity(2);
        orderDetailList.add(od2);

        orderDTO.setOrderDetailList(orderDetailList);

        OrderDTO result = orderService.create(orderDTO);

//        log.info("【创建订单】result={}", result);
        Assertions.assertNotNull(result);
    }

    @Test
    void findOne() {
        OrderDTO result = orderService.findOne(ORDER_ID);

        log.info("【单个订单】result={}", result);
        Assertions.assertEquals(ORDER_ID, result.getOrderId());
    }

    @Test
    void findList() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<OrderDTO> orderDTOPage =  orderService.findList(BUYER_OPENID,pageRequest);
        Assertions.assertNotEquals(0, orderDTOPage.getTotalElements());
    }

    @Test
    void cancel() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.cancel(orderDTO);

        Assertions.assertEquals(OrderStatusEnum.CANCEL.getCode(), result.getOrderStatus());
    }

    @Test
    void finish() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.finish(orderDTO);

        Assertions.assertEquals(OrderStatusEnum.FINISHED.getCode(), result.getOrderStatus());
    }

    @Test
    void paid() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.paid(orderDTO);

        Assertions.assertEquals(PayStatusEnum.SUCCESS.getCode(), result.getPayStatus());
    }

}
```

## API

### 修改时间：毫秒到秒

Date2LongSerializer时间转换

```java
package com.bennyrhys.wechat_order.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

/**
 * 处理时间 毫秒变秒
 * @Author bennyrhys
 * @Date 2020-06-28 18:28
 */
public class Date2LongSerializer extends JsonSerializer<Date> {


    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(date.getTime() / 1000);
    }
}
```

OrderDTO字段类型指定类进行计算

```java
//    创建时间 【考虑到时间排序】 @JsonSerialize 毫秒变秒
@JsonSerialize(using = Date2LongSerializer.class)
private Date createTime;
//    更新时间
@JsonSerialize(using = Date2LongSerializer.class)
private Date updateTime;
```

### 修改返回类型



>法1：null则不显示
>
>法2：在返回值OrderDTO中赋初值

**null则不显示**

OrderDTO

```java
// 保障不给前端返回为null的字段 【一个个配太傻，配置文件统一配】
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
```

配置文件控制全局

```yml
# 配置json为nll的字段 不显示
   jackson:
    default-property-inclusion: non_null
```



**空列表=[]**

```java
//    【新增】一对多关系，订单详情列表。防止映射为空加 @Transient 过滤掉
    private List<OrderDetail> orderDetailList = new ArrayList<>();
```

"orderDetailList": []



**空字符串=“”**

BuyerOrderController

```java
//        return ResultVOUtil.success(orderDTOPage.getContent());
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        return resultVO;
```

ResultVO

```java
//    提示
    private String msg="";
```

{

​    "code": 0,

​    "msg": "",

​    "data": null

}

### 控制台

service辅助，校验openid是否为本人操作

订单详情、取消订单（确认身份）

BuyerService

```java
package com.bennyrhys.wechat_order.service;

import com.bennyrhys.wechat_order.dto.OrderDTO;

/**
 * 买家
 * 为控制台提供安全性保证
 * @Author bennyrhys
 * @Date 2020-06-29 08:31
 */
public interface BuyerService {
//    查询一个订单
    OrderDTO findOrderOne(String openid, String orderId);
//    取消一个订单
    OrderDTO cancelOrder(String openid, String orderId);
}
```

BuyerServiceImpl

```java
package com.bennyrhys.wechat_order.service.impl;

import com.bennyrhys.wechat_order.dto.OrderDTO;
import com.bennyrhys.wechat_order.enums.ResultEnum;
import com.bennyrhys.wechat_order.exception.SellException;
import com.bennyrhys.wechat_order.service.BuyerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 买家
 * 为控制台提供安全性保证
 * @Author bennyrhys
 * @Date 2020-06-29 08:33
 */
@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    OrderServiceImpl orderService;

    @Override
    public OrderDTO findOrderOne(String openid, String orderId) {
        return checkOrderOwner(openid, orderId);
    }

    @Override
    public OrderDTO cancelOrder(String openid, String orderId) {
        OrderDTO orderDTO = checkOrderOwner(openid, orderId);
        if (orderDTO == null) {
            log.error("【取消订单】查不到该订单。orderId={}", orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        return orderService.cancel(orderDTO);
    }

    /**
     * 判断是否是自己的订单，通过openid
     */
    public OrderDTO checkOrderOwner(String openid, String orderId) {
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null) {
            return  null;
        }
//       判断是否是自己的订单，通过openid
        if(!orderDTO.getBuyerOpenid().equalsIgnoreCase(openid)) {
            log.error("【查询订单】订单的openid不一致。openid={},orderId", openid, orderId);
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return orderDTO;
    }
}
```

BuyerOrderController

```java
package com.bennyrhys.wechat_order.controller;

import com.bennyrhys.wechat_order.VO.ResultVO;
import com.bennyrhys.wechat_order.converter.OrderForm2OrderDTOConverter;
import com.bennyrhys.wechat_order.dto.OrderDTO;
import com.bennyrhys.wechat_order.enums.ResultEnum;
import com.bennyrhys.wechat_order.exception.SellException;
import com.bennyrhys.wechat_order.form.OrderForm;
import com.bennyrhys.wechat_order.service.BuyerService;
import com.bennyrhys.wechat_order.service.OrderService;
import com.bennyrhys.wechat_order.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 买家订单
 * @Author bennyrhys
 * @Date 2020-06-28 15:35
 */
@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private BuyerService buyerService;

    /**
     * 创建订单
     * @param orderForm
     * @param bindingResult
     * @return
     */
    @PostMapping("create")
    public ResultVO<Map<String, String>> create(@Valid OrderForm orderForm,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("【创建定单】参数不正确orderForm={}", orderForm);
//            捕获表单异常并返回
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }

//        转换orderForm->orderDTO
        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【创建订单】购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }
        OrderDTO createResult = orderService.create(orderDTO);

        Map<String, String> map = new HashMap<>();
        map.put("orderId", createResult.getOrderId());

        return ResultVOUtil.success(map);

    }
    /**
     * 订单列表
     * http://localhost:8080/sell/buyer/order/list?openid= ew3euwhd7sjw9diwkq&page=&size=
     */
    @GetMapping("/list")
    public ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid,
                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if (StringUtils.isEmpty(openid)) {
            log.error("【查询订单列表】订单列表为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<OrderDTO> orderDTOPage = orderService.findList(openid, pageRequest);

        return ResultVOUtil.success(orderDTOPage.getContent());
    }

    /**
     * 订单详情
     */
    @GetMapping("/detail")
    public ResultVO<OrderDTO> detail(@RequestParam("openid") String openid,
                                     @RequestParam("orderId") String orderId) {
        // 直接访问资源，越权访问
        // TODO 不安全的做法，已改进
        OrderDTO orderDTO = buyerService.findOrderOne(openid, orderId);
        return ResultVOUtil.success(orderDTO);
    }
    /**
     * 取消订单
     */
    @PostMapping("/cancel")
    public ResultVO cancel(@RequestParam("openid") String openid,
                           @RequestParam("orderId") String orderId) {
        // TODO 不安全的做法，已改进
        buyerService.cancelOrder(openid, orderId);
        return ResultVOUtil.success();
    }
}
```

# 内网穿透

使用natapp映射本地环境

# 微信

## 网页授权

### 参考文档

官方文档
https://mp.weixin.qq.com/wiki

调试内网穿透
https://natapp.cn

第三方SDK .
https://github.com/Wechat-Group/weixin-java-tools

支付（需要企业资质）

https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=7_1

网页授权

https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html

### 获取openid之路



### 公众账户设置域名

授权回调本地环境（内网穿透-ssl+https）

填写域名。公众号设置-功能设置-设置JS接口安全域名后，公众号开发者可在该域名下调用微信开放的JS接口。

下载认证文件，拷贝到本地静态资源目录（注意/sell前缀先撤掉，直接访问验证）,验证通过后侧回信息

### 获取code

个体用户没有获取个人信息权限，公众号后台测试号可以啊。再配置一遍js和个人信息的域名安全检测

注意：code仅能使用一次，5分钟后失效

```
https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc10086806f4c0df0&redirect_uri=http://bennyrhys.mynatapp.cc/sell/weixin/auth&response_type=code&scope=snsapi_base&state=STATE#wechat_redirec
```

### 换access_token

snsapi_base 和 snsapi_userinfo两种获取信息

动态获取code拼接 

```
https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc10086806f4c0df0&redirect_uri=http://bennyrhys.mynatapp.cc/sell/weixin/auth&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirec
```

### 手动版

```java
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
```

### 第三方

```
微信小程序：weixin-java-miniapp
微信支付：weixin-java-pay
微信开放平台：weixin-java-open
公众号（包括订阅号和服务号）：weixin-java-mp
企业号/企业微信：weixin-java-cp

<dependency>
  <groupId>com.github.binarywang</groupId>
  <artifactId>（不同模块参考下文）</artifactId>
  <version>3.8.0</version>
</dependency>
```

weixin-java-mp，OAuth2网页授权，



配置文件application.yml

```yml
# 配置微信公众号
wechat:
  mpAppId: wxc10086806f4c0df0
  mpAppSecret: bdcb164ea61b4b1cc6846cd549325898
```

配置WechatMpConfig

```java
package com.bennyrhys.wechat_order.config;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 微信公众号sdk配置
 * @Author bennyrhys
 * @Date 2020-06-30 07:12
 */
@Component
public class WechatMpConfig {

    @Autowired
     private WechatAccountConfig wechatAccountConfig;

    @Bean
    public WxMpService wxMpService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
        return wxMpService;
    }

    @Bean
    public WxMpConfigStorage wxMpConfigStorage(){
        WxMpDefaultConfigImpl wxMpConfigStorage = new WxMpDefaultConfigImpl();
//      从配置文件读
        wxMpConfigStorage.setAppId(wechatAccountConfig.getMpAppId());
        wxMpConfigStorage.setSecret(wechatAccountConfig.getMpAppSecret());
        return wxMpConfigStorage;
    }
}
```

账户信息WechatAccountConfig

```java
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
```

控制台

```java
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
```

### 前端联调

商铺信息api放到静态资源目录，直接渲染数据

虚拟机代码配置前端跳转：cd /opt/code/sell_fe_buyer/config/index.js



**修改前端路径**

[root@localhost config]# vim index.js 

```
sellUrl: 'http://127.0.0.1',
openidUrl: 'http://sell.springboot.cn/sell/wechat/authorize',
wechatPayUrl: 'http://127.0.0.1'

修改后
sellUrl: 'http://sell.com',
openidUrl: 'http://bennyrhys.mynatapp.cc/sell/wechat/authorize',
wechatPayUrl: 'http://127.0.0.1'

```

重新构建前端项目

```
[root@localhost config]# cd ..

[root@localhost sell_fe_buyer]# pwd

/opt/code/sell_fe_buyer

[root@localhost sell_fe_buyer]# npm run build

// 生成文件覆盖网站目录文件
[root@localhost sell_fe_buyer]# ls -al dist/
总用量 8
drwxr-xr-x.  3 root root   38 6月  30 00:42 .
drwxr-xr-x. 10 root root 4096 4月  21 2017 ..
-rw-r--r--.  1 root root  616 6月  30 00:43 index.html
drwxr-xr-x.  4 root root   27 6月  30 00:43 static
[root@localhost sell_fe_buyer]# cp -r dist/* /opt/data/wwwroot/sell/
cp：是否覆盖"/opt/data/wwwroot/sell/index.html"？ y
cp：是否覆盖"/opt/data/wwwroot/sell/static/css/reset.css"？ y
cp：是否覆盖"/opt/data/wwwroot/sell/static/css/app.fc95ecb15d823cfb3172a7b93aaa741d.css"？ y
cp：是否覆盖"/opt/data/wwwroot/sell/static/js/vendor.e8bcf9a796b8d5dbca42.js"？ y
```



场景

注意：以下操作找个wifi，校园网不稳定

微信访问sell.com，手机端无法访问host映射在电脑上。

工具：charles安装配置 for Mac，win的fiddler

解决访问：通过抓包和代理请求将微信请求映射到电脑访问

确认手机ping通，设置http代理为电脑ip端口（8888） 

手机微信访问：sell.com

## 支付

### 文档

支付

https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=7_1

第三方SDK（用于各第三方SDK支付）

https://github.com/Pay-Group/best-pay-sdk



1. 授权和支付分开调试，授权用微信官方测试账号，支付用借我的账号，写死openid调试
2. 微信授权 https://www.imooc.com/article/70497
3. 微信支付 https://www.imooc.com/article/31607
4. 扫码登录调试文档 见doc目录下的open.md

### 在网页发起支付

![image-20200630201849812](https://tva1.sinaimg.cn/large/007S8ZIlly1ggakypigvvj31g40eagy6.jpg)

微信内H5调起支付,写到后端static/pay.html



解决缺少total_fee:

统一下单有total_fee字段返回预支付id就是total_fee，就是指预支付id不正确

解决当前页面url未注册：

公众平台-微信支付，开发配置，支付url最多5个，备案



js静态模板下订单变动态，引入freemark模板。控制台返回ModelAndView路径。相应建立项目freemark文件



配置账户信息application.yml

```yml
# 配置微信公众号
wechat:
#  用户授权-测试号
#  mpAppId: wxc10086806f4c0df0
#  mpAppSecret: bdcb164ea61b4b1cc6846cd549325898

#  支付号-不用secret
  mpAppId: wxd898fcb01713c658
  mchId: 1483469312
  mchKey: C5245D70627C1F8E9964D494B0735025
  keyPath: /var/wechat-cert/h5.p12
  notifyUrl: http/sell/pay/notify # 最好用外网地址【微信异步通知】
```

调用信息WechatAccountConfig

```java
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
```

支付配置WechatPayConfig

```java
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
```

转json看日志方便JsonUtil

```java
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
```

控制台模拟创建PayController

```java
package com.bennyrhys.wechat_order.controller;

import com.bennyrhys.wechat_order.dto.OrderDTO;
import com.bennyrhys.wechat_order.enums.ResultEnum;
import com.bennyrhys.wechat_order.exception.SellException;
import com.bennyrhys.wechat_order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 支付
 * @Author bennyrhys
 * @Date 2020-06-30 16:30
 */

@Controller
@RequestMapping("pay")
@Slf4j
public class PayController {

    @Autowired
    private OrderService orderService;

    @GetMapping("create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl) {
//      1. 查询订单
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null) {
            log.info("【支付】订单为空");
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

//        发起支付【支付逻辑写到paySerive】

        return new ModelAndView("pay/create");
    }
}
```

测试模拟创建订单，PayServiceImplTest

```java
package com.bennyrhys.wechat_order.service.impl;

import com.bennyrhys.wechat_order.dto.OrderDTO;
import com.bennyrhys.wechat_order.service.OrderService;
import com.bennyrhys.wechat_order.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 微信支付
 * @Author bennyrhys
 * @Date 2020-06-30 19:48
 */
@SpringBootTest
@Slf4j
class PayServiceImplTest {

    @Autowired
    private PayService payService;
    @Autowired
    private OrderService orderService;

    @Test
    void create() {
        OrderDTO orderDTO = orderService.findOne("1593336982853846059");
        payService.create(orderDTO);
    }
}
```

静态请求。根据订单的返回信息修改js静态的请求参数。将支付请求在微信上访问请求

static/pay.html

```html
<script>
    function onBridgeReady(){
        WeixinJSBridge.invoke(
            'getBrandWCPayRequest', {
                "appId":"wx2421b1c4370ec43b",     //公众号名称，由商户传入
                "timeStamp":"1395712654",         //时间戳，自1970年以来的秒数
                "nonceStr":"e61463f8efa94090b1f366cccfbbb444", //随机串
                "package":"prepay_id=u802345jgfjsdfgsdg888",
                "signType":"MD5",         //微信签名方式：
                "paySign":"70EA570631E4BB79628FBCA90534C63FF7FADD89" //微信签名
            },
            function(res){
                if(res.err_msg == "get_brand_wcpay_request:ok" ){
                    // 使用以上方式判断前端返回,微信团队郑重提示：
                    //res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
                }
            });
    }
    if (typeof WeixinJSBridge == "undefined"){
        if( document.addEventListener ){
            document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
        }else if (document.attachEvent){
            document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
            document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
        }
    }else{
        onBridgeReady();
    }
</script>
```



### 动态发起支付

引入freemark模板，${}方式动态获取数据

**虚拟机配置前端代码**index.js.外网的支付域名http://sell/wechat/pay/create，编译重新覆盖项目

此时未修改状态，支付后还是显示支付

通过charies在手机选择商品直接支付



PayController

```java
package com.bennyrhys.wechat_order.controller;

import com.bennyrhys.wechat_order.dto.OrderDTO;
import com.bennyrhys.wechat_order.enums.ResultEnum;
import com.bennyrhys.wechat_order.exception.SellException;
import com.bennyrhys.wechat_order.service.OrderService;
import com.bennyrhys.wechat_order.service.PayService;
import com.lly835.bestpay.model.PayRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 支付
 * @Author bennyrhys
 * @Date 2020-06-30 16:30
 */

@Controller
@RequestMapping("pay")
@Slf4j
public class PayController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private PayService payService;

    @GetMapping("create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl,
                               Map<String,Object> map){
//      1. 查询订单
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null) {
            log.info("【支付】订单为空");
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

//      2. 发起支付【支付逻辑写到paySerive】
        PayRequest payRequest = payService.create(orderDTO);
//      参数注入模板动态模板
        map.put("orderId", "1111");
        map.put("payRequest",payRequest);
        map.put("returnUrl", returnUrl);
        return new ModelAndView("pay/create", map);
    }
}
```

create.ftlh

```html
<#--这个文件名后缀 ftl是旧版-->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>#[$]#</title>
</head>
<body>
    <h1>这是模板内容${orderId}</h1>
    <h1>${payRequest.openid}</h1>

</body>
</html>

<#--静->动-->
<script>
    function onBridgeReady(){
        WeixinJSBridge.invoke(
            'getBrandWCPayRequest', {
                "appId":"${payResponse.appId}",     //公众号名称，由商户传入
                "timeStamp":"${payResponse.timeStamp}",         //时间戳，自1970年以来的秒数
                "nonceStr":"${payResponse.nonceStr}", //随机串
                "package":"${payResponse.packAge}",
                "signType":"MD5",         //微信签名方式：
                "paySign":"${payResponse.paySign}" //微信签名
            },
            function(res){
//                    if(res.err_msg == "get_brand_wcpay_request:ok" ) {

//                    }     // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
                location.href = "${returnUrl}";
            }
        );
    }
    if (typeof WeixinJSBridge == "undefined"){
        if( document.addEventListener ){
            document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
        }else if (document.attachEvent){
            document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
            document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
        }
    }else{
        onBridgeReady();
    }
</script>
```

### 异步通知

支付成功不能用前端通知，js微信团队说成功通知不可靠，并且前端代码容易被篡改



yml的配置地址要和控制台请求的一致`notifyUrl: http/sell/pay/notify # 最好用外网地址【微信异步通知】`

异步通知传入的xml中是微信提供的流水号

收到通知，修改支付订单状态。写到service中,处理金额比较差值

返回确认收到。防止微信一直发异步通知：收到异步通过修改订单支付状态后告知微信已经处理。API支付结果



PayService

```java
PayResponse notify(String notifyData);
```

PayServiceImpl

```java
    /**
     * 异步通知
     * @param notifyData
     * @return
     */
    @Override
    public PayResponse notify(String notifyData) {
        //1. 验证签名
        //2. 支付的状态 best替做了前两件事情
        //3. 支付金额
        //4. 支付人(下单人 == 支付人)
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("【微信支付】异步通知，PayResponse={}", JsonUtil.toJson(payResponse));

//        查订单
        OrderDTO orderDTO = orderService.findOne(payResponse.getOrderId());

//        判断订单是否存在
        if (orderDTO == null) {
            log.error("【微信支付】异步通知，订单不存在.orderId={}",payResponse.getOrderId());
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

//        判断金额是否一致【金额比较不能Bigdecim和double比较，并且转换后还有小数不一致情况，所以相减解决】compareTo == 0 代表金额一致
        if (!MathUtil.equals(payResponse.getOrderAmount(), orderDTO.getOrderAmount().doubleValue())) {
            log.error("【微信支付】异步通知，订单金额不一致.orderId={}，微信通知金额={}，系统金额={}",
                    payResponse.getOrderId(),
                    payResponse.getOrderAmount(),
                    orderDTO.getOrderAmount());
            throw new SellException(ResultEnum.WXPAY_NOTIFY_MONEY_VERIFY_ERROR);
        }

//        修改订单支付状态
        orderService.paid(orderDTO);
        return payResponse;
    }
```

处理金额的数学

```java
package com.bennyrhys.wechat_order.utils;

/**
 * 金额计算
 * @Author bennyrhys
 * @Date 2020-07-01 09:59
 */
public class MathUtil {

    private static final Double MONEY_RANGE = 0.01;

    /*比较两金额相减，判断金额一致*/
    public static boolean equals(Double d1, Double d2) {
        double result = Math.abs(d1 - d2);
        return (result < MONEY_RANGE) ? true : false;
    }
}
```

PayController

```java
/**
 * 微信异步通知
 * 会传入xml的字符串
 * 接收到通知，同步返回xml确认信息
 * @param notifyData
 */
@PostMapping("/notify")
public ModelAndView notify(@RequestBody String notifyData) {
    payService.notify(notifyData);

    //返回给微信处理结果
    return new ModelAndView("pay/success");
}
```

返回的信息templates/pay/success.ftl

```xml
<xml>
    <return_code><![CDATA[SUCCESS]]></return_code>
    <return_msg><![CDATA[OK]]></return_msg>
</xml>
```

## 退款



# 扩展

1. 后台图片上传使用又拍云，[又拍云免费体验](https://console.upyun.com/register/?invite=HyTufSjS-)

