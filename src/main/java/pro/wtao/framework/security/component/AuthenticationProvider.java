package pro.wtao.framework.security.component;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import pro.wtao.framework.security.context.ApplicationContextHolder;
import pro.wtao.framework.security.model.AuthReq;
import pro.wtao.framework.security.model.AuthenticationToken;
import pro.wtao.framework.security.model.LoginUser;

/**
 * <pre>
 * <b>用户提供者</b>
 * <b>Description:替换spring-security组件</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/10/8 14:40    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/10/8
 */
public class AuthenticationProvider<C extends AuthReq> implements org.springframework.security.authentication.AuthenticationProvider {



    @Override
    public final AuthenticationToken<C> authenticate(Authentication authentication) throws AuthenticationException {
        if(!(authentication instanceof AuthenticationToken)){
            throw new AuthenticationServiceException("不支持的凭据类型");
        }
        AuthenticationToken<C> authenticationToken = (AuthenticationToken<C>)authentication;

        LoginUser loginUser = ApplicationContextHolder.getBean(UserDetailsService.class).loadUserDetails(authenticationToken.getCredentials());
        if(loginUser==null){
            throw new AuthenticationServiceException("用户获取失败");
        }

        //通过认证
        authenticationToken.setDetails(loginUser);
        authenticationToken.setAuthenticated(true);

        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(AuthenticationToken.class);
    }

}
