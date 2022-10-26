package pro.wtao.security.demo.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import pro.wtao.framework.security.filter.AuthenticationFilter;
import pro.wtao.security.demo.security.UsernamePasswordReqVo;

/**
 * <pre>
 * <b></b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/10/26 15:58    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/10/26
 */
@Configuration
public class AuthBeans {
    /**
     * 用户名密码登录入口
     */
    @Bean
    AuthenticationFilter<UsernamePasswordReqVo> usernamePasswordFilter() {
        return new AuthenticationFilter<>("/login/pwd", HttpMethod.POST, UsernamePasswordReqVo.class);
    }
}
