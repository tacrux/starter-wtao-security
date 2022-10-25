package pro.wtao.framework.security.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;
import pro.wtao.framework.security.util.JsonUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAccessDeinedHandler implements AccessDeniedHandler {

    // TODO: 2022/9/7 错误页路径由配置指定
    @Nullable
    private final String errorPage;
    /**
     * 响应状态码
     */
    private final int httpStatus;
    /**
     * 响应体
     */
    private final Object body;

    public JwtAccessDeinedHandler(@Nullable String errorPage, int httpStatus, Object body) {
        this.errorPage = errorPage;
        this.httpStatus = httpStatus;
        this.body = body;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 检查是否为Ajax请求
        if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) {
            writeResponseJson(response);
            return;
        }
        if (!response.isCommitted()) {
            if (errorPage != null) {
                // Put exception into request scope (perhaps of use to a view)
                request.setAttribute(WebAttributes.ACCESS_DENIED_403, accessDeniedException);

                // Set the 403 status code.
                response.setStatus(HttpStatus.FORBIDDEN.value());

                // forward to error page.
                RequestDispatcher dispatcher = request.getRequestDispatcher(errorPage);
                dispatcher.forward(request, response);
            } else {
                writeResponseJson(response);
            }
        }
    }

    private void writeResponseJson(HttpServletResponse response) throws IOException {
        response.setStatus(httpStatus);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        JsonUtil.getObjectMapper().writeValue(response.getOutputStream(), body);
    }

}
