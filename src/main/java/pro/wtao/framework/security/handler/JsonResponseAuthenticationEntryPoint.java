package pro.wtao.framework.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import pro.wtao.framework.security.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <pre>
 * <b>处理认证过程的错误</b>
 * <b>Description: 统一包装响应到Json</b>
 *
 * <b>Author:</b> tacrux
 * <b>Date:</b> 2022/9/7 13:45
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/9/7 13:45    tacrux     new file.
 * </pre>
 */
@Slf4j
public class JsonResponseAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 响应状态码
     */
    private final int httpStatus;
    /**
     * 响应体
     */
    private final Object body;

    /**
     * 唯一构造方法
     *
     * @param httpStatus 响应状态码
     * @param body       响应体
     */
    public JsonResponseAuthenticationEntryPoint(int httpStatus, Object body) {
        this.httpStatus = httpStatus;
        this.body = body;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.warn(authException.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus);
        JsonUtil.getObjectMapper().writeValue(response.getOutputStream(), body);

    }
}
