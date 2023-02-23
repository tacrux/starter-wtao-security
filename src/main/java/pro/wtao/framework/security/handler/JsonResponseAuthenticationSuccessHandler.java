package pro.wtao.framework.security.handler;

import com.google.common.base.Charsets;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import pro.wtao.framework.security.config.SecurityProperties;
import pro.wtao.framework.security.context.OnlineUserContext;
import pro.wtao.framework.security.model.AuthResult;
import pro.wtao.framework.security.model.LoginUser;
import pro.wtao.framework.security.model.Result;
import pro.wtao.framework.security.model.Token;
import pro.wtao.framework.security.util.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

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

    private final OnlineUserContext onlineUserContext;


    private final SecurityProperties properties;

    public JsonResponseAuthenticationSuccessHandler(OnlineUserContext onlineUserContext, SecurityProperties properties) {
        this.onlineUserContext = onlineUserContext;
        this.properties = properties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        LoginUser loginUser = (LoginUser) authentication.getDetails();
        long accessTokenExpiresIn = properties.getAccessTokenExpiresIn();
        long refreshTokenExpiresIn = properties.getRefreshTokenExpiresIn();
        String accessToken = JwtUtils.create(Collections.emptyMap(), Collections.emptyMap(), loginUser.getUsername(), "12", accessTokenExpiresIn, loginUser.getJti());
        HashMap<String, Object> rtHeader = new HashMap<>();
        rtHeader.put("accessJti",loginUser.getJti());
        String refreshToken = JwtUtils.create(rtHeader, Collections.emptyMap(), loginUser.getUsername(), "12", refreshTokenExpiresIn, String.valueOf(UUID.randomUUID()));


        onlineUserContext.put(loginUser);

        response.setCharacterEncoding(Charsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Result.ok(new AuthResult(loginUser, new Token(String.valueOf(accessTokenExpiresIn), accessToken,refreshToken))).writeTo(response.getOutputStream());
    }
}
