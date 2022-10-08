package pro.wtao.framework.security.service.AuthorityValidators;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

public interface AccessValidator {
    /**
     * 权限校验逻辑
     *
     * @param request
     * @param authentication
     * @return
     */
    boolean validate(HttpServletRequest request, Authentication authentication);


    /**
     * <pre>
     * 	是否支持
     * </pre>
     *
     * @param annotation
     * @return
     */
    boolean isSupport(Annotation annotation);

}
