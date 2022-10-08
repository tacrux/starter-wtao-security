package pro.wtao.framework.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pro.wtao.framework.security.context.OnlineUserHolder;
import pro.wtao.framework.security.model.AuthReq;
import pro.wtao.framework.security.model.LoginUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 * <b>抽象认证用户提供服务</b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/10/8 14:27    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/10/8
 */
@Slf4j
public abstract class AbstractUserDetailsService<C extends AuthReq> implements UserDetailsService<C> {

    /**
     * 给自定义实现装载请求响应等参数、执行钩子
     * 由pro.wtao.framework.security.service.AuthenticationProvider调用
     * @param authReq	认证请求参数
     * @return
     */
    @Override
    public final LoginUser loadUserDetails( C authReq) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 设置全部请求头
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            request = ((ServletRequestAttributes) requestAttributes).getRequest();
            response = ((ServletRequestAttributes) requestAttributes).getResponse();
        }
        // 加载用户信息

        LoginUser loginUser = this.loadUserDetails(authentication, authReq, request, response);

        // 执行钩子
        beforeVerification(loginUser, authReq,request,response);
        if(!verification(loginUser, authReq,request,response)){
            throw new BadCredentialsException("认证凭据无效");
        }
        afterVerification(loginUser, authReq,request,response);

        return loginUser;
    }


    /**
     * 加载用户详情
     *
     * @param authentication 当前上下文中的授权认证信息
     * @param request
     * @param response
     * @return
     */
    public abstract LoginUser loadUserDetails(Authentication authentication, C authReq, HttpServletRequest request,
                                              HttpServletResponse response);


    @Override
    public void afterVerification(LoginUser loginUser, C authReq, HttpServletRequest request,
                                  HttpServletResponse response) {

    }

    @Override
    public void beforeVerification(LoginUser loginUser, C authReq, HttpServletRequest request,
                                   HttpServletResponse response) {

    }
}

