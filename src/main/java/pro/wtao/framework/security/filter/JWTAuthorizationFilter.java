package pro.wtao.framework.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;
import pro.wtao.framework.security.context.OnlineUserHolder;
import pro.wtao.framework.security.model.LoginUser;
import pro.wtao.framework.security.model.Result;
import pro.wtao.framework.security.util.JsonUtil;
import pro.wtao.framework.security.util.JwtUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <pre>
 * <b></b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/10/8 17:47    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/10/8
 */
@Slf4j
public class JWTAuthorizationFilter  extends OncePerRequestFilter {

    private final OnlineUserHolder onlineUserHolder;

    public JWTAuthorizationFilter(OnlineUserHolder onlineUserHolder) {
        this.onlineUserHolder = onlineUserHolder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        try {
            // 从header或参数列表中获取token
            String tokenParameterName = HttpHeaders.AUTHORIZATION;
            token = StringUtils.defaultString(request.getParameter("accessToken"), request.getHeader(tokenParameterName));
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(JsonUtil.to(Result.fail(Result.Status.UNAUTHORIZED)));
            return;
        }
        // 移除Bearer Token认证所带的前缀，兼容使用Authorization授权
        token = StringUtils.trim(token.replaceAll("Bearer", ""));

        if (StringUtils.isBlank(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 校验解码jwt
        DecodedJWT decodedjwt = null;
        try {
            decodedjwt = JwtUtils.decode(token);
            Assert.notNull(decodedjwt,"token验证失败");
        } catch (Exception e) {
            log.error(e.getMessage());
            Result.fail(Result.Status.UNAUTHORIZED).writeTo(response.getOutputStream());
            return;
        }

        // 设置认证信息到上下文
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(decodedjwt));

        filterChain.doFilter(request, response);
        return;
    }


    public Authentication getAuthentication(DecodedJWT decodedjwt) {
        LoginUser loginUser = onlineUserHolder.get(decodedjwt.getId());
        return new UsernamePasswordAuthenticationToken(loginUser.getUsername(), decodedjwt.getToken(), loginUser.getAuthorities());
    }
}

