package pro.wtao.framework.security.config;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pro.wtao.framework.security.component.AccessValidatorChain;
import pro.wtao.framework.security.component.AnnotationAccessProviders.AnnotationAccessProvider;
import pro.wtao.framework.security.component.AnnotationAccessProviders.LocalAnnotationAccessProviderImpl;
import pro.wtao.framework.security.component.AnnotationAccessProviders.RedisAnnotationAccessProviderImpl;
import pro.wtao.framework.security.component.AuthenticationProvider;
import pro.wtao.framework.security.component.AuthorityValidators.AccessValidator;
import pro.wtao.framework.security.component.matchInfoConverters.DefaultMachInfoConverter;
import pro.wtao.framework.security.component.matchInfoConverters.MatchInfoConverter;
import pro.wtao.framework.security.context.OnlineUserHolder;
import pro.wtao.framework.security.context.RedisOnlineUserHolder;
import pro.wtao.framework.security.filter.AuthenticationFilter;
import pro.wtao.framework.security.filter.JWTAuthorizationFilter;
import pro.wtao.framework.security.handler.JsonResponseAuthenticationEntryPoint;
import pro.wtao.framework.security.handler.JsonResponseAuthenticationSuccessHandler;
import pro.wtao.framework.security.handler.JwtAccessDeinedHandler;
import pro.wtao.framework.security.model.LoginUser;
import pro.wtao.framework.security.model.Result;

import java.util.*;

/**
 * <pre>
 * <b>?????????</b>
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
@AutoConfiguration
@ConditionalOnProperty(name = "wtao.security.server-type",havingValue = "AUTHORIZATION_SERVER",matchIfMissing = true)
@EnableWebSecurity
public class GatewayAutoConfig {
    // @formatter:off
    /**
     * ????????????
     */
    private static final String[] STATIC_RESOURCE_MATCHS =
            {"/**/*.css", "/**/*.js", "/**/*.png", "/**/*.gif", "/**/*.jpg", "/actuator/**", "/health/*", "/ws/*", "*.wsdl"};
    // @formatter:on

    @Autowired
    private SecurityProperties properties;

    @Autowired
    private List<AuthenticationFilter<?>> authenticationFilters;


    @Bean
    AuthenticationSuccessHandler authenticationSuccessHandler(OnlineUserHolder onlineUserHolder, SecurityProperties properties) {
        return new JsonResponseAuthenticationSuccessHandler(onlineUserHolder, properties);
    }

    /**
     * ????????????
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
    AuthenticationProvider<?> authenticationProvider() {
        return new AuthenticationProvider<>();
    }


    @Bean
    AuthenticationManager authenticationManager(AuthenticationProvider<?> authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }

    /**
     * ???????????????redis?????? ????????????????????????
     *
     * @return
     */
    @Bean
    AnnotationAccessProvider redisAnnotationAccessProviderImpl(SecurityProperties properties, RedisTemplate<String, ?> redisTemplate) {
        return new RedisAnnotationAccessProviderImpl(properties,redisTemplate);
    }

    /**
     * ????????????????????????????????????
     *
     * @param accessValidators ?????????
     */
    @Bean
    public AccessValidatorChain accessValidatorChain(@Autowired List<? extends AccessValidator> accessValidators,
                                                     AnnotationAccessProvider annotationAccessProvider) {
        return new AccessValidatorChain(accessValidators, annotationAccessProvider);
    }

    @Bean
    @ConditionalOnMissingBean(MatchInfoConverter.class)
    public MatchInfoConverter matchInfoConverter(){
        return new DefaultMachInfoConverter();
    }

    /**
     * ?????????????????????
     */
    @Bean
    public AuthenticationTrustResolver authenticationTrustResolver() {
        return new AuthenticationTrustResolverImpl();
    }


    @Bean
    OnlineUserHolder onlineUserHolder(RedisTemplate<String, LoginUser> redisTemplate) {
        return new RedisOnlineUserHolder(redisTemplate);
    }



    /**
     * ?????????????????????????????????httpSecurity
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http, @Autowired OnlineUserHolder onlineUserHolder) throws Exception {

        // ???????????????????????????
        for (AuthenticationFilter<?> f : authenticationFilters) {
            http.authorizeRequests().antMatchers(f.getHttpMethod(), f.getPattern()).permitAll();
        }

        String[] permitPattern = ArrayUtils.addAll(STATIC_RESOURCE_MATCHS, properties.getClient().getPublicUrls());


        return http
                // ????????????
                .cors(Customizer.withDefaults())
                // ????????????????????????
                .authorizeRequests()
                .antMatchers(permitPattern).permitAll()
                // ?????????????????????????????????????????????
                .anyRequest().access("@accessValidatorChain.validate(request, authentication)")
                .and()
                //jwt??????
                .csrf().disable()
                .addFilterAt(new JWTAuthorizationFilter(onlineUserHolder), BasicAuthenticationFilter.class)
                //????????????
                // @formatter:off
                .exceptionHandling().accessDeniedHandler(new JwtAccessDeinedHandler(null, HttpStatus.FORBIDDEN.value(), Result.fail(Result.Status.FORBIDDEN)))
                .authenticationEntryPoint(new JsonResponseAuthenticationEntryPoint(HttpStatus.UNAUTHORIZED.value(), Result.fail(Result.Status.UNAUTHORIZED)))
                .and().build();

    }

}
