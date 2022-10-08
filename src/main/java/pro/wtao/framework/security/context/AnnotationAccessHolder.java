package pro.wtao.framework.security.context;

import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import pro.wtao.framework.security.annotation.LoginAccess;
import pro.wtao.framework.security.annotation.NonAccess;
import pro.wtao.framework.security.annotation.RbacAccess;
import pro.wtao.framework.security.model.UriGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * <pre>
 * <b>注解-控制器映射 持有者</b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/10/8 17:28    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/10/8
 */
public class AnnotationAccessHolder implements ApplicationListener<ContextRefreshedEvent> {

    private static Map<RequestSimpleMatchInfo, Annotation> annotationMappings = new HashMap<>();

    private static final List<Class<? extends Annotation>> SCAN_ANNOTATIONS =
            new ArrayList<>(Arrays.asList(NonAccess.class, LoginAccess.class, RbacAccess.class));
    private static final Class<? extends Annotation> DEFAULT_ACCESS = RbacAccess.class;

    private boolean refreshed;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (refreshed) {
            return;
        }
        //scanAnnotations添加自定义注解，优先级最高

        ApplicationContext context = event.getApplicationContext();

        // 获取所有RequestMapping
        RequestMappingHandlerMapping mappings = context.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handelMethods = mappings.getHandlerMethods();
        // @formatter:off
        handelMethods.forEach((mapping, method) -> {

            // 获取最优先的方法注解
            Annotation annotation =
                    SCAN_ANNOTATIONS.stream().map(method::getMethodAnnotation).filter(Objects::nonNull).findFirst()
                            .orElse(null);
            // 没有方法注解，获取最优先的类注解
            annotation = annotation == null ?
                    SCAN_ANNOTATIONS.stream().map(a -> method.getBeanType().getAnnotation(a)).filter(Objects::nonNull)
                            .findFirst().orElse(null) : annotation;

            //默认注解
            annotation = (Annotation) ObjectUtils.defaultIfNull(annotation, DEFAULT_ACCESS);

            //组装map
            Annotation finalAnnotation = annotation;
            mapping.getMethodsCondition().getMethods().stream()
                    .flatMap(requestMethod -> mapping.getPatternValues().stream()
                            .map(path -> new RequestSimpleMatchInfo(requestMethod.name(), path)))
                    .distinct()
                    .forEach(simpleMatchInfo -> annotationMappings.put(simpleMatchInfo, finalAnnotation));
        });
        // @formatter:on

        refreshed = true;
    }

    /**
     * 获取URL的最优先注解权限
     */
    public static Annotation getAccessAnnotation(HttpServletRequest request) {
        return annotationMappings.get(RequestSimpleMatchInfo.fromRequest(request));
    }

    /**
     * <pre>
     * <b>请求匹配信息，包含请求头和路径</b>
     * <b>Description:</b>
     *
     * <b>Author:</b> tacrux
     * <b>Date:</b> 2022/9/8 10:47
     * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
     * <b>Changelog:</b>
     *   Ver   		Date                    Author               	 Detail
     *   ----------------------------------------------------------------------
     *   1.0   2022/10/8 17:28    Wangtao     new file.
     * </pre>
     *
     * @author Wangtao
     * @since 2022/10/8
     */
    @Data
    public static class RequestSimpleMatchInfo {
        private AntPathMatcher antPathMatcher = new AntPathMatcher(",");
        private String requestMethod;
        private String requestUri;

        public RequestSimpleMatchInfo(String requestMethod, String requestUri) {
            this.requestMethod = requestMethod;
            this.requestUri = requestUri;
        }

        public static RequestSimpleMatchInfo fromRequest(HttpServletRequest request) {
            return new RequestSimpleMatchInfo(request.getMethod(), request.getRequestURI());
        }

        public boolean match(UriGrantedAuthority uriGrantedAuthority) {
            return antPathMatcher.match(uriGrantedAuthority.getUri(), requestUri)
                    && Objects.equals(uriGrantedAuthority.getMethod().name(), requestMethod);
        }
    }
}
