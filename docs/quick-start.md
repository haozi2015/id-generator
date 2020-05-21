# Quick Start
### 先决条件
1. JDK 1.8+;
2. Maven 3.2.x+;
3. Git

### 下载与构建
```bash
git clone https://github.com/haozi2015/id-generator.git
cd id-generator
mvn -DskipTests clean package -P build
```
### 存储模式二选一
+ MySQL, 执行初始化脚本 [idgenerator.sql](./mysql/idgenerator.sql)
+ Redis, 无

### 启动控制面板
```bash
cd id-generator-dashboard/target/
tar -xvf id-generator-dashboard-1.0-SNAPSHOT.tar.gz
#vim ./id-generator-dashboard/application.yml --配置存储地址
./id-generator-dashboard/start.sh
```
### 启动ID生成器三选一
#### 简单版
+ 自定义注册中心

```bash
cd id-generator-app/id-generator-simple/target/
tar -xvf id-generator-simple-1.0-SNAPSHOT.tar.gz
#vim ./id-generator-simple/application.yml --配置存储地址
./id-generator-simple/start.sh
```

#### Spring Cloud
+ Eureka注册中心

```bash
cd id-generator-app/id-generator-springcloud/target/
tar -xvf id-generator-springcloud-1.0-SNAPSHOT.tar.gz
#vim ./id-generator-springcloud/application.yml --配置存储、Eureka地址
./id-generator-springcloud/start.sh
```

#### Dubbo
+ zookeeper注册中心

```bash
cd id-generator-app/id-generator-dubbo/target/
tar -xvf id-generator-dubbo-1.0-SNAPSHOT.tar.gz
#vim ./id-generator-dubbo/application.yml --配置存储、zookeeper地址
./id-generator-dubbo/start.sh
```


### 配置文件 application.yml
SpringBoot标准
```yaml
#应用端口
server:
  port: 6080

---
spring:
  application:
    #Spring应用名称
    name: id-generator-XXX

#支持分布式框架一：dubbo方式提供服务，部署id-generator-dubbo-provider项目配置参数
---
dubbo:
  scan:
    #扫描包路径 无需修改
    base-packages: com.haozi.id.generator.dubbo.provider
  application:
    #dubbo应用名称
    name: id-generator-provider
  registry:
    #dubbo注册/发现，使用zookeeper
    address: zookeeper://127.0.0.1:2181
  protocol:
    #dubbo使用的协议
    name: dubbo

#支持分布式框架二：Spring Cloud方式提供服务，部署id-generator-spring-cloud项目配置参数
---
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:1001/eureka/


#存储方式一：MySQL存储，与spring.redis配置二选一
---
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/idgenerator
    username: root
    password: root

#存储方式二：Redis存储，与spring.datasource配置二选一
---
spring:
  redis:
    host: 127.0.0.1
    port: 6379
  #集群方式
# cluster:
#   nodes: 127.0.0.1:7001,127.0.0.1:7002,127.0.0.1:7003,127.0.0.1:7004,127.0.0.1:7005,127.0.0.1:7006
    

---
id:
  generator:
    #单次请求批量获取ID最大限制
    one-request-num-max: 50
    #控制台地址
    dashboard: http://127.0.0.1:6082
``` 