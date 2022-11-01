# JWT安全框架

## 说明

### 微服务集成
#### 授权服务器

1. 授权服务器引入依赖

```xml

<dependency>
    <groupId>pro.wtao</groupId>
    <artifactId>starter-wtao-security</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```

2. 继承AuthReq，定义登录参数

```java
public class UsernamePasswordReqVo extends AuthReq {
    private String username;
    private String password;

    @Override
    public String getPrincipal() {
        return username;
    }
}
//getter and setter...
```

3. 往IOC容器注入一个适配上述登录参数的AuthenticationFilter

```java

@Configuration
public class AuthBeans {
    /**
     * 用户名密码登录入口
     */
    @Bean
    AuthenticationFilter<UsernamePasswordReqVo> usernamePasswordFilter() {
        // 指定路径，请求方式，入参类型
        return new AuthenticationFilter<>("/login/pwd", HttpMethod.POST, UsernamePasswordReqVo.class);
    }
}
```

5. 自定义用户查找校验逻辑，继承AbstractUserDetailsService， 实现loadUserDetails和verification方法
6. 配置属性
> 授权服务器同时也是受保护的服务端
```properties
# 直接开放的接口，逗号风格，支持ant表达式
wtao.security.public-urls=""
# 本服务唯一标识，同pro.wtao.framework.security.model.UriGrantedAuthority.systemCode
wtao.security.system-code="demo"
```

```java

@Service
public class UserPasswordUserDetailService extends AbstractUserDetailsService<UsernamePasswordReqVo> {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDao userDao;

    // 重写用户查找逻辑
    @Override
    public LoginUser loadUserDetails(Authentication authentication, UsernamePasswordReqVo parameter,
                                     HttpServletRequest request, HttpServletResponse response) {
        //查找用户
        UserEntity userEntity = userDao.selectByUsername(parameter.getUsername());
        //构建LoginUser
        LoginUserExt loginUser = new LoginUserExt();
        BeanUtils.copyProperties(user, loginUser);
        //这里可以携带登陆用户的业务信息，如所属企业等
        loginUser.setExtInfo(String.format("我是%s号用户", loginUser.getUserid()));
        return loginUser;
    }

    // 重新校验逻辑，成功返回true
    @Override
    public boolean verification(LoginUser loginUser, UsernamePasswordReqVo parameter, HttpServletRequest request,
                                HttpServletResponse response) {
        return passwordEncoder.matches(parameter.getPassword(), loginUser.getPassword());
    }

    @AllArgsConstructor
    @Data
    public static class UserEntity {
        private String userid;
        private String username;
        private String password;
        private String phone;
        private String email;
        private Boolean enabled;
        private Collection<UriGrantedAuthority> authorities;
    }

    @Data
    public static class LoginUserExt extends LoginUser {
        private String extInfo;
    }
}


```

#### 受保护的服务端
1. 服务端引入依赖

```xml

<dependency>
    <groupId>pro.wtao</groupId>
    <artifactId>starter-wtao-security</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```
2. 配置属性
```properties
# 直接开放的接口，逗号风格，支持ant表达式
wtao.security.public-urls=""
# 本服务唯一标识，同pro.wtao.framework.security.model.UriGrantedAuthority.systemCode
wtao.security.system-code="demo"

```
### 单机服务集成
同 <a name="授权服务器">授权服务器</a> 