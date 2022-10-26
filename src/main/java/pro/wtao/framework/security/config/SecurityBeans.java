package pro.wtao.framework.security.config;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import pro.wtao.framework.security.context.OnlineUserHolder;
import pro.wtao.framework.security.context.RedisOnlineUserHolder;
import pro.wtao.framework.security.handler.JsonResponseAuthenticationSuccessHandler;
import pro.wtao.framework.security.model.LoginUser;
import pro.wtao.framework.security.service.AccessValidatorChain;
import pro.wtao.framework.security.service.AuthenticationProvider;
import pro.wtao.framework.security.service.AuthorityValidators.AccessValidator;

import java.util.List;

/**
 * <pre>
 * <b></b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/10/11 9:52    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/10/11
 */
@Configuration
public class SecurityBeans {

    /**
     * 加密方式
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder() {
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.equals(encodedPassword) || super.matches(rawPassword, encodedPassword);
            }
        };
    }

    @Bean
    AuthenticationProvider<?> authenticationProvider(){
        return  new AuthenticationProvider<>();
    }


    @Bean
    AuthenticationManager authenticationManager(AuthenticationProvider<?> authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }

    /**
     * 注解式配置权限校验责任链
     *
     * @param accessValidators 校验器
     */
    @Bean
    public AccessValidatorChain accessValidatorChain(@Autowired List<? extends AccessValidator> accessValidators) {
        return new AccessValidatorChain(accessValidators);
    }

    /**
     * 用户可信解析器
     */
    @Bean
    public AuthenticationTrustResolver authenticationTrustResolver() {
        return new AuthenticationTrustResolverImpl();
    }




    @Bean
    AuthenticationSuccessHandler authenticationSuccessHandler(OnlineUserHolder onlineUserHolder, SecurityProperties properties) {
        return new JsonResponseAuthenticationSuccessHandler(onlineUserHolder,properties);
    }

    @Bean
    OnlineUserHolder onlineUserHolder(RedisTemplate<String, LoginUser> redisTemplate){
        return new RedisOnlineUserHolder(redisTemplate);
    }

}
