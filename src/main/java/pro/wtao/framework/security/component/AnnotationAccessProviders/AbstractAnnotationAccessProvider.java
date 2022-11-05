package pro.wtao.framework.security.component.AnnotationAccessProviders;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import pro.wtao.framework.security.annotation.LoginAccess;
import pro.wtao.framework.security.annotation.NonAccess;
import pro.wtao.framework.security.annotation.RbacAccess;
import pro.wtao.framework.security.model.RequestMatchInfo;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * <pre>
 * <b></b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/11/4 16:15    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/11/4
 */
public abstract class AbstractAnnotationAccessProvider implements ApplicationListener<ContextRefreshedEvent>, AnnotationAccessProvider {
    private static final List<Class<? extends Annotation>> SCAN_ANNOTATIONS =
            new ArrayList<>(Arrays.asList(NonAccess.class, LoginAccess.class, RbacAccess.class));
    private static final Annotation DEFAULT_ACCESS = () -> RbacAccess.class;
    private boolean refreshed;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (refreshed) {
            return;
        }
        Map<RequestMatchInfo, Annotation> annotationMappings= new HashMap<>();

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
            annotation = ObjectUtils.defaultIfNull(annotation, DEFAULT_ACCESS);

            //组装map
            Annotation finalAnnotation = annotation;
            mapping.getMethodsCondition().getMethods().stream()
                    .flatMap(requestMethod -> mapping.getPatternValues().stream()
                            .map(path -> new RequestMatchInfo(requestMethod.name(), path)))
                    .distinct()
                    .forEach(simpleMatchInfo -> annotationMappings.put(simpleMatchInfo, finalAnnotation));
        });

        // @formatter:on
        // 本服务的annotationMappings存储起来
        putAllAnnotationMappings(annotationMappings);

        refreshed = true;

    }

    abstract void putAllAnnotationMappings(Map<RequestMatchInfo, Annotation> annotationMappings);
}
