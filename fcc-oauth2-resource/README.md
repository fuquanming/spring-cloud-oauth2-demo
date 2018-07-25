# 加入eureka注册中心，开启熔断

* application.yml，添加注册中心配置信息
```
eureka:
    client:
        service-url:
            defaultZone: http://localhost:20000/eureka/  
```
* application.yml，添加user服务配置，使用serviceId，注释url
```
            #url: http://127.0.0.1:60011/
            serviceId: user    # 注册中心使用，UserServiceFallback中getRoute()返回该名称
```
* pom.xml，添加注册中心客户端配置信息
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka</artifactId>
    <version>2.0.0.M2</version>
</dependency>
```
* 新增UserServiceFallback，对服务user，熔断配置输出

* 不使用熔断，当服务不能访问时，输出的信息如下
```
{
    "timestamp": "2018-07-25T03:31:04.713+0000",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Connect to 127.0.0.1:60011 [/127.0.0.1] failed: Connection refused: connect"
}
```
* 熔断，当服务不能访问时，屏蔽内部错误信息，输出的信息如下
```
{
    "error": "service user lost"
}
```
