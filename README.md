# GMall

#### 仿天猫、京东类似的B2C电商项目：
     基于springboot、dubbo、zookeeper的微服务

- gmall-user-service用户服务的service层8070
* gmall-user-web用户服务的web层8080
- gmall-manage-service用户服务的service层8071
* gmall-manage-web用户服务的web层8081
* gmall-item-service前台的商品详情服务直接调用gmall-manage-service模块
* gmall-item-web前台的商品详情web层8082
- gmall-search-service用户服务的service层8073
* gmall-search-web用户服务的web层8083
- gmall-cart-service用户服务的service层8074
* gmall-cart-web用户服务的web层8084
* gmall-passport-web用户认证中心端口：8085
* gmall-user-service用户服务的service8070


    项目会持续更新，如需协同开发联系（zachary）：
     
      WeChat:l18391713434
     
     
        Tel:18391713434
        开发环境
工具 	版本号 	下载
JDK 	1.8 	https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
Mysql 	5.7 	https://www.mysql.com/
Redis 	3.2 	https://redis.io/download
Elasticsearch 	6.2.2 	https://www.elastic.co/downloads
MongoDb 	3.2 	https://www.mongodb.com/download-center
RabbitMq 	3.7.14 	http://www.rabbitmq.com/download.html
nginx 	1.10 	http://nginx.org/en/download.html


开发工具
工具 	说明 	官网
IDEA 	开发IDE 	https://www.jetbrains.com/idea/download
RedisDesktop 	redis客户端连接工具 	https://redisdesktop.com/download
Robomongo 	mongo客户端连接工具 	https://robomongo.org/download
SwitchHosts 	本地host管理 	https://oldj.github.io/SwitchHosts/
X-shell 	Linux远程连接工具 	http://www.netsarang.com/download/software.html
Navicat 	数据库连接工具 	http://www.formysql.com/xiazai.html
PowerDesigner 	数据库设计工具 	http://powerdesigner.de/
Axure 	原型设计工具 	https://www.axure.com/
MindMaster 	思维导图设计工具 	http://www.edrawsoft.cn/mindmaster
ScreenToGif 	gif录制工具 	https://www.screentogif.com/
ProcessOn 	流程图绘制工具 	https://www.processon.com/
PicPick 	屏幕取色工具 	https://picpick.app/zh/


技术选型
后端技术
技术 	说明 	官网
Spring Boot 	容器+MVC框架 	https://spring.io/projects/spring-boot
Spring Security 	认证和授权框架 	https://spring.io/projects/spring-security
MyBatis 	ORM框架 	http://www.mybatis.org/mybatis-3/zh/index.html
MyBatisGenerator 	数据层代码生成 	http://www.mybatis.org/generator/index.html
PageHelper 	MyBatis物理分页插件 	http://git.oschina.net/free/Mybatis_PageHelper
Swagger-UI 	文档生产工具 	https://github.com/swagger-api/swagger-ui
Hibernator-Validator 	验证框架 	http://hibernate.org/validator/
Elasticsearch 	搜索引擎 	https://github.com/elastic/elasticsearch
RabbitMq 	消息队列 	https://www.rabbitmq.com/
Redis 	分布式缓存 	https://redis.io/
MongoDb 	NoSql数据库 	https://www.mongodb.com/
Docker 	应用容器引擎 	https://www.docker.com/
Druid 	数据库连接池 	https://github.com/alibaba/druid
OSS 	对象存储 	https://github.com/aliyun/aliyun-oss-java-sdk
JWT 	JWT登录支持 	https://github.com/jwtk/jjwt
LogStash 	日志收集 	https://github.com/logstash/logstash-logback-encoder
Lombok 	简化对象封装工具 	https://github.com/rzwitserloot/lombok
前端技术
技术 	说明 	官网
Vue 	前端框架 	https://vuejs.org/
Vue-router 	路由框架 	https://router.vuejs.org/
Vuex 	全局状态管理框架 	https://vuex.vuejs.org/
Element 	前端UI框架 	https://element.eleme.io/
Axios 	前端HTTP框架 	https://github.com/axios/axios
v-charts 	基于Echarts的图表框架 	https://v-charts.js.org/
Js-cookie 	cookie管理工具 	https://github.com/js-cookie/js-cookie
nprogress 	进度条控件 	https://github.com/rstacruz/nprogress


