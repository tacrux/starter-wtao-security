package pro.wtao.framework.security.handler;

import com.google.common.base.Charsets;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import pro.wtao.framework.security.config.SecurityProperties;
import pro.wtao.framework.security.context.OnlineUserHolder;
import pro.wtao.framework.security.model.AuthResult;
import pro.wtao.framework.security.model.LoginUser;
import pro.wtao.framework.security.model.Result;
import pro.wtao.framework.security.model.Token;
import pro.wtao.framework.security.util.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * <pre>
 * <b></b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/10/8 11:27    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/10/8
 */
public class JsonResponseAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final OnlineUserHolder onlineUserHolder;


    private final SecurityProperties properties;

    public JsonResponseAuthenticationSuccessHandler(OnlineUserHolder onlineUserHolder, SecurityProperties properties) {
        this.onlineUserHolder = onlineUserHolder;
        this.properties = properties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        LoginUser loginUser = (LoginUser) authentication.getDetails();
        long jwtExp = properties.getJwtExp();
        String jwt = JwtUtils.create(Collections.emptyMap(), Collections.emptyMap(), loginUser.getUsername(), "12", jwtExp, loginUser.getJti());

        onlineUserHolder.put(loginUser);

        response.setCharacterEncoding(Charsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Result.ok(new AuthResult(loginUser, new Token(jwtExp, jwt))).writeTo(response.getOutputStream());
    }
}
