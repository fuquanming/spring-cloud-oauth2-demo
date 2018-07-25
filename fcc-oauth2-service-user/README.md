# 加入eureka注册中心

* UserServiceApplication，添加标记@EnableDiscoveryClient

* application.yml，添加注册中心配置信息
```
eureka:
    client:
        service-url:
            defaultZone: http://localhost:20000/eureka/  
```
* pom.xml，添加注册中心客户端配置信息
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka</artifactId>
    <version>2.0.0.M2</version>
</dependency>
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpcore</artifactId>
</dependency>
```