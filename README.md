# dubbo-demo
dubbo demo
使用maven构建Doubbo服务的可执行jar包
Dobbo 服务的运行方式
1、使用Servlet容器运行（Tomcat、JBoss等）
  缺点：增加复杂性（端口、管理），浪费资源
2、自建Main方法类运行
  缺点：Doubbo本身提供了高级特性没用上，自己编写启动类可能会有缺陷
3、使用Dubbo框架提供的Main方法类来运行（Spring容器）
  优点：框架本身提供（com.alibaba.doubo.container.Main）,可实现优雅关机（ShutdownHook）
