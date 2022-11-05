package pro.wtao.framework.security.component.AnnotationAccessProviders;

import org.springframework.stereotype.Component;
import pro.wtao.framework.security.model.RequestMatchInfo;

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
public class LocalAnnotationAccessProviderImpl extends AbstractAnnotationAccessProvider {

    protected static Map<RequestMatchInfo, Annotation> annotationMappings = new HashMap<>();


    @Override
    void putAllAnnotationMappings(Map<RequestMatchInfo, Annotation> annotationMappings) {

    }

    @Override
    public Annotation getAccessAnnotation(HttpServletRequest request) {
        return annotationMappings.get(RequestMatchInfo.fromRequest(request));
    }

    public Map<RequestMatchInfo, Annotation> getAnnotationMappings() {
        return annotationMappings;
    }
}
