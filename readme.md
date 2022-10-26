# 安全框架
## 用法
### 引入依赖
```xml
        <dependency>
            <groupId>pro.wtao</groupId>
            <artifactId>starter-wtao-security</artifactId>
            <version>0.0.1-SNAPSHOT</version>
            <scope>compile</scope>
        </dependency>
```
### 自定义用户查找校验逻辑，继承AbstractUserDetailsService， 实现loadUserDetails和verification方法

### 继承AuthReq，定义登录参数，往IOC容器注入一个适配登录参数的AuthenticationFilter