package pro.wtao.framework.security.filter;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pro.wtao.framework.security.model.AuthReq;
import pro.wtao.framework.security.model.AuthenticationToken;
import pro.wtao.framework.security.util.JsonUtil;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <pre>
 * <b>抽象认证过滤器</b>
 * <b>Description:继承该类实现登录接口</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/10/8 11:15    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/10/8
 */
public class AuthenticationFilter<T extends AuthReq> extends AbstractAuthenticationProcessingFilter {
    @Getter
    private final HttpMethod httpMethod;

    @Getter
    private final String pattern;

    private final Class<T> reqType;


    public AuthenticationFilter(String pattern, HttpMethod httpMethod, Class<T> reqType) {
        super(new AntPathRequestMatcher(pattern, httpMethod.name()));
        this.httpMethod = httpMethod;
        this.pattern = pattern;
        this.reqType = reqType;
    }

    @Override
    public void setServletContext(@Nonnull ServletContext servletContext) {
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        assert applicationContext != null;
        AuthenticationSuccessHandler successHandler = applicationContext.getBean(AuthenticationSuccessHandler.class);
        super.setAuthenticationSuccessHandler(successHandler);
    }


    private AuthenticationToken<T> getToken(HttpServletRequest request)
            throws InstantiationException, IllegalAccessException {

        T body = null;
        try {
            body =  JsonUtil.from(request.getInputStream(),reqType);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new BadCredentialsException("请求参数解析异常: "+e.getMessage());
        }

        if (body == null) {
            throw new BadCredentialsException("请求参数不合法");
        }

        return new AuthenticationToken<>(body.getPrincipal(), body);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            return this.getAuthenticationManager().authenticate(getToken(request));
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new BadCredentialsException("认证失败: "+e.getMessage());
        }

    }

    @Override
    @Autowired
    public final void  setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

}

