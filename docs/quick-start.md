# Quick Start
### 先决条件
1. JDK 1.8+;
2. Maven 3.2.x+;
3. Git

### 下载与构建
```bash
git clone https://github.com/haozi2015/id-generator.git
cd id-generator
mvn -DskipTests clean package
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
