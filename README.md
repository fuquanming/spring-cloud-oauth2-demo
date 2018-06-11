spring cloud2/boot2 + oauth2 + zuul + swagger2

简要spring security oauth2认证思路。

* client模式，没有用户的概念，直接与认证服务器交互，用配置中的客户端信息去申请accessToken，客户端有自己的client_id,client_secret对应于用户的username,password，而客户端也拥有自己的authorities，当采取client模式认证时，对应的权限也就是客户端自己的authorities。

* password模式，自己本身有一套用户体系，在认证时需要带上自己的用户名和密码，以及客户端的client_id,client_secret。此时，accessToken所包含的权限是用户本身的权限，而不是客户端的权限。


###fcc-oauth2-server
**oauth2服务端
**使用oauth2协议来做api-gateway，已实现方式client_credentials,refresh_token,password
**mysql存储username,password(表:sys_user),client_id,client_secret(表:oauth_client_details),脚本oauth2.sql
**redis存储token

password执行顺序：

1、ClientDetailsServiceImpl：自定义查看clientId接口，判断clientId是否存在

2、AuthenticationProviderImpl：spring-security 登录鉴权-检验登录方式为password时username、password值判断，默认返回Token对象表示登陆成功

3、UserDetailsServiceImpl：自定义用户查询接口-username是否存在

4、PasswordEncoderImpl：自定义密码编码

client_credentials执行顺序：

1、ClientDetailsServiceImpl

2、PasswordEncoderImpl


**可以使用grant_type的方式为client_credentials,refresh_token,password

password请求地址：http://localhost:60000/oauth/token?username=fqm&password=fqm&grant_type=password&client_id=client_2&client_secret=client_2secret
```
{   
    "access_token":"b217d948-39e7-47dd-b373-07a4186fac5d",
    "token_type":"bearer",
    "refresh_token":"37357f35-6f71-4eb4-8056-6375e734f88a",
    "expires_in":28385,
    "scope":"select"
}
```

client请求地址：http://localhost:60000/oauth/token?grant_type=client_credentials&client_id=client_2&client_secret=client_2secret
```
{
    "access_token":"ff579450-d88e-46f6-b435-8cd78f7c7371",
    "token_type":"bearer",
    "expires_in":43199,
    "scope":"select"
}
```

refresh_token请求地址：http://localhost:60000/oauth/token?grant_type=refresh_token&client_id=client_2&client_secret=client_2secret&refresh_token=37357f35-6f71-4eb4-8056-6375e734f88a
```
{
  "access_token": "a43ddf64-945a-4c4f-9df3-5d232867d7d1",
  "token_type": "bearer",
  "refresh_token": "37357f35-6f71-4eb4-8056-6375e734f88a",
  "expires_in": 43199,
  "scope": "select"
}
```


***fcc-oauth2-resource
**oauth2需要授权的资源端
**redis获取token并鉴权
**添加自定义oauth2过滤器在鉴权成功后，在请求头里设置username的参数
**使用zuul转发api数据
**添加集成各个swagger2的api路由

* 需要鉴权的接口

地址：http://localhost:60001/api/product/1?access_token=cc0d0c8e-6c68-45e3-a6ed-80f2b65dc2cb&name=张三1
```
{
    "productId":"1",
    "productName":"张三1",
    "info":"http://127.0.0.1:60012/-product:access_token:cc0d0c8e-6c68-45e3-a6ed-80f2b65dc2cb<br/>name:张三1<br/>oauth2-username:fqm"
}
```

地址：http://localhost:60001/api/user/1?access_token=cc0d0c8e-6c68-45e3-a6ed-80f2b65dc2cb&name=张三1
```
{
    "userId":"1",
    "userName":"张三1",
    "info":"http://127.0.0.1:60011/-user:access_token:cc0d0c8e-6c68-45e3-a6ed-80f2b65dc2cb<br/>name:张三1<br/>oauth2-username:fqm"
}
```

*oauth2需要授权的资源端
1、继承ResourceServerConfigurerAdapter，引入redis，重写方法
```
    @Autowired
    RedisConnectionFactory redisConnectionFactory;
``` 
```
    /** 需要鉴权的URL地址 */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/api/**").authenticated();
        //.antMatchers(HttpMethod.POST, "/foo").hasAuthority("FOO_WRITE");
    }
``` 

```
    /** 设置鉴权token的来源 */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // resourceId 在数据库oauth_client_details中的资源resource_Ids
        resources.resourceId("oauth2-resource").tokenStore((new RedisTokenStoreImpl(redisConnectionFactory)));// spring-cloud
    }
```    

OAuth2HeaderFilter：自定义拦截器Oauth2认证成功后添加自定义头文件信息
```
            // 设置oauth2的username，URLEncoder编码（如果有中文的话）
            ctx.addZuulRequestHeader("oauth2-username", URLEncoder.encode(username, "UTF-8"));
            // api 获取方式如下：
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("oauth2-username");
``` 

* zuul配置信息：转发api数据到真是的内部接口
```
    zuul:
        routes:
            api-user:
                stripPrefix: false # 默认true，重定向时，/api/不在请求路径里，即访问http://127.0.0.1:60001/api/product/1,则重定向到http://127.0.0.1:60000/product/1
                path: /api/user/**
                url: http://127.0.0.1:60011/      
            api-product:
                stripPrefix: false # 默认true，重定向时，/api/不在请求路径里，即访问http://127.0.0.1:60001/api/product/1,则重定向到http://127.0.0.1:60000/product/1
                path: /api/product/**
                url: http://127.0.0.1:60012/      
        host:
            max-per-route-connections: 1000
            max-total-connections: 1000 
```         

* 添加集成各个swagger2的api路由
访问地址：http://localhost:60001/swagger-ui.html

1、添加依赖
```
    <dependency>
        <groupId>com.spring4all</groupId>
        <artifactId>swagger-spring-boot-starter</artifactId>
        <version>1.7.1.RELEASE</version>
    </dependency>
```     
2、实现SwaggerResourcesProvider接口，参见DocumentationConfig
3、添加配置信息，配置接口地址中关于swagger2的信息
```
swagger:
    api:
        names: service-user,service-product
        locations: http://localhost:60011/v2/api-docs,http://localhost:60012/v2/api-docs 
        versions: 2.0,2.0 
``` 
4、fcc-oauth2-service-product和fcc-oauth2-service-user为具体实现api的接口应用

*** 使用swagger2做在线API
** fcc-oauth2-service-product使用swagger-spring-boot-starter集成
** fcc-oauth2-service-user使用io.springfox集成
** 添加防止跨域的类CorsFilterConfig
