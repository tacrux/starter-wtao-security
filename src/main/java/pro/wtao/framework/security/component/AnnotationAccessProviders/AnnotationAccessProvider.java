package pro.wtao.framework.security.component.AnnotationAccessProviders;

import lombok.Data;
import org.springframework.util.AntPathMatcher;
import pro.wtao.framework.security.model.UriGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Objects;

public interface AnnotationAccessProvider {
    /**
     * 获取URL的最优先注解权限
     */
    Annotation getAccessAnnotation(HttpServletRequest request);


}
