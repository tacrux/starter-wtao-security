package pro.wtao.framework.security.component.AnnotationAccessProviders;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

public interface AnnotationAccessProvider {
    /**
     * 获取URL的最优先注解权限
     */
    Annotation getAccessAnnotation(HttpServletRequest request);


}
