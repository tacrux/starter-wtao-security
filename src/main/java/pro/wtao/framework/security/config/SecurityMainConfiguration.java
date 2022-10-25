package pro.wtao.framework.security.config;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pro.wtao.framework.security.context.OnlineUserHolder;
import pro.wtao.framework.security.filter.AbstractAuthenticationFilter;
import pro.wtao.framework.security.filter.JWTAuthorizationFilter;
import pro.wtao.framework.security.handler.JsonResponseAuthenticationEntryPoint;
import pro.wtao.framework.security.handler.JwtAccessDeinedHandler;
import pro.wtao.framework.security.model.Result;

import java.util.List;

/**
 * <pre>
 * <b>主配置</b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/9/30 15:54    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/9/30
 */
@Configuration
@EnableWebSecurity
public class SecurityMainConfiguration {
    // @formatter:off
    /**
     * 静态资源
     */
    private static final String[] STATIC_RESOURCE_MATCHS =
            {"/**/*.css", "/**/*.js", "/**/*.png", "/**/*.gif", "/**/*.jpg", "/actuator/**", "/health/*", "/ws/*", "*.wsdl"};
    // @formatter:on

    @Autowired
    private SecurityProperties properties;

    @Autowired
    private List<AbstractAuthenticationFilter<?>> authenticationFilters;

    /**
     * 直接在过滤器链里面配置httpSecurity
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http, @Autowired OnlineUserHolder onlineUserHolder) throws Exception {

        // 登录接口可直接访问
        for (AbstractAuthenticationFilter<?> f : authenticationFilters) {
            http.authorizeRequests().antMatchers(f.getHttpMethod(), f.getPattern()).permitAll();
        }

        String[] permitPattern = ArrayUtils.addAll(STATIC_RESOURCE_MATCHS, properties.getClient().getPublicUrls());


        return http
                // 允许跨域
                .cors(Customizer.withDefaults())
                // 配置是否需要认证
                .authorizeRequests()
                .antMatchers(permitPattern).permitAll()
                // 其他的接口都需要认证后才能请求
                .anyRequest().access("@accessValidatorChain.validate(request, authentication)")
                .and()
                //jwt入口
                .csrf().disable()
                .addFilterAt(new JWTAuthorizationFilter(onlineUserHolder), BasicAuthenticationFilter.class)
                //错误处理
                // @formatter:off
                .exceptionHandling().accessDeniedHandler(new JwtAccessDeinedHandler(null, HttpStatus.FORBIDDEN.value(), Result.fail(Result.Status.FORBIDDEN)))
                .authenticationEntryPoint(new JsonResponseAuthenticationEntryPoint(HttpStatus.UNAUTHORIZED.value(), Result.fail(Result.Status.UNAUTHORIZED)))
                .and().build();

    }

}
