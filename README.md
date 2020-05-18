# id-generator 分布式ID生成器

## 概述
id-generator分布式ID生成器, 解决在分布式系统唯一性标识生成复杂、不统一的问题,如数据库分库分表数据唯一标识、业务流水号等场景. id-generator提供一站式部署, 稳定、高性能的ID生成系统.
## 功能
+ 提供全局唯一ID序号
+ 提供隔离增长序号，以唯一KEY为自增ID标识, 如数据库-表之间的自增id效果；
+ 支持自定义增长步长；
+ 支持固定前缀；
+ 支持固定位数
+ 支持ID重置；
+ 支持页面配置，即时生效； 
+ 支持Dubbo、Spring Cloud应用部署
+ ~~支持JDBC插件~~

## 设计

## 快速开始
#### 下载与构建
```bash
git clone https://github.com/haozi2015/id-generator.git
cd id-generator
mvn -DskipTests clean package
```
[more...](./docs/quick-start.md)
