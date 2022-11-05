package pro.wtao.framework.security.component;

import pro.wtao.framework.security.model.AuthReq;
import pro.wtao.framework.security.model.LoginUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 * <b>认证用户加载接口</b>
 * <b>Description:替代spring-security的组件, 由</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/10/8 14:25    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/10/8
 */
public interface UserDetailsService <C extends AuthReq> {
    /**
     * 加载用户详情
     * @param authReq	认证请求参数
     * @return
     */
    LoginUser loadUserDetails(C authReq);


    boolean verification(LoginUser loginUser, C authReq, HttpServletRequest request, HttpServletResponse response);


    /**
     * 用户认证授权通过后
     * @param loginUser	登录用户信息
     * @param request
     * @param response
     */
    void afterVerification(LoginUser loginUser, C authReq, HttpServletRequest request, HttpServletResponse response);


    /**
     * 用户认证授权通过后
     * @param loginUser	登录用户信息
     * @param request
     * @param response
     */
    void beforeVerification(LoginUser loginUser, C authReq, HttpServletRequest request, HttpServletResponse response);

}
