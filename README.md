# dubbo-demo
## 介绍
    dubbo-common-parent：  maven 父类
    dubbo-common-core:  公共核心类
    dubbo-facade-admin: admin 服务接口
    dubbo-service-admin:  admin dubbo 服务提供者
    dubbo-web-admin:  admin dubbo 服务消费者
    dubbo-admin:  dubbo 后台管理平台
    dubbo-monitor:  dubbo 监控中心

## 使用的技术：spring,spring mvc,mybatis,redis,dubbo,zookeeper,swagger

## 启动服务
使用maven构建Dubbo服务的可执行jar包
Dubbo 服务的运行方式
1、使用Servlet容器运行（Tomcat、JBoss等）
  缺点：增加复杂性（端口、管理），浪费资源
2、自建Main方法类运行
  缺点：Doubbo本身提供了高级特性没用上，自己编写启动类可能会有缺陷
3、使用Dubbo框架提供的Main方法类来运行（Spring容器）
  优点：框架本身提供（com.alibaba.doubo.container.Main）,可实现优雅关机（ShutdownHook）

## 启动dubbo服务
  java -jar dubbo-service-admin.jar &
